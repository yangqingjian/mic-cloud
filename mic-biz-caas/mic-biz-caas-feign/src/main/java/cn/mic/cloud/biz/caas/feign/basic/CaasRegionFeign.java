package cn.mic.cloud.biz.caas.feign.basic;

import cn.mic.cloud.biz.caas.api.basic.CaasRegionApi;
import cn.mic.cloud.biz.caas.domain.basic.CaasRegion;
import cn.mic.cloud.freamework.common.core.BaseEntityFeign;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 行政区域 接口抽象层类
 *
 * @author : YangQingJian
 * @date : 2023/1/7
 */
@FeignClient(value = "mic-biz-caas-service", path = "/caasRegion" , contextId = "caasRegionFeign")
public interface CaasRegionFeign extends CaasRegionApi, BaseEntityFeign<CaasRegion> {

}