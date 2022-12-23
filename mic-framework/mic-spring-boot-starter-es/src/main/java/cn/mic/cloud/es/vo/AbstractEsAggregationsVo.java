package cn.mic.cloud.es.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.elasticsearch.search.aggregations.Aggregations;

/**
 * 数据统计
 */
@ToString
public abstract class AbstractEsAggregationsVo {

    /**
     * 数据统计
     */
    @Setter
    @Getter
    private Aggregations aggregations;

}
