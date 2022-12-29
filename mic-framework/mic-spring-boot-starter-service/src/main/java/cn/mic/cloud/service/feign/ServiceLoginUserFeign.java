package cn.mic.cloud.service.feign;

import cn.mic.cloud.freamework.common.core.login.LoginAuthInterface;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 注意配置
 *
 * @author : YangQingJian
 * @date : 2022/12/28
 */
@FeignClient(value = "${security.value:mic-biz-test-service}", path = "${security.path:/loginUser}")
public interface ServiceLoginUserFeign extends LoginAuthInterface {

}
