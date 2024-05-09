package cn.mic.cloud.freamework.common.core.login.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author : YangQingJian
 * @date : 2022/12/28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
