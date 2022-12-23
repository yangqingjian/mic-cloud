package cn.mic.cloud.mybatis.plus.core;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 数据仓库基类
 */
public abstract class BaseEntityRepositoryImpl<T> extends ServiceImpl<BaseMapper<T>, T> implements BaseEntityRepository<T> {

    /**
     * 重写
     *
     * @return
     */
    //@Override
    //@SuppressWarnings("unchecked")
    //protected Class<T> currentModelClass() {
    //    return (Class<T>) ReflectionKit.getSuperClassGenericType(getClass(), BaseEntity.class, 0);
    //}

}
