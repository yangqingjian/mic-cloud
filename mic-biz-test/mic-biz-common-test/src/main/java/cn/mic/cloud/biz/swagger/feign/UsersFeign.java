package cn.mic.cloud.biz.swagger.feign;

import cn.mic.cloud.biz.swagger.entity.Users;
import cn.mic.cloud.mybatis.plus.core.BaseEntityFeign;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author : YangQingJian
 * @date : 2022/12/22
 */
@FeignClient(name = "mic-biz-test" , path = "/users")
public interface UsersFeign extends BaseEntityFeign<Users> {


}
