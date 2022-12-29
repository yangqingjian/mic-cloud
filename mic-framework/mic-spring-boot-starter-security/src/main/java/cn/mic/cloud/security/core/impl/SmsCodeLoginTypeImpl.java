package cn.mic.cloud.security.core.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.mic.cloud.freamework.common.constants.LoginTypeEnum;
import cn.mic.cloud.freamework.common.core.login.LoginAuthInterface;
import cn.mic.cloud.freamework.common.core.login.LoginRequest;
import cn.mic.cloud.freamework.common.core.login.LoginUser;
import cn.mic.cloud.freamework.common.exception.AuthenticationException;
import cn.mic.cloud.security.core.LoginTypeInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author : YangQingJian
 * @date : 2022/12/27
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SmsCodeLoginTypeImpl implements LoginTypeInterface {

    private final LoginAuthInterface loginAuthInterface;


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
        LoginUser loginUser = loginAuthInterface.getLoginUser(loginRequest);
        if (ObjectUtil.isNull(loginUser)) {
            throw new AuthenticationException("手机号或者验证码不存在");
        }
        return loginUser;
    }

}
