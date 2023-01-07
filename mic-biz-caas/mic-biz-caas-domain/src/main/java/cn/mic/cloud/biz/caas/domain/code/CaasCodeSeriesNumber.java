package cn.mic.cloud.biz.caas.domain.code;

import cn.mic.cloud.freamework.common.core.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
/**
 * 编码序列号 实体类
 *
 * @author : YangQingJian
 * @date : 2023/1/7
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName(value = "CAAS_CODE_SERIES_NUMBER")
public class CaasCodeSeriesNumber extends BaseEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;
	/**
     * 编码配置ID
     */
    @ApiModelProperty(value = "编码配置ID", required = false)
    @TableField(value = "CODE_RULE_ID")
    private String codeRuleId;
	/**
     * 格式化后的日期
     */
    @ApiModelProperty(value = "格式化后的日期", required = false)
    @TableField(value = "DATE_STR")
    private String dateStr;
	/**
     * 当前顺序号
     */
    @ApiModelProperty(value = "当前顺序号", required = false)
    @TableField(value = "SEQ_NUMBER")
    private Long seqNumber;
	/**
     * 业务变量
     */
    @ApiModelProperty(value = "业务变量", required = false)
    @TableField(value = "BIZ_KEY")
    private String bizKey;

}
