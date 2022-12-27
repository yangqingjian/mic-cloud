package cn.mic.cloud.security.constants;

import cn.mic.cloud.freamework.common.constants.CommonEnum;
import lombok.Getter;

/**
 * 登录方式
 *
 * @author : YangQingJian
 * @date : 2022/12/27
 */
public enum LoginTypeEnum implements CommonEnum {

    USERNAME_PASSWORD("usernamePassword", "用户名密码"),
    PHONE_VERIFICATION_CODE("phoneVerificationCode", "手机验证码"),
    WE_CHAT_CODE("weChatCode", "微信验证码");

    @Getter
    private String code;

    @Getter
    private String desc;

    LoginTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
