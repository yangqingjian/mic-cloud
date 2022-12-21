package cn.mic.cloud.framework.redis.comp;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.mic.cloud.framework.redis.anno.RedisLockAnnotation;
import cn.mic.cloud.framework.redis.common.RedisLockDefinitionHolder;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author : YangQingJian
 * @date : 2022/12/21
 */
@Configuration
@Aspect
@Slf4j
public class RedisLockAop {

    @Resource
    private RedisTemplate redisTemplate;

    // 扫描的任务队列
    private static ConcurrentLinkedQueue<RedisLockDefinitionHolder> holderList = new ConcurrentLinkedQueue();

    /**
     * 线程池，维护keyAliveTime
     */
    private static final ScheduledExecutorService SCHEDULER = new ScheduledThreadPoolExecutor(1,
            new BasicThreadFactory.Builder().namingPattern("redisLock-schedule-pool").daemon(true).build());

    /**
     * @annotation 中的路径表示拦截特定注解
     */
    @Pointcut("@annotation(cn.mic.cloud.framework.redis.anno.RedisLockAnnotation)")
    public void redisLockPC() {
    }

    @Around(value = "redisLockPC()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        // 解析参数
        Method method = resolveMethod(pjp);
        RedisLockAnnotation annotation = method.getAnnotation(RedisLockAnnotation.class);
        // 省略很多参数校验和判空
        String businessKey = getBusinessKey(pjp, annotation, method);
        log.info("-------------businessKey={} , start----------", businessKey);
        String uniqueValue = UUID.randomUUID().toString();
        // 加锁
        Object result = null;
        boolean releaseFlag = false;
        /**
         * 释放锁标识
         */
        try {
            boolean isSuccess = redisTemplate.opsForValue().setIfAbsent(businessKey, uniqueValue);
            if (!isSuccess) {
                log.info("-------------businessKey={} , 当前已有对象持有锁----------", businessKey);
                throw new Exception("当前已有对象持有锁，请稍后再试");
            }
            /**
             * 设置标识
             */
            releaseFlag = true;
            redisTemplate.expire(businessKey, annotation.lockTime(), TimeUnit.SECONDS);
            Thread currentThread = Thread.currentThread();
            // 将本次 Task 信息加入「延时」队列中
            holderList.add(new RedisLockDefinitionHolder(businessKey, annotation.lockTime(), System.currentTimeMillis(),
                    currentThread, annotation.tryCount()));
            // 执行业务操作
            result = pjp.proceed();
            // 线程被中断，抛出异常，中断此次请求
            if (currentThread.isInterrupted()) {
                log.info("-------------businessKey={} , 当前线程已中断----------", businessKey);
                throw new InterruptedException("当前线程已中断，请稍后再试");
            }
        } catch (InterruptedException e) {
            log.error("当前线程已中断，请稍后再试 , businessKey = {} , ", businessKey, e);
            throw new Exception(e);
        } catch (Exception e) {
            log.error("当前已有对象持有锁，请稍后再试, businessKey = {}", businessKey, e);
            throw e;
        } finally {
            if (ObjectUtil.equals(releaseFlag, true)) {
                redisTemplate.delete(businessKey);
                // 请求结束后，强制删掉 key，释放锁
                log.info("-------------businessKey={} , 已释放锁----------", businessKey);
            }
            log.info("-------------businessKey={} , end----------", businessKey);
        }
        return result;
    }

    private String getBusinessKey(ProceedingJoinPoint pjp, RedisLockAnnotation annotation, Method method) {
        String resultStr;
        Object[] params = pjp.getArgs();
        String attrField = annotation.attrField();
        if (StrUtil.isBlank(attrField)) {
            resultStr = DigestUtil.md5Hex(JSON.toJSONString(params));
        } else {
            Object object = BeanUtil.getFieldValue(params[0], attrField);
            if (ObjectUtil.isEmpty(object)) {
                throw new RuntimeException("加锁时根据【" + attrField + "】获取唯一key失败");
            }
            resultStr = DigestUtil.md5Hex(object.toString());
        }

        String methodName = method.getName();
        String clazzName = pjp.getTarget().getClass().getName();
        /**
         * key = 前缀 + 类名 + 方法名 + 参数名称
         */
        resultStr = String.format("%s:%s:%s:%s", annotation.typeEnum().getCode(), clazzName, methodName, resultStr);
        return resultStr;
    }

    private Method resolveMethod(ProceedingJoinPoint pjp) {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        return method;
    }

    /**
     * 初始化之后的操作
     */
    @PostConstruct
    public void initSchedule() {
        // 5秒执行一次「续时」操作
        SCHEDULER.scheduleAtFixedRate(() -> {
            // 这里记得加 try-catch，否者报错后定时任务将不会再执行=-=
            Iterator<RedisLockDefinitionHolder> iterator = holderList.iterator();
            while (iterator.hasNext()) {
                RedisLockDefinitionHolder holder = iterator.next();
                // 判空
                if (holder == null) {
                    iterator.remove();
                    continue;
                }
                // 判断 key 是否还有效，无效的话进行移除
                if (redisTemplate.opsForValue().get(holder.getBusinessKey()) == null) {
                    iterator.remove();
                    continue;
                }
                // 超时重试次数，超过时给线程设定中断
                if (holder.getCurrentCount() > holder.getTryCount()) {
                    holder.getCurrentTread().interrupt();
                    iterator.remove();
                    continue;
                }
                // 判断是否进入最后三分之一时间
                long curTime = System.currentTimeMillis();
                boolean shouldExtend = (holder.getLastModifyTime() + holder.getModifyPeriod()) <= curTime;
                if (shouldExtend) {
                    holder.setLastModifyTime(curTime);
                    redisTemplate.expire(holder.getBusinessKey(), holder.getLockTime(), TimeUnit.SECONDS);
                    log.info("businessKey : [" + holder.getBusinessKey() + "], 主动延迟时间 : " + holder.getCurrentCount());
                    holder.setCurrentCount(holder.getCurrentCount() + 1);
                }
            }
        }, 0, 5, TimeUnit.SECONDS);
    }


}
