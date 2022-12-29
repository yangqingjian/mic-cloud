package cn.mic.cloud.es.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 搜索引擎相关配置
 *
 * @author yangqingjian
 * @date 2020-11-21
 */
@Data
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "spring.elasticsearch.config")
@ComponentScan(basePackages = "cn.mic.cloud.es")
public class ElasticProperties {

    /**
     * 数据备份数，如果只有一台机器，设置为0
     */
    private Integer replicasNumbers = 0;

    /**
     * 是数据分片数，默认为5，有时候设置为3
     */
    private Integer shardsNumbers = 3;

    /**
     * 刷新速度（如果大批量导入，为了加快速度可以设置大点）
     */
    private String refreshInterval = "5s";

    /**
     * 最大返回的数量
     */
    private Integer maxResultWindow = 10000;

    /**
     * 批量操作的大小
     */
    private Integer batchSize = 10000;

}
