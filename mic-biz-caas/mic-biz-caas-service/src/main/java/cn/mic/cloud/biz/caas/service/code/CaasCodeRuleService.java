package cn.mic.cloud.biz.caas.service.code;

import cn.mic.cloud.biz.caas.api.code.CaasCodeRuleApi;
import cn.mic.cloud.biz.caas.domain.code.CaasCodeRule;
import cn.mic.cloud.mybatis.plus.core.BaseEntityService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;

/**
 * 编码规则 service接口
 *
 * @author : YangQingJian
 * @date : 2023/1/7
 */
@RestController
@RequestMapping("/caasCodeRule")
@Api(tags = "编码规则")
public interface CaasCodeRuleService extends BaseEntityService<CaasCodeRule> , CaasCodeRuleApi {



}
