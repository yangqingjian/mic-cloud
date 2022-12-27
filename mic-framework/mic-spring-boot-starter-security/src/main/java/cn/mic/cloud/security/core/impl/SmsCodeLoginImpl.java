package cn.mic.cloud.security.core.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Method;
import cn.mic.cloud.framework.redis.comp.RedisKit;
import cn.mic.cloud.freamework.common.core.LoginUser;
import cn.mic.cloud.freamework.common.exception.AuthenticationException;
import cn.mic.cloud.freamework.common.exception.SystemException;
import cn.mic.cloud.security.config.SecurityCommonConfig;
import cn.mic.cloud.security.constants.LoginTypeEnum;
import cn.mic.cloud.security.core.HttpSecurityUtils;
import cn.mic.cloud.security.core.LoginInterface;
import cn.mic.cloud.security.vo.LoginRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static cn.mic.cloud.security.constants.SecurityConstants.CACHE_SMS_CODE;

/**
 * @author : YangQingJian
 * @date : 2022/12/27
 */
@Service
@Slf4j
public class SmsCodeLoginImpl implements LoginInterface {

    @Resource
    private  SecurityCommonConfig securityCommonConfig;

    @Resource
    private  HttpSecurityUtils httpSecurityUtils;

    @Resource
    private  RedisKit redisKit;

    /**
     * 判断是否当前支持处理
     *
     * @param loginRequest
     * @return
     */
    @Override
    public boolean support(LoginRequest loginRequest) {
        return ObjectUtil.equals(loginRequest.getLoginType(), LoginTypeEnum.SMS_CODE);
    }

    /**
     * 认证
     *
     * @param loginRequest
     * @return
     */
    @Override
    public LoginUser auth(LoginRequest loginRequest) {
        if (ObjectUtil.isNull(securityCommonConfig.getSelectByMobileUrl())) {
            throw new SystemException("手机号接口地址查询未配置");
        }
        String smsCode = redisKit.getStr(CACHE_SMS_CODE + loginRequest.getLoginName());
        if (StrUtil.isBlank(smsCode)) {
            throw new AuthenticationException("手机验证码不对或者手机验证码已超时");
        }
        if (!ObjectUtil.equals(loginRequest.getLoginSecret(), smsCode)) {
            throw new AuthenticationException("手机号或者验证码错误");
        }
        String remoteUrl = securityCommonConfig.getSelectByMobileUrl();
        remoteUrl = String.format(remoteUrl, loginRequest.getLoginName());
        LoginUser loginUser = httpSecurityUtils.remoteGetLoginUser(remoteUrl, Method.POST);
        if (ObjectUtil.isNull(loginUser)) {
            throw new AuthenticationException("用户名不存在");
        }
        return loginUser;
    }

}
