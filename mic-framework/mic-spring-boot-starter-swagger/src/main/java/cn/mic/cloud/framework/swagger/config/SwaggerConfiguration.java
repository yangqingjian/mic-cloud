package cn.mic.cloud.framework.swagger.config;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.ArrayList;
import java.util.List;

/**
 * 配置类，swagger.enabled参数可以在配置文件中配置，如果为false就不开放文档，这里matchIfMissing给的默认是true
 */
@Slf4j
@Configuration
@EnableSwagger2WebMvc
@RequiredArgsConstructor
public class SwaggerConfiguration {

    private final SwaggerProperties swaggerProperties;

    @Bean
    public Docket dockerBean() {
        if (StrUtil.isBlank(swaggerProperties.getBasePackage())) {
            log.warn("swagger扫描包未配置，已忽略swagger配置");
            return null;
        }
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo(swaggerProperties))
                //分组名称
                .groupName(swaggerProperties.getGroupName())
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))
                .paths(PathSelectors.any())
                .paths(PathSelectors.regex("^(?!auth).*$"))
                .build()
                .enable(swaggerProperties.getEnable())
                .securitySchemes(securitySchemes())//接口头部信息验证
                .securityContexts(securityContexts());//设置哪些头部信息需要验证
        if (StrUtil.isNotBlank(swaggerProperties.getHost())) {
            return docket.host(swaggerProperties.getHost());
        }
        return docket;
    }

    /**
     * 安全模式，这里指定token通过Authorization头请求头传递
     */
    private List<SecurityScheme> securitySchemes() {
        List<SecurityScheme> apiKeyList = new ArrayList<SecurityScheme>();
        apiKeyList.add(new ApiKey("Authorization", "Authorization", "header"));
        return apiKeyList;
    }

    /**
     * 安全上下文，设置哪些的接口需要进行头部认证，默认排除/auth验证路径
     */
    private List<SecurityContext> securityContexts() {
        List<SecurityContext> securityContexts = new ArrayList<>();
        securityContexts.add(
                SecurityContext.builder()
                        .forPaths(PathSelectors.regex("^.*(?<!auth)//.*$"))//排除掉认证的auth接口路径
                        .securityReferences(defaultAuth())//添加全局借鉴，不需要每个接口都填写头部内容
                        .build());
        return securityContexts;
    }

    /**
     * 默认的全局鉴权策略，头部认证，
     *
     * @return
     */
    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> securityReferences = new ArrayList<>();
        securityReferences.add(new SecurityReference("Authorization", authorizationScopes));
        return securityReferences;
    }

    private ApiInfo apiInfo(SwaggerProperties swaggerProperties) {
        return new ApiInfoBuilder()
                .title(swaggerProperties.getTitle())//api名称
                .description(swaggerProperties.getDescription())//api文档描述
                .contact(new Contact(swaggerProperties.getContactName(), swaggerProperties.getContactUrl(), swaggerProperties.getContactEmail()))//联系人信息
                .version(swaggerProperties.getVersion())//版本号
                .build();
    }
}
