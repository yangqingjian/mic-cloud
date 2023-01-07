package cn.mic.cloud.biz.caas.feign.code;

import cn.mic.cloud.biz.caas.api.code.CaasCodeRuleApi;
import cn.mic.cloud.biz.caas.domain.code.CaasCodeRule;
import cn.mic.cloud.freamework.common.core.BaseEntityFeign;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 编码规则 接口抽象层类
 *
 * @author : YangQingJian
 * @date : 2023/1/7
 */
@FeignClient(value = "mic-biz-caas-service", path = "/caasCodeRule" , contextId = "caasCodeRuleFeign")
public interface CaasCodeRuleFeign extends CaasCodeRuleApi, BaseEntityFeign<CaasCodeRule> {

}