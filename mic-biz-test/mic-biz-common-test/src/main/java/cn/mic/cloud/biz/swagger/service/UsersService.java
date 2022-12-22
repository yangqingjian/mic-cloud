package cn.mic.cloud.biz.swagger.service;

import cn.mic.cloud.biz.swagger.entity.Users;
import cn.mic.cloud.mybatis.plus.core.BaseEntityService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : YangQingJian
 * @date : 2022/12/22
 */
@RestController
@RequestMapping("/users")
public interface UsersService extends BaseEntityService<Users> {



}
