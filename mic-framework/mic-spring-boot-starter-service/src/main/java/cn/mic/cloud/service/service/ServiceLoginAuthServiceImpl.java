package cn.mic.cloud.service.service;

import cn.mic.cloud.freamework.common.core.login.LoginAuthInterface;
import cn.mic.cloud.freamework.common.core.login.LoginRequest;
import cn.mic.cloud.freamework.common.core.login.LoginUser;
import cn.mic.cloud.freamework.common.vos.login.LoginSmsCodeSendRequest;
import cn.mic.cloud.service.feign.ServiceLoginUserFeign;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author : YangQingJian
 * @date : 2022/12/29
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ServiceLoginAuthServiceImpl implements LoginAuthInterface {

    private final ServiceLoginUserFeign serviceLoginUserFeign;

    /**
     * 根据用户名查询
     *
     * @param loginRequest
     * @return
     */
    @Override
    public LoginUser getLoginUser(LoginRequest loginRequest) {
        return serviceLoginUserFeign.getLoginUser(loginRequest);
    }

    /**
     * 发送验证码
     *
     * @param request
     */
    @Override
    public String sendSmsCode(LoginSmsCodeSendRequest request) {
        return serviceLoginUserFeign.sendSmsCode(request);
    }

    /**
     * 存储token，返回过期时间
     *
     * @param key
     * @param loginUser
     * @return
     */
    @Override
    public Date redisStoreToken(String key, LoginUser loginUser) {
        return serviceLoginUserFeign.redisStoreToken(key, loginUser);
    }

    /**
     * 查询token
     *
     * @param key
     * @return
     */
    @Override
    public LoginUser redisGetToken(String key) {
        return serviceLoginUserFeign.redisGetToken(key);
    }

    /**
     * 删除token
     *
     * @param key
     * @return
     */
    @Override
    public Boolean redisRemoveToken(String key) {
        return serviceLoginUserFeign.redisRemoveToken(key);
    }
}
