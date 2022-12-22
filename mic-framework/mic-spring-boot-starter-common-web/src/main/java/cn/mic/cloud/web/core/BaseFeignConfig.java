package cn.mic.cloud.web.core;

import feign.Contract;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : YangQingJian
 * @date : 2022/6/28
 */
@ConditionalOnClass(value = SpringMvcContract.class)
@Configuration
public class BaseFeignConfig {

    @Bean
    public Contract feignContract() {
        return new HierarchicalContract();
    }

}
