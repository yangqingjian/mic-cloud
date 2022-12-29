package cn.mic.cloud.biz.test.service.service;

import cn.mic.cloud.biz.test.api.LoginUserApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : YangQingJian
 * @date : 2022/12/28
 */
@RestController
@RequestMapping("/loginUser")
public interface LoginUserService extends LoginUserApi {



}
