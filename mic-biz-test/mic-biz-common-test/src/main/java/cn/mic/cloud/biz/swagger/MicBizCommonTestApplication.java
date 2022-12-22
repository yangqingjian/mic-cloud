package cn.mic.cloud.biz.swagger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author : YangQingJian
 * @date : 2022/12/20
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"cn.mic.cloud.biz.swagger.feign"})
public class MicBizCommonTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(MicBizCommonTestApplication.class, args);
    }
}
