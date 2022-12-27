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




}
