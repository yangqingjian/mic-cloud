package cn.mic.cloud.biz.caas.web.controller.code;

import cn.mic.cloud.biz.caas.domain.code.CaasCodeRule;
import cn.mic.cloud.common.web.core.AbstractBaseEntityController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* 编码规则 web接口
*
* @author : YangQingJian
* @date : 2023/1/7
*/
@RestController
@RequestMapping("/caasCodeRule")
@Api(tags = "编码规则")
@Slf4j
public class CaasCodeRuleController extends AbstractBaseEntityController<CaasCodeRule> {


}
