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
 * 编码规则 实体类
 *
 * @author : YangQingJian
 * @date : 2023/1/7
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName(value = "CAAS_CODE_RULE")
public class CaasCodeRule extends BaseEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;
	/**
     * 编码
     */
    @ApiModelProperty(value = "编码", required = false)
    @TableField(value = "CODE")
    private String code;
	/**
     * 日期格式（YYYYMMDD，YYMMDD，YYYYMM,YYMM等）如果没有就不填写
     */
    @ApiModelProperty(value = "日期格式（YYYYMMDD，YYMMDD，YYYYMM,YYMM等）如果没有就不填写", required = false)
    @TableField(value = "DATE_FORMAT")
    private String dateFormat;
	/**
     * 编码前缀
     */
    @ApiModelProperty(value = "编码前缀", required = false)
    @TableField(value = "PREFIX")
    private String prefix;
	/**
     * 顺序号的长度
     */
    @ApiModelProperty(value = "顺序号的长度", required = false)
    @TableField(value = "SEQ_LENGTH")
    private Integer seqLength;
	/**
     * 编码的总长度
     */
    @ApiModelProperty(value = "编码的总长度", required = false)
    @TableField(value = "LENGTH")
    private Integer length;
	/**
     * 步长
     */
    @ApiModelProperty(value = "步长", required = false)
    @TableField(value = "STEP_LENGTH")
    private Integer stepLength;
	/**
     * 开始序号
     */
    @ApiModelProperty(value = "开始序号", required = false)
    @TableField(value = "START_NUMBER")
    private Long startNumber;
	/**
     * 周期(年/月/日)
     */
    @ApiModelProperty(value = "周期(年/月/日)", required = false)
    @TableField(value = "PERIOD")
    private String period;
	/**
     * 备注
     */
    @ApiModelProperty(value = "备注", required = false)
    @TableField(value = "REMARK")
    private String remark;
	/**
     * BIZKEY在DATE的前面（默认为否）
     */
    @ApiModelProperty(value = "BIZKEY在DATE的前面（默认为否）", required = false)
    @TableField(value = "BIZKEY_FRONT_DATE_FLAG")
    private Boolean bizkeyFrontDateFlag;

}
