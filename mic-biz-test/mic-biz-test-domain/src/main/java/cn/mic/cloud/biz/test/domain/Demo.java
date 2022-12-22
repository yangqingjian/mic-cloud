package cn.mic.cloud.biz.test.domain;

import cn.mic.cloud.freamework.common.core.BaseEntity;
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
@TableName(value = "demo")
public class Demo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 姓名
     */
    @TableField(value = "name")
    private String name;


}
