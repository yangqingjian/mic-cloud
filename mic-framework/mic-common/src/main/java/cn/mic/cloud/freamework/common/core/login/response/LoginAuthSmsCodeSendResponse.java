package cn.mic.cloud.freamework.common.core.login.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @author : YangQingJian
 * @date : 2022/12/23
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class LoginAuthSmsCodeSendResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 验证码
     */
    private String smsCode;
    /**
     * 有效时间
     */
    private Integer timeoutSeconds;

}
