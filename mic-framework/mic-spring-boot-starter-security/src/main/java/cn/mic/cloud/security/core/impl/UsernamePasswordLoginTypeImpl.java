package cn.mic.cloud.security.core.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.mic.cloud.freamework.common.constants.LoginTypeEnum;
import cn.mic.cloud.freamework.common.core.login.LoginAuthInterface;
import cn.mic.cloud.freamework.common.core.login.request.LoginAuthRequest;
import cn.mic.cloud.freamework.common.core.login.LoginAuthUser;
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
public class UsernamePasswordLoginTypeImpl implements LoginTypeInterface {

    private final LoginAuthInterface loginAuthInterface;

    /**
     * 判断是否当前支持处理
     *
     * @param loginAuthRequest
     * @return
     */
    @Override
    public boolean support(LoginAuthRequest loginAuthRequest) {
        return ObjectUtil.equals(loginAuthRequest.getLoginType()  , LoginTypeEnum.USERNAME_PASSWORD);
    }

    /**
     * 认证
     *
     * @param loginAuthRequest
     * @return
     */
    @Override
    public LoginAuthUser auth(LoginAuthRequest loginAuthRequest) {
        LoginAuthUser loginUser = loginAuthInterface.getLoginUser(loginAuthRequest);
        if (ObjectUtil.isNull(loginUser)){
            throw new AuthenticationException("用户名不存在");
        }
        return loginUser;
    }

}
