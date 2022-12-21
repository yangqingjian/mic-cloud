package cn.mic.cloud.framework.redis.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author yangqingjian
 * @date 2020/09/03
 */
@Data
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "spring.redis.redisson")
public class RedissonProperties {

    private String masterName;

    private String sentinelAddresses;

    private String password;

    private int timeout = 5000;
    /**
     * 数量不能低于32个
     */
    private int masterCount = 32;
    /**
     * 数量不能低于32个
     */
    private int slaveCount = 32;
}
