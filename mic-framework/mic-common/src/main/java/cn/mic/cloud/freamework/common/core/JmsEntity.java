package cn.mic.cloud.freamework.common.core;

import java.util.UUID;

/**
 * 消息的基类
 */
public interface JmsEntity<T> {

    default String getMessageKey() {
        return UUID.randomUUID().toString();
    }

    default String getMessageId() {
        return getTopic() + "_" + getMessageKey();
    }

    String getTopic();
}
