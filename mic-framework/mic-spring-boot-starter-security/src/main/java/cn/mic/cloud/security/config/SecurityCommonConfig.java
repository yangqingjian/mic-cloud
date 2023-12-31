package cn.mic.cloud.security.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author : YangQingJian
 * @date : 2022/12/27
 */
@Configuration
@Data
@RefreshScope
@NoArgsConstructor
@ConfigurationProperties(prefix = "security")
@ComponentScan("cn.mic.cloud.security")
public class SecurityCommonConfig {

    /**
     * 免登陆地址
     */
    private List<String> ignoreUrls;

    /**
     * 忽略token转换
     */
    private List<String> ignoreTokenAuthentication;

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


    /**
     * jwt密钥
     */
    private String publicKey = "123456789";

    /**
     * 失效日期，秒 默认为10小时 = 36000
     */
    private Integer expireTimeSeconds = 36000;


}
