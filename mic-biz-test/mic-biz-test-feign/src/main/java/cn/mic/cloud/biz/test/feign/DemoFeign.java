package cn.mic.cloud.biz.test.feign;

import cn.mic.cloud.biz.test.api.DemoApi;
import cn.mic.cloud.biz.test.domain.Demo;
import cn.mic.cloud.freamework.common.core.BaseEntityFeign;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author : YangQingJian
 * @date : 2022/12/22
 */
@FeignClient(value = "mic-biz-test-service", path = "/demo" , contextId = "demoFeign")
public interface DemoFeign extends DemoApi, BaseEntityFeign<Demo> {

}
