package cn.mic.cloud.rocket.mq.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 延迟的配置类
 */
@AllArgsConstructor
public enum MqDelayLevel {
    /**
     * 延迟1秒
     */
    DELAY_1S(1),
    DELAY_5S(2),
    DELAY_10S(3),
    DELAY_30S(4),
    /**
     * 延迟1分钟
     */
    DELAY_1M(5),
    DELAY_2M(6),
    DELAY_3M(7),
    DELAY_4M(8),
    DELAY_5M(9),
    DELAY_6M(10),
    DELAY_7M(11),
    DELAY_8M(12),
    DELAY_9M(13),
    DELAY_10M(14),

    /**
     * 延迟1小时
     */
    DELAY_1H(15),
    DELAY_2H(16),
    ;
    @Getter
    private int level;

}
