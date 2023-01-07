package cn.mic.cloud.biz.caas.web.controller.basic;

import cn.mic.cloud.biz.caas.domain.basic.CaasRegion;
import cn.mic.cloud.common.web.core.AbstractBaseEntityController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* 行政区域 web接口
*
* @author : YangQingJian
* @date : 2023/1/7
*/
@RestController
@RequestMapping("/caasRegion")
@Api(tags = "行政区域")
@Slf4j
public class CaasRegionController extends AbstractBaseEntityController<CaasRegion> {


}
