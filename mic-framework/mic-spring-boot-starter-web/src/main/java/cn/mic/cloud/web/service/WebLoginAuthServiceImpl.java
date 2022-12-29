package cn.mic.cloud.web.service;

import cn.mic.cloud.freamework.common.core.login.LoginAuthInterface;
import cn.mic.cloud.freamework.common.core.login.LoginRequest;
import cn.mic.cloud.freamework.common.core.login.LoginUser;
import cn.mic.cloud.freamework.common.vos.login.LoginSmsCodeSendRequest;
import cn.mic.cloud.web.feign.WebLoginUserFeign;
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
public class WebLoginAuthServiceImpl implements LoginAuthInterface {

    private final WebLoginUserFeign webLoginUserFeign;

    /**
     * 根据用户名查询
     *
     * @param loginRequest
     * @return
     */
    @Override
    public LoginUser getLoginUser(LoginRequest loginRequest) {
        return webLoginUserFeign.getLoginUser(loginRequest);
    }

    /**
     * 发送验证码
     *
     * @param request
     */
    @Override
    public String sendSmsCode(LoginSmsCodeSendRequest request) {
        return webLoginUserFeign.sendSmsCode(request);
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
        return webLoginUserFeign.redisStoreToken(key, loginUser);
    }

    /**
     * 查询token
     *
     * @param key
     * @return
     */
    @Override
    public LoginUser redisGetToken(String key) {
        return webLoginUserFeign.redisGetToken(key);
    }

    /**
     * 删除token
     *
     * @param key
     * @return
     */
    @Override
    public Boolean redisRemoveToken(String key) {
        return webLoginUserFeign.redisRemoveToken(key);
    }
}
