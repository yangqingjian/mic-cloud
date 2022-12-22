package cn.mic.cloud.mybatis.plus.core;

import cn.hutool.core.date.DateUtil;
import cn.mic.cloud.mybatis.plus.anno.DateTimeSnowflakeFormatAnno;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * 自定义时间前缀加雪花算法
 *
 * @author yangqingjian
 * @date 2021/3/6
 */
public class DateTimeSnowflakeIdGenerator implements IdentifierGenerator {

    /**
     * 生成Id
     *
     * @param entity 实体
     * @return id
     */
    @Override
    public Number nextId(Object entity) {
        return new DefaultIdentifierGenerator().nextId(entity);
    }

    /**
     * String 类型id生成
     * 如果有时间格式化，主街上增加
     *
     * @param entity
     * @return
     */
    @Override
    public String nextUUID(Object entity) {
        String snowflakeString = String.valueOf(nextId(entity));
        if (entity.getClass().isAnnotationPresent(DateTimeSnowflakeFormatAnno.class)) {
            DateTimeSnowflakeFormatAnno dateTimeSnowflakeFormat = entity.getClass().getAnnotation(DateTimeSnowflakeFormatAnno.class);
            String format = dateTimeSnowflakeFormat.format();
            if (StringUtils.isBlank(format)) {
                format = DateTimeSnowflakeFormatAnno.DEFAULT_FORMAT_STRING;
            }
            String date = DateUtil.format(new Date(), format);
            snowflakeString = date + snowflakeString;
        }
        return snowflakeString;
    }

}
