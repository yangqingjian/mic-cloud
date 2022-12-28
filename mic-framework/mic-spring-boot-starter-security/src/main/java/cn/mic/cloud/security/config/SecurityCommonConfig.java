package cn.mic.cloud.security.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author : YangQingJian
 * @date : 2022/12/27
 */
@Configuration
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "security")
@ComponentScan("cn.mic.cloud.security")
public class SecurityCommonConfig {

    /**
     * 免登陆地址
     */
    private List<String> ignoreUrls;

    /**
     * 根据登录名获取用户信息
     */
    private String getLoginUserForUsernameUrl = "lb://mic-biz-test-service/demo/getLoginUser";

    /**
     * 根据手机号获取用户信息
     */
    private String getLoginUserForSmsUrl = "lb://mic-biz-test-service/demo/getLoginUser";

    /**
     * 发送手机验证码
     */
    private String sendSmsCodeUrl = "lb://mic-biz-test-service/demo/sendSmsCode";;

    /**
     * 根据微信号获取用户信息
     */
    private String selectByWxChatUrl;

    /**
     * 超时时间为3秒
     */
    private Integer readTimeOut = 300000;

    /**
     * 连接时间为1秒
     */
    private Integer connectTimeOut = 1000;




}
