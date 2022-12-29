package cn.mic.cloud.mdc.config;

import cn.mic.cloud.mdc.util.MdcRequestIdUtil;
import feign.RequestInterceptor;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 自动装配mdc
 *
 * @author : YangQingJian
 * @date : 2022/12/6
 */
@Configuration
@ComponentScan(basePackages = "cn.mic.cloud.mdc")
public class MdcAutoConfiguration {

    /**
     * 因为feign的底层是直接循环所有的RequestInterceptor，因此这里可以直接使用
     *
     * @return
     */
    @Bean
    @ConditionalOnClass(value = RequestInterceptor.class)
    RequestInterceptor getMdcRequestInterceptor() {
        return requestTemplate -> {
            //将MDC中uud_request_id传入header
            requestTemplate.header(MdcRequestIdUtil.REQUEST_ID, MDC.get(MdcRequestIdUtil.REQUEST_ID));
        };
    }


}
