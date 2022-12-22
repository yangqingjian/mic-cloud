package cn.mic.cloud.biz.test.service.service;


import cn.mic.cloud.biz.test.api.DemoApi;
import cn.mic.cloud.biz.test.domain.Demo;
import cn.mic.cloud.mybatis.plus.core.BaseEntityService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : YangQingJian
 * @date : 2022/12/22
 */
@RestController
@RequestMapping("/demo")
public interface DemoService extends BaseEntityService<Demo> , DemoApi {



}
