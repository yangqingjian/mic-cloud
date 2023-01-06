package cn.mic.cloud.code.generate.config;

import cn.hutool.core.date.DateUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import static cn.mic.cloud.code.generate.constants.CodeGenerateConstants.*;

/**
 * @author : YangQingJian
 * @date : 2023/1/4
 */
@Setter
@NoArgsConstructor
public class CodeGenerateConfig {

    /**
     * 注释配置
     */
    @Getter
    private String author = "YangQingJian";

    @Getter
    private String date = DateUtil.format(new Date(), "yyyy/M/d");

    /**
     * 数据库配置
     */
    @Getter
    private String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/mic-test?useUnicode=true&serverTimezone=CTT&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false&useSSL=false&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true";
    @Getter
    private String jdbcUser = "root";
    @Getter
    private String jdbcPwd = "123456";
    @Getter
    private String jdbcDriver = "com.mysql.cj.jdbc.Driver";

    /**
     * 生成文件的路径
     */
    @Getter
    private String basePath = "D:\\code-generate";

    /**
     * 模块
     */
    @Getter
    private String moduleName = "caas";


    private String projectPreName;
    public String getProjectPreName() {
        return projectPre + getModuleName();
    }

    /**
     * 模块的包
     */
    private String basePackage;

    public String getBasePackage() {
        return baseModulePackagePre + getModuleName();
    }

    /**
     * 生成的子包，如“.abc” 记得一定要加"."
     * 为空则不生成子包
     */
    @Getter
    private String subPackage = "";

    /**
     * path
     */
    private String apiPath;

    public String getApiPath() {
        return getBasePath() + "/" + getProjectPreName() + "-api";
    }

    private String domainPath;

    public String getDomainPath() {
        return getBasePath() + "/" + getProjectPreName() + "-domain";
    }

    private String feignPath;
    public String getFeignPath() {
        return getBasePath() + "/" + getProjectPreName() + "-feign";
    }

    private String servicePath;
    public String getServicePath() {
        return getBasePath() + "/" + getProjectPreName() + "-service";
    }

    private String webPath;

    public String getWebPath() {
        return getBasePath() + "/" + getProjectPreName() + "-web";
    }

    /**
     * package
     */
    private String apiPackage;

    public String getApiPackage() {
        return getBasePackage() + ".api" + getSubPackage();
    }

    private String domainPackage;

    public String getDomainPackage() {
        return getBasePackage() + ".domain" + getSubPackage();
    }

    private String feignPackage;

    public String getFeignPackage() {
        return getBasePackage() + ".feign" + getSubPackage();
    }

    private String servicePackage;

    public String getServicePackage() {
        return getBasePackage() + ".service" + getSubPackage();
    }

    private String serviceImplPackage;

    public String getServiceImplPackage() {
        return getBasePackage() + ".service" + getSubPackage() + ".impl";
    }

    private String webPackage;

    public String getWebPackage() {
        return getBasePackage() + ".web.controller" + getSubPackage();
    }

    private String repositoryPackage;

    public String getRepositoryPackage() {
        return getBasePackage() + ".repository" + getSubPackage();
    }

    private String repositoryImplPackage;

    public String getRepositoryImplPackage() {
        return getBasePackage() + ".repository" + getSubPackage() + ".impl";
    }

    private String mapperPackage;

    public String getMapperPackage() {
        return getBasePackage() + ".mapper" + getSubPackage();
    }

    /**
     * package对应的路径
     */
    private String fullApiPackagePath;

    public String getFullApiPackagePath() {
        return getApiPath() + baseSrcPath + getApiPackage().replace(".", "/") + "/";
    }

    private String fullDomainPackagePath;

    public String getFullDomainPackagePath() {
        return getDomainPath() + baseSrcPath + getDomainPackage().replace(".", "/") + "/";
    }

    private String fullFeignPackagePath;

    public String getFullFeignPackagePath() {
        return getFeignPath() + baseSrcPath + getFeignPackage().replace(".", "/") + "/";
    }

    private String fullServicePackagePath;

    public String getFullServicePackagePath() {
        return getServicePath() + baseSrcPath + getServicePackage().replace(".", "/") + "/";
    }

    private String fullServiceImplPackagePath;

    public String getFullServiceImplPackagePath() {
        return getServicePath() + baseSrcPath + getServiceImplPackage().replace(".", "/") + "/";
    }

    private String fullRepositoryPackagePath;

    public String getFullRepositoryPackagePath() {
        return getServicePath() + baseSrcPath + getRepositoryPackage().replace(".", "/") + "/";
    }

    private String fullRepositoryImplPackagePath;

    public String getFullRepositoryImplPackagePath() {
        return getServicePath() + baseSrcPath + getRepositoryImplPackage().replace(".", "/") + "/";
    }

    private String fullWebPackagePath;

    public String getFullWebPackagePath() {
        return getWebPath() + baseSrcPath + getWebPackage().replace(".", "/") + "/";
    }

    private String fullMapperPackagePath;

    public String getFullMapperPackagePath() {
        return getServicePath() + baseSrcPath + getMapperPackage().replace(".", "/") + "/";
    }

    public CodeGenerateConfig(String author, String moduleName, String basePath, String subPackage) {
        this.author = author;
        this.moduleName = moduleName;
        this.basePath = basePath;
        this.subPackage = subPackage;
    }

    public CodeGenerateConfig(String moduleName, String basePath, String subPackage) {
        this.moduleName = moduleName;
        this.basePath = basePath;
        this.subPackage = subPackage;
    }

    /**
     * 模块和主目录
     * @param moduleName
     * @param basePath
     */
    public CodeGenerateConfig(String moduleName, String basePath) {
        this.moduleName = moduleName;
        this.basePath = basePath;
    }

}
