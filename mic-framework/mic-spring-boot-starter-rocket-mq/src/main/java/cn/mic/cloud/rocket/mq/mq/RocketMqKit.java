package cn.mic.cloud.rocket.mq.mq;

import cn.mic.cloud.freamework.common.core.JmsEntity;
import cn.mic.cloud.freamework.common.exception.BusinessException;
import cn.mic.cloud.rocket.mq.constants.MqDelayLevel;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.Assert;

/**
 */
@Slf4j
public class RocketMqKit {

    private static long rocket_timeout = 30 * 1000L;

    private static RocketMQTemplate rocketMQTemplate;

    public RocketMqKit(RocketMQTemplate rocketMQTemplate) {
        log.info(" RocketMqKit 注入 RocketMQTemplate 实例");
        RocketMqKit.rocketMQTemplate = rocketMQTemplate;
    }

    private static RocketMQTemplate getRocketMQTemplate() {
        if (rocketMQTemplate == null) {
            if (log.isErrorEnabled()) {
                log.error("RocketMqKit.rocketMQTemplate未注入成功, 需要在启动类配置@ComponentScan(basePackages = {\"com.ihome.common.mq\"}");
            }
            throw new BusinessException("RocketMqKit.rocketMQTemplate 未注入成功");
        }
        return rocketMQTemplate;
    }


    public static SendResult sendDelay(JmsEntity<?> vo, MqDelayLevel delayLevel) {
        SendResult sendResult = getRocketMQTemplate().syncSend(vo.getTopic(), getMessage(vo), rocket_timeout, delayLevel.getLevel());
        if (log.isInfoEnabled()) {
            log.info("发送消息: {}, 结果:{}", JSONObject.toJSONString(vo), sendResult);
        }
        return sendResult;
    }

    public static SendResult send(JmsEntity<?> vo) {
        SendResult sendResult = getRocketMQTemplate().syncSend(vo.getTopic(), getMessage(vo), rocket_timeout);
        if (log.isInfoEnabled()) {
            log.info("发送消息: {}, 结果:{}", JSONObject.toJSONString(vo), sendResult);
        }
        return sendResult;
    }

    public static SendResult sendOrderly(JmsEntity<?> vo) {
        SendResult sendResult = getRocketMQTemplate().syncSendOrderly(vo.getTopic(), getMessage(vo), vo.getMessageId());
        if (log.isInfoEnabled()) {
            log.info("发送消息: {}, 结果:{}", JSONObject.toJSONString(vo), sendResult);
        }
        return sendResult;
    }

    public static void send(JmsEntity<?> vo, SendCallback callback) {
        getRocketMQTemplate().asyncSend(vo.getTopic(), getMessage(vo), new LogSendCallBack(vo, callback), rocket_timeout);
    }

    public static void sendOrderly(JmsEntity<?> vo,SendCallback callback) {
        getRocketMQTemplate().asyncSendOrderly(vo.getTopic(), getMessage(vo), vo.getMessageId(),new LogSendCallBack(vo, callback), rocket_timeout);
    }

    public static void sendDelay(JmsEntity<?> vo, MqDelayLevel delayLevel, SendCallback callback) {
        getRocketMQTemplate().asyncSend(vo.getTopic(), getMessage(vo), new LogSendCallBack(vo, callback), rocket_timeout, delayLevel.getLevel());
    }

    public static void sendOneWay(JmsEntity<?> vo) {
        getRocketMQTemplate().sendOneWay(vo.getTopic(), getMessage(vo));
        if (log.isInfoEnabled()) {
            log.info("发送消息 {}", JSONObject.toJSONString(vo));
        }
    }

    public static void sendOneWayOrderly(JmsEntity<?> vo) {
        getRocketMQTemplate().sendOneWayOrderly(vo.getTopic(), getMessage(vo), vo.getMessageId());
        if (log.isInfoEnabled()) {
            log.info("发送消息 {}", JSONObject.toJSONString(vo));
        }
    }

    public static SendResult sendTransaction(JmsEntity<?> vo) {
        TransactionSendResult sendResult = getRocketMQTemplate().sendMessageInTransaction(vo.getTopic(), getMessage(vo), null);
        if (log.isInfoEnabled()) {
            log.info("发送消息: {}, 结果:{}", JSONObject.toJSONString(vo), sendResult);
        }
        return sendResult;
    }


    private static Message getMessage(JmsEntity<?> vo) {
        Assert.notNull(vo.getTopic(), "消息topic不能为空！");
        return MessageBuilder.withPayload(vo)
                .setHeaderIfAbsent(MessageConst.PROPERTY_KEYS, vo.getMessageKey())
                .build();
    }

    @AllArgsConstructor
    private static class LogSendCallBack implements SendCallback {

        private JmsEntity<?> msg;

        private SendCallback callback;

        @Override
        public void onSuccess(SendResult sendResult) {
            if (log.isInfoEnabled()) {
                log.info("异步发送消息: {}, 结果:{}", JSONObject.toJSONString(msg), sendResult);
            }
            if (callback != null) {
                callback.onSuccess(sendResult);
            }
        }

        @Override
        public void onException(Throwable e) {
            if (log.isErrorEnabled()) {
                log.info("异步发送消息: {}, 发生异常:{}", JSONObject.toJSONString(msg), e);
            }
            if (callback != null) {
                callback.onException(e);
            }
        }
    }

}
