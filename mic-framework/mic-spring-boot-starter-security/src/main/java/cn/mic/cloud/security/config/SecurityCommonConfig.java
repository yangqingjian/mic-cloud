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
    private String selectByLoginNameUrl = "lb://mic-biz-test-service/demo/selectByLoginName?username=%s";

    /**
     * 根据手机号获取用户信息
     */
    private String selectByMobileUrl = "lb://mic-biz-test-service/demo/selectByMobile?mobile=%s";;

    /**
     * 根据微信号获取用户信息
     */
    private String selectByWxChatUrl;

    /**
     * 超时时间为3秒
     */
    private Integer readTimeOut = 3000;

    /**
     * 连接时间为1秒
     */
    private Integer connectTimeOut = 1000;




}
