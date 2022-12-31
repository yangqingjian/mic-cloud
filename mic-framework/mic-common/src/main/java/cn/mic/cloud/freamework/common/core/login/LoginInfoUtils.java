package cn.mic.cloud.freamework.common.core.login;

import cn.hutool.core.util.ObjectUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 用户登录 本地线程信息
 * 需要 在业务系统内 继承 配置类 LoginUserConfig
 *
 * @author Yorking
 * @date 2020/06/22
 * @see
 */
public class LoginInfoUtils {

    /**
     * 获取登录名称
     *
     * @return
     */
    public static String getLoginName() {
        LoginAuthUser loginUser = getLoginUser();
        if (ObjectUtil.isNull(loginUser)) {
            return null;
        }
        return loginUser.getUsername();
    }

    /**
     * 获取当前用户
     *
     * @return
     */
    public static LoginAuthUser getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (ObjectUtil.isNull(authentication)) {
            return null;
        }
        return (LoginAuthUser) authentication;
    }


}
