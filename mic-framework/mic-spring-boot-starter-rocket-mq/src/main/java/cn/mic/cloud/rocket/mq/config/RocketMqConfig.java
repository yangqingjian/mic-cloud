package cn.mic.cloud.rocket.mq.config;

import cn.mic.cloud.rocket.mq.mq.RocketMqKit;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * rocketmq配置类
 *
 * @author yangqingjian
 * @date 2022/12/23
 */
@Configuration
public class RocketMqConfig {

    @Bean
    public RocketMqKit getRocketMq(@Lazy RocketMQTemplate rocketMQTemplate) {
        return new RocketMqKit(rocketMQTemplate);
    }

}
