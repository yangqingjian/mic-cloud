package cn.mic.cloud.biz.test.vo;

import cn.mic.cloud.freamework.common.core.JmsEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author : YangQingJian
 * @date : 2022/12/23
 */
@Data
@NoArgsConstructor
public class DemoMessageVo implements JmsEntity, Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TOPIC = "test-demo-topic";
    /**
     * 用户名称
     */
    private String userName;

    public String getTopic() {
        return TOPIC;
    }

}
