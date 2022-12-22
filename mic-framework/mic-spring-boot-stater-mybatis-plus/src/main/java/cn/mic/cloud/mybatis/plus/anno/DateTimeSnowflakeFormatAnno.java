package cn.mic.cloud.mybatis.plus.anno;

import java.lang.annotation.*;

/**
 * 验证用户是否包含资源权限
 * 使用方式：ID字段上面的注解需要如下使用
 * @TableId(type = IdType.ASSIGN_UUID)
 * 然后在类上面使用此注解
 *
 * @author yangqingjian
 * @date 2021-03-06
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DateTimeSnowflakeFormatAnno {

    /**
     * 默认格式
     */
    String DEFAULT_FORMAT_STRING = "yyyy";

    /**
     * 日期格式化,默认按照年来拆分
     *
     * @return
     */
    String format() default DEFAULT_FORMAT_STRING;
}
