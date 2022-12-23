package cn.mic.cloud.biz.test.service.jms.consumer;

import cn.mic.cloud.biz.test.vo.DemoMessageVo;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author : YangQingJian
 * @date : 2022/12/23
 */
@Component
@RocketMQMessageListener(consumerGroup = "consumer-group-" + DemoMessageVo.TOPIC, topic = DemoMessageVo.TOPIC)
@Slf4j
public class DemoMessageVoConsumer implements RocketMQListener<DemoMessageVo> {

    @Override
    public void onMessage(DemoMessageVo message) {
        log.info("DemoMessageVo onMessage  = {}", JSON.toJSONString(message));
    }

}
