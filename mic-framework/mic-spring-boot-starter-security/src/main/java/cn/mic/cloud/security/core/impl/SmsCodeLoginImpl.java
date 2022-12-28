package cn.mic.cloud.security.core.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.Method;
import cn.mic.cloud.framework.redis.comp.RedisKit;
import cn.mic.cloud.freamework.common.core.login.LoginUser;
import cn.mic.cloud.freamework.common.exception.AuthenticationException;
import cn.mic.cloud.freamework.common.exception.SystemException;
import cn.mic.cloud.security.config.SecurityCommonConfig;
import cn.mic.cloud.freamework.common.constants.LoginTypeEnum;
import cn.mic.cloud.security.core.HttpSecurityUtils;
import cn.mic.cloud.security.core.LoginInterface;
import cn.mic.cloud.freamework.common.core.login.LoginRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author : YangQingJian
 * @date : 2022/12/27
 */
@Service
@Slf4j
public class SmsCodeLoginImpl implements LoginInterface {

    @Resource
    private SecurityCommonConfig securityCommonConfig;

    @Resource
    private HttpSecurityUtils httpSecurityUtils;


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
        if (ObjectUtil.isNull(securityCommonConfig.getGetLoginUserForSmsUrl())) {
            throw new SystemException("手机号接口地址查询未配置");
        }
        String remoteUrl = securityCommonConfig.getGetLoginUserForSmsUrl();
        LoginUser loginUser = httpSecurityUtils.getRemoteObject(remoteUrl, Method.POST, loginRequest, LoginUser.class);
        if (ObjectUtil.isNull(loginUser)) {
            throw new AuthenticationException("手机号或者验证码不存在");
        }
        return loginUser;
    }

}
