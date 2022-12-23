package cn.mic.cloud.es.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author yangqingjian
 * @date 2020/11/25
 */
public class IdxVo {
    /**
     * 索引名称
     */
    @Setter
    @Getter
    private String idxName;

    /**
     * 索引配置
     */
    @Setter
    @Getter
    private IdxSql idxSql;

    public static class IdxSql {
        /**
         * 是否动态
         */
        @Setter
        @Getter
        private boolean dynamic = false;

        /**
         * 字段名称
         */
        @Setter
        @Getter
        private Map<String, EsPropertyVo> properties;
    }
}