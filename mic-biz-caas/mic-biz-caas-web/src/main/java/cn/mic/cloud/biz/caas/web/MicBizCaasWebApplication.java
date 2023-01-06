package cn.mic.cloud.biz.caas.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author : YangQingJian
 * @date : 2022/12/22
 */
@SpringBootApplication
@EnableDiscoveryClient
public class MicBizCaasWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicBizCaasWebApplication.class, args);
    }

}
