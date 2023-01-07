package cn.mic.cloud.biz.caas.service.basic;

import cn.mic.cloud.biz.caas.api.basic.CaasRegionApi;
import cn.mic.cloud.biz.caas.domain.basic.CaasRegion;
import cn.mic.cloud.mybatis.plus.core.BaseEntityService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;

/**
 * 行政区域 service接口
 *
 * @author : YangQingJian
 * @date : 2023/1/7
 */
@RestController
@RequestMapping("/caasRegion")
@Api(tags = "行政区域")
public interface CaasRegionService extends BaseEntityService<CaasRegion> , CaasRegionApi {



}
