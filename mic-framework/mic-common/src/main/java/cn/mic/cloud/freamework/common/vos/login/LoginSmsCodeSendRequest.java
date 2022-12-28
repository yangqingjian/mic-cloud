package cn.mic.cloud.freamework.common.vos.login;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author : YangQingJian
 * @date : 2022/12/28
 */
@Data
public class LoginSmsCodeSendRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    private String mobile;
    /**
     * 验证码有效时长（秒为单位）
     */
    private Integer timeout = 600;

}
