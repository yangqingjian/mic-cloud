package cn.mic.cloud.es.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.elasticsearch.search.builder.SearchSourceBuilder;

/**
 * 查询
 *
 * @author yangqingjian
 * @date 2020/11/26
 */
@Data
@NoArgsConstructor
@ToString
public class BaseEsRequestVo<T> {
    /**
     * 索引名称
     */
    private String indexName;

    /**
     * 查询信息
     */
    private SearchSourceBuilder sourceBuilder;

    /**
     * clazz
     */
    private Class<T> clazz;


}
