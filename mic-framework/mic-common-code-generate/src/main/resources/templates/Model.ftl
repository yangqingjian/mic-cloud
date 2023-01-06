package ${domainPackage};

import cn.mic.cloud.freamework.common.core.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
/**
 * ${tableComment} 实体类
 *
 * @author : ${author}
 * @date : ${date}
 */
@Data
@NoArgsConstructor
@TableName(value = "${tableNameUpper}")
public class ${tableNameFirstUpper} extends BaseEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;
    <#list excludeBaseModelColumns as model>
	/**
     * ${model.columnComment!}
     */
    @ApiModelProperty(value = "${model.columnComment!?trim}", required = ${model.nullAble})
    @TableField(value = "${model.columnName?upper_case}")
	<#if model.columnName?upper_case = 'VERSION'>
	@com.baomidou.mybatisplus.annotation.Version
	</#if>
    <#if model.nullAble = 'true'>
    @<#if model.javaType = 'String'>NotEmpty<#else>NotNull</#if>(message = "【${model.columnComment!?trim}】不能为空")
    </#if>
    <#if model.columnType = 'DATE'>
    @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	@org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd")
    </#if>
    <#if model.columnType = 'TIMESTAMP' || model.columnType='DATETIME'>
    @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    </#if>
    <#if model.javaType = 'String'>
    private String ${model.changeColumnName?uncap_first};
    <#else>
    private ${model.javaType} ${model.changeColumnName?uncap_first};
    </#if>
    </#list>

}
