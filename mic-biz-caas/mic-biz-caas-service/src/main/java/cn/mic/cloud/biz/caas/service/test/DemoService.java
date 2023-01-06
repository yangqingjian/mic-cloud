package cn.mic.cloud.biz.caas.service.test;

import cn.mic.cloud.biz.caas.api.test.DemoApi;
import cn.mic.cloud.biz.caas.domain.test.Demo;
import cn.mic.cloud.mybatis.plus.core.BaseEntityService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;

/**
 * 测试 service接口
 *
 * @author : YangQingJian
 * @date : 2023/1/6
 */
@RestController
@RequestMapping("/demo")
@Api(tags = "测试")
public interface DemoService extends BaseEntityService<Demo> , DemoApi {



}
