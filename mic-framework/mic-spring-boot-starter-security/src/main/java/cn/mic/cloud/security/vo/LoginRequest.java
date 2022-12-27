package cn.mic.cloud.security.vo;

import cn.mic.cloud.security.constants.LoginTypeEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author : YangQingJian
 * @date : 2022/12/23
 */
@Data
public class LoginRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 登录方式
     * 密码登录  验证码登录  微信登录
     */
    @NotNull(message = "登录方式不能为空")
    private LoginTypeEnum loginType = LoginTypeEnum.USERNAME_PASSWORD;

    /**
     * 登录名
     */
    @NotBlank(message = "登录名不能为空")
    private String loginName;

    /**
     * 密钥 : 前端完成加密过程
     */
    @NotBlank(message = "密钥不能为空")
    private String loginSecret;


}
