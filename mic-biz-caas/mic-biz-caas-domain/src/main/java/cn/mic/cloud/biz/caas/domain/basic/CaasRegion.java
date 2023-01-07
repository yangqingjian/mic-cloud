package cn.mic.cloud.biz.caas.domain.basic;

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
 * 行政区域 实体类
 *
 * @author : YangQingJian
 * @date : 2023/1/7
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName(value = "CAAS_REGION")
public class CaasRegion extends BaseEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 编码
     */
    @ApiModelProperty(value = "编码", required = false)
    @TableField(value = "REGION_CODE")
    private String regionCode;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称", required = false)
    @TableField(value = "REGION_NAME")
    private String regionName;
    /**
     * 父区域编码
     */
    @ApiModelProperty(value = "父区域编码", required = false)
    @TableField(value = "PARENT_CODE")
    private String parentCode;

}
