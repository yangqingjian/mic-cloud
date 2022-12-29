package cn.mic.cloud.freamework.common.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.mic.cloud.freamework.common.constants.SecurityConstants;
import com.google.common.collect.Lists;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author : YangQingJian
 * @date : 2022/12/28
 */
public class SecurityCoreUtils {

    /**
     * 从 request 的 header 中获取 Authorization
     *
     * @param request 请求
     * @return JWT
     */
    public static String getAuthorization(HttpServletRequest request) {
        String bearerToken = request.getHeader(SecurityConstants.HEADER_TOKEN_PARAM);
        return StrUtil.isBlank(bearerToken) ? request.getParameter(SecurityConstants.HEADER_TOKEN_PARAM) : bearerToken;
    }

    /**
     * 判断指定url地址是否匹配指定url集合中的任意一个
     *
     * @param urlPath 指定url地址
     * @return 是否匹配  匹配返回true，不匹配返回false
     */
    public static boolean matches(String urlPath, List<String> urls) {
        if (ObjectUtil.isEmpty(urls)) {
            return false;
        }
        for (String url : urls) {
            if (isMatch(url, urlPath)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断url是否与规则配置:
     * ? 表示单个字符
     * * 表示一层路径内的任意字符串，不可跨层级
     * ** 表示任意层路径
     *
     * @param url     匹配规则
     * @param urlPath 需要匹配的url
     * @return
     */
    private static boolean isMatch(String url, String urlPath) {
        AntPathMatcher matcher = new AntPathMatcher();
        return matcher.match(url, urlPath);
    }

    /**
     * 登录以及swagger放开
     *
     * @return
     */
    public static List<String> getDefaultIgnoresAuthUrl() {
        return Lists.newArrayList("/","/api/auth/**", "/v2/**", "/doc.html", "/swagger-resources/**", "/webjars/**");
    }


}
