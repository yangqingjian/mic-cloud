package cn.mic.cloud.biz.test.service.service;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.mic.cloud.framework.redis.comp.RedisKit;
import cn.mic.cloud.freamework.common.core.login.SimpleAuthority;
import cn.mic.cloud.freamework.common.core.login.request.*;
import cn.mic.cloud.freamework.common.core.login.LoginAuthUser;
import cn.mic.cloud.freamework.common.core.login.response.LoginAuthSmsCodeSendResponse;
import cn.mic.cloud.freamework.common.core.login.response.LoginTokenRedisRemoveResponse;
import cn.mic.cloud.freamework.common.core.login.response.LoginTokenRedisStoreResponse;
import cn.mic.cloud.freamework.common.exception.AuthenticationException;
import cn.mic.cloud.freamework.common.exception.InvalidParameterException;
import cn.mic.cloud.freamework.common.utils.SecurityCoreUtils;
import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static cn.mic.cloud.biz.test.service.constants.MicBizTestServiceConstants.CACHE_SMS_CODE;
import static cn.mic.cloud.biz.test.service.constants.MicBizTestServiceConstants.CACHE_TOKEN_CODE;

/**
 * @author : YangQingJian
 * @date : 2022/12/28
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LoginUserServiceImpl implements LoginUserService {

    private final RedisKit redisKit;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * 根据用户名查询
     *
     * @param request
     * @return
     */
    @Override
    public LoginAuthUser getLoginUser(LoginAuthRequest request) {
        LoginAuthUser loginUser = getTempLoginUser(request);
        switch (request.getLoginType()) {
            case SMS_CODE:
                String cacheSmsCode = redisKit.getStr(CACHE_SMS_CODE + request.getLoginName());
                if (StrUtil.isBlank(cacheSmsCode)) {
                    throw new AuthenticationException("验证码错误或者已过期");
                }
                if (ObjectUtil.notEqual(cacheSmsCode, request.getLoginSecret())) {
                    throw new AuthenticationException("验证码错误或者已过期");
                }
                /**
                 * 认证成功之后要清楚验证码
                 */
                redisKit.remove(CACHE_SMS_CODE + request.getLoginName());
                break;
            case USERNAME_PASSWORD:
                /**
                 * 使用encoder.matches进行匹配
                 */
                if (!encoder.matches(request.getLoginSecret(), loginUser.getPassword())) {
                    throw new AuthenticationException("用户名或者密码错误");
                }
                break;
            default:
                throw new InvalidParameterException("登录类型[%s]未开发", request.getLoginType().getDesc());
        }
        return loginUser;
    }

    /**
     * 发送验证码
     *
     * @param request
     */
    @Override
    public LoginAuthSmsCodeSendResponse sendSmsCode(LoginSmsCodeSendRequest request) {
        if (ObjectUtil.notEqual(request.getMobile() , "13880981076")){
            throw new InvalidParameterException("手机号【%s】在系统中不存在" , request.getMobile());
        }
        LoginAuthSmsCodeSendResponse response = new LoginAuthSmsCodeSendResponse();
        Integer code = new Random().nextInt(100000);
        String cacheCode = String.format("%06d", code);
        log.info("mobile = {} , cacheCode = {}", request.getMobile(), cacheCode);
        redisKit.set(CACHE_SMS_CODE + request.getMobile(), cacheCode, request.getTimeout(), TimeUnit.SECONDS);
        response.setSmsCode(cacheCode);
        response.setTimeoutSeconds(request.getTimeout());
        return response;
    }

    /**
     * 存储token，返回过期时间
     *
     * @param request
     * @return
     */
    @Override
    public LoginTokenRedisStoreResponse redisStoreToken(LoginTokenRedisStoreRequest request) {
        String key = request.getKey();
        Assert.hasText(key, "key不能为空");
        key = SecurityCoreUtils.getTokenRedisKey(key);
        redisKit.set(CACHE_TOKEN_CODE + key, JSON.toJSONString(request.getLoginUser()), request.getExpireSeconds(), TimeUnit.SECONDS);
        DateTime expireDate = DateUtil.offset(new Date(), DateField.SECOND, request.getExpireSeconds());
        LoginTokenRedisStoreResponse response = new LoginTokenRedisStoreResponse();
        response.setExpireDate(expireDate);
        return response;
    }

    /**
     * 查询token
     *
     * @param request
     * @return
     */
    @Override
    public LoginAuthUser redisGetToken(LoginTokenRedisGetRequest request) {
        String key = request.getKey();
        Assert.hasText(key, "key不能为空");
        key = SecurityCoreUtils.getTokenRedisKey(key);
        return redisKit.getObj(CACHE_TOKEN_CODE + key, LoginAuthUser.class);
    }

    /**
     * 删除token
     *
     * @param request
     * @return
     */
    @Override
    public LoginTokenRedisRemoveResponse redisRemoveToken(LoginTokenRedisRemoveRequest request) {
        String key = request.getKey();
        Assert.hasText(key, "key不能为空");
        key = SecurityCoreUtils.getTokenRedisKey(key);
        redisKit.remove(CACHE_TOKEN_CODE + key);
        LoginTokenRedisRemoveResponse response = new LoginTokenRedisRemoveResponse();
        response.setFlag(true);
        return response;
    }

    private LoginAuthUser getTempLoginUser(LoginAuthRequest request) {
        LoginAuthUser loginUser = new LoginAuthUser();
        if (request.getLoginName().length() == 11) {
            //loginUser.setMobile(request.getLoginName());
            loginUser.setUsername("admin");
        } else {
            loginUser.setUsername(request.getLoginName());
            //loginUser.setMobile("13880981076");
        }
        //SHA-256+随机盐+密钥对密码进行加密。
        loginUser.setPassword(encoder.encode("123456"));
        loginUser.setDepartPositionId("123");
        SimpleAuthority adminRole = new SimpleAuthority("admin");
        SimpleAuthority clientRole = new SimpleAuthority("client");
        List<SimpleAuthority> authorities = Lists.newArrayList(adminRole, clientRole);
        loginUser.setAuthorities(authorities);
        return loginUser;
    }
}
