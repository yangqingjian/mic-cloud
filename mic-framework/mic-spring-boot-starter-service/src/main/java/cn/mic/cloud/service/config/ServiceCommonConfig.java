package cn.mic.cloud.service.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author : YangQingJian
 * @date : 2022/12/28
 */
@Configuration
@EnableFeignClients(basePackages = {"cn.mic.cloud.service.feign"})
@ComponentScan("cn.mic.cloud.service")
@Slf4j
public class ServiceCommonConfig {




}
