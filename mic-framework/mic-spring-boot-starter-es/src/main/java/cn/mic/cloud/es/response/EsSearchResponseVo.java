package cn.mic.cloud.es.response;

import cn.mic.cloud.es.vo.AbstractEsAggregationsVo;
import lombok.*;

import java.util.List;

/**
 * 搜索结果
 * @author yangqingjian
 * @date 2020-11-27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
public class EsSearchResponseVo<T> extends AbstractEsAggregationsVo {

    /**
     * 结果集
     */
    private List<T> list;

    /**
     * 总数
     */
    private long total = 0;



}
