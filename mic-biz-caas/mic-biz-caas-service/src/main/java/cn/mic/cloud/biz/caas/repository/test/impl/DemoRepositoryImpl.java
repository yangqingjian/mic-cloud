package cn.mic.cloud.biz.caas.repository.test.impl;

import cn.mic.cloud.biz.caas.domain.test.Demo;
import cn.mic.cloud.biz.caas.mapper.test.DemoMapper;
import cn.mic.cloud.mybatis.plus.core.BaseEntityRepositoryImpl;
import org.springframework.stereotype.Repository;
import cn.mic.cloud.biz.caas.repository.test.DemoRepository;

/**
* 测试  repository接口实现类
*
* @author : YangQingJian
* @date : 2023/1/6
*/
@Repository
public class DemoRepositoryImpl extends BaseEntityRepositoryImpl<DemoMapper,Demo> implements DemoRepository {


}
