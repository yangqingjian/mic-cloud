package cn.mic.cloud.biz.caas.feign.code;

import cn.mic.cloud.biz.caas.api.code.CaasCodeSeriesNumberApi;
import cn.mic.cloud.biz.caas.domain.code.CaasCodeSeriesNumber;
import cn.mic.cloud.freamework.common.core.BaseEntityFeign;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 编码序列号 接口抽象层类
 *
 * @author : YangQingJian
 * @date : 2023/1/7
 */
@FeignClient(value = "mic-biz-caas-service", path = "/caasCodeSeriesNumber" , contextId = "caasCodeSeriesNumberFeign")
public interface CaasCodeSeriesNumberFeign extends CaasCodeSeriesNumberApi, BaseEntityFeign<CaasCodeSeriesNumber> {

}