package cn.mic.cloud.common.web.config;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 解决中文乱码问题
 *
 * @author : YangQingJian
 * @date : 2022/12/21
 */
@Configuration
@ComponentScan(basePackages = "cn.mic.cloud.common.web")
public class WebCommonConfiguration implements WebMvcConfigurer {

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.defaultContentType(MediaType.APPLICATION_JSON_UTF8);
    }


}
