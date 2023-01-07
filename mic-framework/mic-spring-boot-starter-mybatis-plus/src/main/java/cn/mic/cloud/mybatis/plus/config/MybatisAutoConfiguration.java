package cn.mic.cloud.mybatis.plus.config;

import cn.mic.cloud.mybatis.plus.core.DateTimeSnowflakeIdGenerator;
import cn.mic.cloud.mybatis.plus.core.DefaultMetaObjectHandler;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.IllegalSQLInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * mybatis配置文件
 *
 * @author yangqingjian
 * @date 2020-10-01
 */
@Configuration
@MapperScan(basePackages = {"${mybatis-plus.mapper-scanner}"})
public class MybatisAutoConfiguration {

    /**
     * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题(该属性会在旧插件移除后一同移除)
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        /**
         * 分页插件
         */
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        /**
         * 乐观锁
         */
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        /**
         * 防全表更新与删除插件
         */
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        /**
         * 性能插件
         */
        //interceptor.addInnerInterceptor(new IllegalSQLInnerInterceptor());
        return interceptor;
    }

    /**
     * metaObject插件
     *
     * @return
     */
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new DefaultMetaObjectHandler();
    }

    /**
     * 雪花算法的ID插件
     *
     * @return
     */
    @Bean
    @Primary
    public IdentifierGenerator identifierGenerator() {
        return new DateTimeSnowflakeIdGenerator();
    }

}
