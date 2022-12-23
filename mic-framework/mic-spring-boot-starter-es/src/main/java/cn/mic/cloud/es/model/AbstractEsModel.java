package cn.mic.cloud.es.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.core.suggest.Completion;

import java.io.Serializable;

/**
 * 搜索引擎基础表
 *
 * @author yangqingjian
 * @date 2020-11-21
 */
@ToString
public abstract class AbstractEsModel {

    /**
     * 主键ID,建议为String,因为最终也会转换成String
     */
    @Setter
    @Getter
    @Id
    @Field(type = FieldType.Keyword)
    private Serializable id;
    /**
     * 字段补全
     */
    @Getter
    @Setter
    @CompletionField
    private Completion suggest;

    /**
     * 字段补全建议
     */
    @Getter
    @Setter
    private String suggestedText;

    /**
     * 补全的方法
     */
    public abstract void buildSuggest();


}
