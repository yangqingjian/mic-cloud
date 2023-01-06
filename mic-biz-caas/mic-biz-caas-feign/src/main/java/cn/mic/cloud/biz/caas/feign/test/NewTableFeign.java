package cn.mic.cloud.biz.caas.feign.test;

import cn.mic.cloud.biz.caas.api.test.NewTableApi;
import cn.mic.cloud.biz.caas.domain.test.NewTable;
import cn.mic.cloud.freamework.common.core.BaseEntityFeign;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 测试 接口抽象层类
 *
 * @author : YangQingJian
 * @date : 2023/1/6
 */
@FeignClient(value = "mic-biz-caas-service", path = "/newTable" , contextId = "newTableFeign")
public interface NewTableFeign extends NewTableApi, BaseEntityFeign<NewTable> {

}