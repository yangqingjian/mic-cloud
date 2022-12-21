package cn.mic.cloud.framework.redis.anno;

import cn.mic.cloud.framework.redis.common.RedisLockTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : YangQingJian
 * @date : 2022/12/21
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface RedisLockAnnotation {
    /**
     * 对象的字段
     * @return
     */
    String attrField() default "";
    /**
     * 超时重试次数
     */
    int tryCount() default 0;
    /**
     * 自定义加锁类型
     */
    RedisLockTypeEnum typeEnum() default RedisLockTypeEnum.ONE;
    /**
     * 释放时间，秒 s 单位
     */
    long lockTime() default 30;
}
