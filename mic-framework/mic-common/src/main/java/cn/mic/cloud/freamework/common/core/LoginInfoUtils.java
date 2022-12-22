package cn.mic.cloud.freamework.common.core;

import cn.mic.cloud.freamework.common.vos.LoginUser;

/**
 * 用户登录 本地线程信息
 * 需要 在业务系统内 继承 配置类 LoginUserConfig
 *
 * @author Yorking
 * @date 2020/06/22
 * @see
 */
public class LoginInfoUtils {

    private static ThreadLocal<LoginUser> loginUserThreadLocal = new InheritableThreadLocal<>();

    public static LoginUser getLoginUser() {
        return loginUserThreadLocal.get();
    }

    public static Long getUserId() {
        if (getLoginUser() != null) {
            return getLoginUser().getUserId();
        }
        return null;
    }

    public static Long getDepPosId() {
        if (getLoginUser() != null) {
            return getLoginUser().getDepPosId();
        }
        return null;
    }




}
