package cn.mic.cloud.mybatis.plus.core;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 数据仓库基类
 */
public abstract class BaseEntityRepositoryImpl<M extends BaseMapper<T> , T> extends ServiceImpl<M, T> implements BaseEntityRepository<T> {

}
