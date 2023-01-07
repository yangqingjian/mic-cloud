package cn.mic.cloud.biz.caas.repository.code.impl;

import cn.mic.cloud.biz.caas.domain.code.CaasCodeSeriesNumber;
import cn.mic.cloud.biz.caas.mapper.code.CaasCodeSeriesNumberMapper;
import cn.mic.cloud.mybatis.plus.core.BaseEntityRepositoryImpl;
import org.springframework.stereotype.Repository;
import cn.mic.cloud.biz.caas.repository.code.CaasCodeSeriesNumberRepository;

/**
* 编码序列号  repository接口实现类
*
* @author : YangQingJian
* @date : 2023/1/7
*/
@Repository
public class CaasCodeSeriesNumberRepositoryImpl extends BaseEntityRepositoryImpl<CaasCodeSeriesNumberMapper,CaasCodeSeriesNumber> implements CaasCodeSeriesNumberRepository {


}
