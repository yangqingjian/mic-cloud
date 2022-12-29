package cn.mic.cloud.security.core;

import cn.mic.cloud.freamework.common.core.login.LoginUser;
import cn.mic.cloud.freamework.common.core.login.LoginRequest;

/**
 * @author : YangQingJian
 * @date : 2022/12/27
 */
public interface LoginTypeInterface {

    /**
     * 判断是否当前支持处理
     *
     * @param loginRequest
     * @return
     */
    boolean support(LoginRequest loginRequest);

    /**
     * 认证
     *
     * @param loginRequest
     * @return
     */
    LoginUser auth(LoginRequest loginRequest);

}
