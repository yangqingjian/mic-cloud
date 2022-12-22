package cn.mic.cloud.biz.swagger.entity;

import cn.mic.cloud.mybatis.plus.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author : YangQingJian
 * @date : 2022/12/22
 */
@Data
@NoArgsConstructor
@TableName(value = "Users")
public class Users extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 姓名
     */
    @TableField(value = "name")
    private String name;


}
