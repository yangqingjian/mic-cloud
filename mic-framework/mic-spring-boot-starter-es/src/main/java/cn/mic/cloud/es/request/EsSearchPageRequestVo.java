package cn.mic.cloud.es.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 分页请求参数
 *
 * @author yangqingjian
 * @date 2020/11/26
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class EsSearchPageRequestVo<T> extends BaseEsRequestVo {

    /**
     * 每页显示条数
     */
    private Integer pageSize = 10;

    /**
     * 当前页，1开始
     */
    private Integer current = 1;


}
