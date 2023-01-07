package cn.mic.cloud.biz.caas.repository.basic.impl;

import cn.mic.cloud.biz.caas.domain.basic.CaasRegion;
import cn.mic.cloud.biz.caas.mapper.basic.CaasRegionMapper;
import cn.mic.cloud.mybatis.plus.core.BaseEntityRepositoryImpl;
import org.springframework.stereotype.Repository;
import cn.mic.cloud.biz.caas.repository.basic.CaasRegionRepository;

/**
* 行政区域  repository接口实现类
*
* @author : YangQingJian
* @date : 2023/1/7
*/
@Repository
public class CaasRegionRepositoryImpl extends BaseEntityRepositoryImpl<CaasRegionMapper,CaasRegion> implements CaasRegionRepository {


}
