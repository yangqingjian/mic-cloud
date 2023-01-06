package cn.mic.cloud.biz.caas.web.controller.test;

import cn.mic.cloud.biz.caas.domain.test.Demo;
import cn.mic.cloud.common.web.core.AbstractBaseEntityController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* 测试 web接口
*
* @author : YangQingJian
* @date : 2023/1/6
*/
@RestController
@RequestMapping("/demo")
@Api(tags = "测试")
@Slf4j
public class DemoController extends AbstractBaseEntityController<Demo> {


}
