package cn.mic.cloud.biz.caas.feign.test;

import cn.mic.cloud.biz.caas.api.test.DemoApi;
import cn.mic.cloud.biz.caas.domain.test.Demo;
import cn.mic.cloud.freamework.common.core.BaseEntityFeign;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 测试 接口抽象层类
 *
 * @author : YangQingJian
 * @date : 2023/1/6
 */
@FeignClient(value = "mic-biz-caas-service", path = "/demo" , contextId = "demoFeign")
public interface DemoFeign extends DemoApi, BaseEntityFeign<Demo> {

}