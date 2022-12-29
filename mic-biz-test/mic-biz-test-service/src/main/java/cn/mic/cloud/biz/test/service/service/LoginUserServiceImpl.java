package cn.mic.cloud.biz.test.service.service;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.mic.cloud.framework.redis.comp.RedisKit;
import cn.mic.cloud.freamework.common.core.login.LoginRequest;
import cn.mic.cloud.freamework.common.core.login.LoginUser;
import cn.mic.cloud.freamework.common.core.login.SimpleAuthority;
import cn.mic.cloud.freamework.common.exception.AuthenticationException;
import cn.mic.cloud.freamework.common.exception.InvalidParameterException;
import cn.mic.cloud.freamework.common.vos.login.LoginSmsCodeSendRequest;
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
import static cn.mic.cloud.biz.test.service.constants.MicBizTestServiceConstants.CACHE_TOKEN_HOURS;

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
     * @param loginRequest
     * @return
     */
    @Override
    public LoginUser getLoginUser(LoginRequest loginRequest) {
        LoginUser loginUser = getTempLoginUser(loginRequest);
        switch (loginRequest.getLoginType()) {
            case SMS_CODE:
                String cacheSmsCode = redisKit.getStr(CACHE_SMS_CODE + loginRequest.getLoginName());
                if (StrUtil.isBlank(cacheSmsCode)) {
                    throw new AuthenticationException("验证码错误或者已过期");
                }
                if (ObjectUtil.notEqual(cacheSmsCode, loginRequest.getLoginSecret())) {
                    throw new AuthenticationException("验证码错误或者已过期");
                }
                break;
            case USERNAME_PASSWORD:
                /**
                 * 使用encoder.matches进行匹配
                 */
                if (!encoder.matches(loginRequest.getLoginSecret(), loginUser.getPassword())) {
                    throw new AuthenticationException("用户名或者密码错误");
                }
                break;
            default:
                throw new InvalidParameterException("登录类型[%s]未开发", loginRequest.getLoginType().getDesc());
        }
        return loginUser;
    }

    /**
     * 发送验证码
     *
     * @param request
     */
    @Override
    public String sendSmsCode(LoginSmsCodeSendRequest request) {
        Integer code = new Random().nextInt(100000);
        String cacheCode = String.format("%06d", code);
        log.info("mobile = {} , cacheCode = {}", request.getMobile(), cacheCode);
        redisKit.set(CACHE_SMS_CODE + request.getMobile(), cacheCode, request.getTimeout(), TimeUnit.SECONDS);
        return cacheCode;
    }

    /**
     * 存储token，返回过期时间
     *
     * @param loginUser
     * @return
     */
    @Override
    public Date redisStoreToken(String key, LoginUser loginUser) {
        Assert.hasText(key, "key不能为空");
        redisKit.set(key, JSON.toJSONString(loginUser), CACHE_TOKEN_HOURS, TimeUnit.HOURS);
        DateTime expireDate = DateUtil.offset(new Date(), DateField.HOUR, CACHE_TOKEN_HOURS);
        return expireDate;
    }

    /**
     * 查询token
     *
     * @param key
     * @return
     */
    @Override
    public LoginUser redisGetToken(String key) {
        Assert.hasText(key, "key不能为空");
        return redisKit.getObj(key, LoginUser.class);
    }

    /**
     * 删除token
     *
     * @param key
     * @return
     */
    @Override
    public Boolean redisRemoveToken(String key) {
        Assert.hasText(key, "key不能为空");
        redisKit.remove(key);
        return true;
    }

    private LoginUser getTempLoginUser(LoginRequest request) {
        LoginUser loginUser = new LoginUser();
        if (request.getLoginName().length() == 11) {
            loginUser.setMobile(request.getLoginName());
            loginUser.setUsername("admin");
        } else {
            loginUser.setUsername(request.getLoginName());
            loginUser.setMobile("13880981076");
        }

        //SHA-256+随机盐+密钥对密码进行加密。
        loginUser.setPassword(encoder.encode("123456"));
        loginUser.setCurrentDepartPosition("123");
        SimpleAuthority adminRole = new SimpleAuthority("admin");
        SimpleAuthority clientRole = new SimpleAuthority("client");
        List<SimpleAuthority> authorities = Lists.newArrayList(adminRole, clientRole);
        loginUser.setAuthorities(authorities);
        return loginUser;
    }
}
