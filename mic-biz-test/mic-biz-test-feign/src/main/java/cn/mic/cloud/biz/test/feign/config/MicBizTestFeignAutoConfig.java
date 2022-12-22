package cn.mic.cloud.biz.test.feign.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * @author : YangQingJian
 * @date : 2022/4/19
 */
@Configuration
@EnableFeignClients(basePackages = "cn.mic.cloud.biz.test.feign")
public class MicBizTestFeignAutoConfig {

}
