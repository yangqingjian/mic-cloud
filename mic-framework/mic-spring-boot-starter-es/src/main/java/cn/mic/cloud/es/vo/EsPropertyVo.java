package cn.mic.cloud.es.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author yangqingjian
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EsPropertyVo implements Serializable {
    /**
     * 类型
     * text、keyword、date、long、integer、short、byte、double、float、half_float、scaled_float、boolean、ip
     * object、nested
     * geo_point、geo_shape、completion
     */
    private String type;
    /**
     * 是否创建索引
     */
    private Boolean index = true;
    /**
     * 分析器
     * ik_max_word
     */
    private String analyzer;
    /**
     * 分词器
     * ik_max_word
     */
    private String searchAnalyzer;
}