package cn.mic.cloud.biz.swagger.controller;

import cn.mic.cloud.biz.swagger.entity.Users;
import cn.mic.cloud.biz.swagger.feign.UsersFeign;
import cn.mic.cloud.biz.swagger.service.UsersService;
import cn.mic.cloud.mybatis.plus.core.AbstractBaseEntityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : YangQingJian
 * @date : 2022/12/22
 */
@RestController
@RequestMapping("/controller/users")
public class UsersController extends AbstractBaseEntityController<UsersService , Users> {

    public UsersController(@Autowired UsersFeign usersFeign) {
        super(usersFeign);
    }

}
