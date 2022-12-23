package cn.mic.cloud.es.response;

import cn.mic.cloud.es.vo.AbstractEsAggregationsVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.elasticsearch.search.aggregations.Aggregations;

/**
 * 分页数据带统计结果集
 * @param <T>
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class EsSearchPageAggreationsResponseVo<T> extends AbstractEsAggregationsVo {

    /**
     * 分页数据
     */
    Page<T> page ;

    /**
     * 数据统计
     */
    private Aggregations aggregations;

}
