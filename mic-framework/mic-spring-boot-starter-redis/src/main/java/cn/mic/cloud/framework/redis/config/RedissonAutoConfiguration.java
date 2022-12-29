package cn.mic.cloud.framework.redis.config;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * Redisson 连接配置
 *
 * @author yangqingjian
 * @date 2022/12/21
 */
@Configuration
@ComponentScan(basePackages = "cn.mic.cloud.framework.redis")
@Slf4j
public class RedissonAutoConfiguration {

    @Resource
    private RedissonProperties redissonProperties;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSentinelServers().addSentinelAddress(redissonProperties.getSentinelAddresses().split(","))
                .setMasterName(redissonProperties.getMasterName())
                .setTimeout(redissonProperties.getTimeout())
                .setPassword(redissonProperties.getPassword())
                .setMasterConnectionPoolSize(redissonProperties.getMasterCount())
                .setSlaveConnectionPoolSize(redissonProperties.getSlaveCount());
        return Redisson.create(config);
    }
}
