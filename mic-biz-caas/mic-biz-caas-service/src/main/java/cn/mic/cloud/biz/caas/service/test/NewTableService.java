package cn.mic.cloud.biz.caas.service.test;

import cn.mic.cloud.biz.caas.api.test.NewTableApi;
import cn.mic.cloud.biz.caas.domain.test.NewTable;
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
@RequestMapping("/newTable")
@Api(tags = "测试")
public interface NewTableService extends BaseEntityService<NewTable> , NewTableApi {



}
