package cn.mic.cloud.web.config;

//import cn.mic.cloud.web.converter.Enum2StringConverterFactory;
//import cn.mic.cloud.web.converter.String2EnumConverterFactory;
//import cn.mic.cloud.web.converter.StringToIEnumConverterFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
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
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.defaultContentType(MediaType.APPLICATION_JSON_UTF8);
    }

    /**
     * 注册转换器
     *
     * @param registry
     */
   /* @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new StringToIEnumConverterFactory());
        registry.addConverterFactory(new String2EnumConverterFactory());
        registry.addConverterFactory(new Enum2StringConverterFactory());
    }*/

}
