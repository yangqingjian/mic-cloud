package cn.mic.cloud.biz.caas.feign.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * @author : YangQingJian
 * @date : 2022/4/19
 */
@Configuration
@EnableFeignClients(basePackages = "cn.mic.cloud.biz.caas.feign")
public class MicBizCaasFeignAutoConfig {

}
