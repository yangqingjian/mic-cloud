package cn.mic.cloud.biz.test.web.controller;

import cn.mic.cloud.biz.test.domain.Demo;
import cn.mic.cloud.common.web.core.AbstractBaseEntityController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : YangQingJian
 * @date : 2022/12/22
 */
@RestController
@RequestMapping("/demo")
@Api(tags = "xxx")
@Slf4j
public class Demo1Controller extends AbstractBaseEntityController<Demo> {


}
