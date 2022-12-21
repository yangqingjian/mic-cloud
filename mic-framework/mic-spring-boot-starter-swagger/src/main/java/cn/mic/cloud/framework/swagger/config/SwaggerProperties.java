package cn.mic.cloud.framework.swagger.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author yangqingjian
 * @date 2022/12/21
 * swagger配置得实体类
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "mic-swagger")
public class SwaggerProperties {
    /**
     * 分组名称
     */
    private String groupName = "默认分组";
    /**
     * swagger会解析的包路径
     **/
    private String basePackage = "";
    /**
     * 是否启用
     */
    private Boolean enable = true;

    /**
     * 标题
     **/
    private String title = "默认标题";
    /**
     * 描述
     **/
    private String description = "默认描述";
    /**
     * 版本
     **/
    private String version = "default-1.1";

    /**
     * host信息
     **/
    private String host;

    /**
     * 联系人
     */
    private String contactName = "杨青见";
    /**
     * 联系人
     */
    private String contactEmail = "578142919@qq.com";
    /**
     * 联系地址
     */
    private String contactUrl = "http://www.mic.cn";

}
