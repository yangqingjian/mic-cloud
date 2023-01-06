package cn.mic.cloud.code.generate.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <B>系统名称：CODE-GENERATE系统</B><BR>
 * <B>模块名称：CODE-GENERATE</B><BR>
 * <B>中文类名：数据库封装字段</B><BR>
 * <B>概要说明：</B><BR>
 * <B>@version：v1.0</B><BR>
 * <B>版本		修改人		备注</B><BR>
 *
 * @author : Administrator
 * @date : 2018年5月12日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColumnClass {
    /**
     * 数据库字段名称
     **/
    private String columnName;
    /**
     * 数据库字段类型
     **/
    private String columnType;
    /**
     * 数据库字段首字母小写且去掉下划线字符串
     **/
    private String changeColumnName;
    /**
     * 数据库字段注释
     **/
    private String columnComment;
    /**
     * 长度
     */
    private Integer columnSize;
    /**
     * 是否必须，为true表示必填
     */
    private String nullAble = "false";
    /**
     * 小数点位数
     **/
    private Integer decimalDigits = 0;
    /**
     *  java对应的类型
     */
    private String javaType;

}