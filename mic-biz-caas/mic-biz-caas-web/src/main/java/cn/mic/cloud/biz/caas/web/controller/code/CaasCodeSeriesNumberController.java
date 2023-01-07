package cn.mic.cloud.biz.caas.web.controller.code;

import cn.mic.cloud.biz.caas.domain.code.CaasCodeSeriesNumber;
import cn.mic.cloud.common.web.core.AbstractBaseEntityController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* 编码序列号 web接口
*
* @author : YangQingJian
* @date : 2023/1/7
*/
@RestController
@RequestMapping("/caasCodeSeriesNumber")
@Api(tags = "编码序列号")
@Slf4j
public class CaasCodeSeriesNumberController extends AbstractBaseEntityController<CaasCodeSeriesNumber> {


}
