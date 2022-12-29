package cn.mic.cloud.service.config;

import cn.mic.cloud.freamework.common.constants.SecurityConstants;
import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author : YangQingJian
 * @date : 2022/12/28
 */
@Configuration
public class ServiceRestTemplateRequestInterceptor {

    /**
     * 因为feign的底层是直接循环所有的RequestInterceptor，因此这里可以直接使用
     *
     * @return
     */
    @Bean
    @ConditionalOnClass(value = RequestInterceptor.class)
    RequestInterceptor getServiceAuthorizationRequestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            //将token传入到header
            requestTemplate.header(SecurityConstants.HEADER_TOKEN_PARAM, servletRequestAttributes.getRequest().getHeader(SecurityConstants.HEADER_TOKEN_PARAM));
        };
    }
}
