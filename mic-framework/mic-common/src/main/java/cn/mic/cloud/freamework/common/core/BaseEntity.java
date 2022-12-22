package cn.mic.cloud.freamework.common.core;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.io.Serializable;
import java.util.Date;

/**
 *
 */
@ToString
@Data
@SuperBuilder
@NoArgsConstructor
public abstract class BaseEntity<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 创建日期对应的数据库列的名称
     */
    public static final String CREATED_TIME_COLUMN = "created_time";

    public static final String CREATED_TIME = "createdTime";
    public static final String LAST_UPDATED_TIME = "lastUpdatedTime";
    public static final String MODIFIED_BY = "modifiedBy";
    public static final String CREATED_BY = "createdBy";
    public static final String IS_DELETED = "isDeleted";
    public static final String VERSION = "version";

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键ID")
    @NotNull(message = "【id】不能为空", groups = UpdateID.class)
    protected T id;

    /**
     * 版本（乐观锁）
     */
    @Version
    @ApiModelProperty(value = "版本")
    @TableField(value = "version")
    protected Integer version;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间【后台】", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    protected Date createdTime;

    /**
     * 最后修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "最后修改时间【后台】", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected Date lastUpdatedTime;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人【后台】", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    protected String createdBy;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人【后台】", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    @TableField(fill = FieldFill.UPDATE)
    protected String modifiedBy;

    /**
     * 是否删除
     */
    @TableLogic
    @TableField(value = "IS_DELETED", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "是否删除【后台】", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    protected Integer isDeleted;

    /**
     * 创建人姓名
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "创建人姓名", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    protected String createdByName;
    /**
     * 修改人姓名
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "修改人姓名", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    protected String modifiedByName;
    /**
     * 排序字段
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "排序字段")
    protected String sortFieldName;
    /**
     * 排序字段的方向
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "是否升序")
    protected Boolean sortFieldAscFlag;

    /**
     * 保存和缺省验证组（service验证）
     */
    public interface ServiceSave extends Default {

    }

    /**
     * 保存和缺省验证组（service验证）
     */
    public interface UpdateID extends Default {

    }

    /**
     * 更新和缺省验证组（service验证）
     */
    public interface ServiceUpdate extends Default {

    }

    /**
     * controller层面校验的验证
     */
    public interface ControllerSave extends Default {

    }

    /**
     * controller层面校验的验证
     */
    public interface ControllerUpdate extends Default {

    }

    /**
     * 填充
     *
     * @param operator
     */
    public void fillOperationInfo(String operator) {
        Date now = new Date();
        if (getId() == null) {
            this.createdBy = operator;
            this.createdTime = now;
        } else {
            this.modifiedBy = operator;
        }
        if (this.isDeleted == null) {
            this.isDeleted = 0;
        }
        this.lastUpdatedTime = now;
    }
}
