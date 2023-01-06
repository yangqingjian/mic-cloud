package cn.mic.cloud.code.generate.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : YangQingJian
 * @date : 2023/1/4
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TableClass {

    private String tableName;

    private String tableNameUpper;

    private String tableComment;

    private String tableNameLower;

    /**
     * 首字母大写驼峰命名
     */
    private String tableNameFirstUpper;
    /**
     * 表的驼峰命名
     */
    private String tableNameCame;

}
