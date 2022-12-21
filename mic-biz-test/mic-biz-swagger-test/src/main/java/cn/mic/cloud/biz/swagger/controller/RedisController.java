package cn.mic.cloud.biz.swagger.controller;

import cn.mic.cloud.framework.redis.anno.RedisLockAnnotation;
import cn.mic.cloud.framework.redis.common.RedisLockTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author : YangQingJian
 * @date : 2022/12/21
 */
@RestController
@Slf4j
@RequestMapping("/redis")
public class RedisController {

    /**
     * 测试锁
     * @param sleep
     * @return
     */
    @GetMapping("/testRedisLock")
    @RedisLockAnnotation(typeEnum = RedisLockTypeEnum.ONE, lockTime = 30 , tryCount = 3)
    public String testRedisLock(@RequestParam("sleep") Long sleep) {
        try {
            log.info("睡眠执行前");
            Thread.sleep(sleep);
            log.info("睡眠执行后");
        } catch (Exception e) {
            // log error
            log.info("has some error", e);
            return "error" + e.getMessage();
        }
        return "测试中文："+String.valueOf(sleep);
    }



}
