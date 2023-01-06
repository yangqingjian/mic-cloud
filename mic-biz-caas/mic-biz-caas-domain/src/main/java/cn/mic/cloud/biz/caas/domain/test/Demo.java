package cn.mic.cloud.biz.caas.domain.test;

import cn.mic.cloud.freamework.common.core.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
/**
 * 测试 实体类
 *
 * @author : YangQingJian
 * @date : 2023/1/6
 */
@Data
@NoArgsConstructor
@TableName(value = "DEMO")
public class Demo extends BaseEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;
	/**
     * 名称
     */
    @ApiModelProperty(value = "名称", required = false)
    @TableField(value = "NAME")
    private String name;
	/**
     * 版本
     */
    @ApiModelProperty(value = "版本", required = false)
    @TableField(value = "VERSION")
	@com.baomidou.mybatisplus.annotation.Version
    private Integer version;
	/**
     * 生日
     */
    @ApiModelProperty(value = "生日", required = false)
    @TableField(value = "BIRTHDAY")
    @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date birthday;
	/**
     * 使用日期
     */
    @ApiModelProperty(value = "使用日期", required = false)
    @TableField(value = "USE_DATE")
    @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	@org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date useDate;

}
