package cn.mic.cloud.biz.caas.service.code;

import cn.mic.cloud.biz.caas.api.code.CaasCodeSeriesNumberApi;
import cn.mic.cloud.biz.caas.domain.code.CaasCodeSeriesNumber;
import cn.mic.cloud.mybatis.plus.core.BaseEntityService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;

/**
 * 编码序列号 service接口
 *
 * @author : YangQingJian
 * @date : 2023/1/7
 */
@RestController
@RequestMapping("/caasCodeSeriesNumber")
@Api(tags = "编码序列号")
public interface CaasCodeSeriesNumberService extends BaseEntityService<CaasCodeSeriesNumber> , CaasCodeSeriesNumberApi {



}
