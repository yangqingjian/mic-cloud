package cn.mic.cloud.security.feign;

import cn.mic.cloud.freamework.common.core.login.LoginAuthInterface;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 注意配置
 *
 * @author : YangQingJian
 * @date : 2022/12/28
 */
@FeignClient(value = "${security.value:mic-biz-test-service}", path = "${security.path:/loginUser}", contextId = "security.contextId:defaultLoginUserFeign")
public interface DefaultLoginUserFeign extends LoginAuthInterface {

}
