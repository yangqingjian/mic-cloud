package cn.mic.cloud.web.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author : YangQingJian
 * @date : 2022/12/28
 */
@Configuration
@EnableFeignClients(basePackages = {"cn.mic.cloud.web"})
@ComponentScan("cn.mic.cloud.web")
@Slf4j
public class WebCommonConfig {





}
