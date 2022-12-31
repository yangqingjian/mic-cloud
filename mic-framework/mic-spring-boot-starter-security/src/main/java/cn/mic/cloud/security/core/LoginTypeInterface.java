package cn.mic.cloud.security.core;

import cn.mic.cloud.freamework.common.core.login.LoginAuthUser;
import cn.mic.cloud.freamework.common.core.login.request.LoginAuthRequest;

/**
 * @author : YangQingJian
 * @date : 2022/12/27
 */
public interface LoginTypeInterface {

    /**
     * 判断是否当前支持处理
     *
     * @param loginAuthRequest
     * @return
     */
    boolean support(LoginAuthRequest loginAuthRequest);

    /**
     * 认证
     *
     * @param loginAuthRequest
     * @return
     */
    LoginAuthUser auth(LoginAuthRequest loginAuthRequest);

}
