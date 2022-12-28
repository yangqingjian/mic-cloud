package cn.mic.cloud.security.core.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.Method;
import cn.mic.cloud.freamework.common.core.login.LoginUser;
import cn.mic.cloud.freamework.common.exception.AuthenticationException;
import cn.mic.cloud.freamework.common.exception.SystemException;
import cn.mic.cloud.security.config.SecurityCommonConfig;
import cn.mic.cloud.freamework.common.constants.LoginTypeEnum;
import cn.mic.cloud.security.core.HttpSecurityUtils;
import cn.mic.cloud.security.core.LoginInterface;
import cn.mic.cloud.freamework.common.core.login.LoginRequest;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author : YangQingJian
 * @date : 2022/12/27
 */
@Service
@Slf4j
public class UsernamePasswordLoginImpl implements LoginInterface {
    @Resource
    private  SecurityCommonConfig securityCommonConfig;

    @Resource
    private  HttpSecurityUtils httpSecurityUtils;

    /**
     * 判断是否当前支持处理
     *
     * @param loginRequest
     * @return
     */
    @Override
    public boolean support(LoginRequest loginRequest) {
        return ObjectUtil.equals(loginRequest.getLoginType()  , LoginTypeEnum.USERNAME_PASSWORD);
    }

    /**
     * 认证
     *
     * @param loginRequest
     * @return
     */
    @Override
    public LoginUser auth(LoginRequest loginRequest) {
        if (ObjectUtil.isNull(securityCommonConfig.getGetLoginUserForUsernameUrl())) {
            throw new SystemException("用户名查询接口地址查询未配置");
        }
        String remoteUrl = securityCommonConfig.getGetLoginUserForUsernameUrl();
        LoginUser loginUser =  httpSecurityUtils.getRemoteObject(remoteUrl, Method.POST , loginRequest,LoginUser.class);
        if (ObjectUtil.isNull(loginUser)){
            throw new AuthenticationException("用户名不存在");
        }
        return loginUser;
    }

}
