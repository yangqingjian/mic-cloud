package cn.mic.cloud.security.filter;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.mic.cloud.freamework.common.core.login.LoginAuthInterface;
import cn.mic.cloud.freamework.common.core.login.LoginUser;
import cn.mic.cloud.freamework.common.exception.InvalidParameterException;
import cn.mic.cloud.freamework.common.exception.TokenExpireException;
import cn.mic.cloud.freamework.common.utils.SecurityCoreUtils;
import cn.mic.cloud.freamework.common.vos.Result;
import cn.mic.cloud.freamework.common.vos.ResultStatusEnum;
import cn.mic.cloud.security.config.SecurityCommonConfig;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static cn.mic.cloud.freamework.common.constants.SecurityConstants.TOKEN_BAD_EXCEPTION_ATTR;
import static cn.mic.cloud.freamework.common.constants.SecurityConstants.TOKEN_BAD_EXCEPTION_URL;

/**
 * @author : YangQingJian
 * @date : 2022/12/23
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final SecurityCommonConfig securityCommonConfig;

    private final LoginAuthInterface loginAuthInterface;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication currentAuthentication = SecurityContextHolder.getContext().getAuthentication();
        if (ObjectUtil.isNotNull(currentAuthentication)) {
            filterChain.doFilter(request, response);
            return;
        }
        if (SecurityCoreUtils.matches(request.getRequestURI(), getAllIgnoreUrl())) {
            log.info("requestUri = {} , 不需要转换token", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }
        // 获取AuthorizationToken
        String authorization = SecurityCoreUtils.getAuthorization(request);
        if (StrUtil.isBlank(authorization)) {
            filterChain.doFilter(request, response);
            return;
        }
        /**
         * 获取token中的信息,已经在里面处理了异常
         */
        /**
         * 获取token中的信息,已经在里面处理了异常
         */
        LoginUser redisLoginUser = null;
        try {
            LoginUser tokenLoginUser = SecurityCoreUtils.parseToken(authorization, securityCommonConfig.getPublicKey(), response);
            redisLoginUser = loginAuthInterface.redisGetToken(authorization);
            if (ObjectUtil.isNull(redisLoginUser)) {
                log.error("token=【%s】查询缓存失败", authorization);
                throw new TokenExpireException("token在redis中已失效");
            }
            if (ObjectUtil.notEqual(tokenLoginUser, redisLoginUser)) {
                log.error("token=【%s】查询缓存失败", authorization);
                throw new InvalidParameterException("redis中的对象和token解析对象不一致");
            }
        } catch (Exception e) {
            // 传递异常信息
            request.setAttribute(TOKEN_BAD_EXCEPTION_ATTR, e);
            // 指定处理该请求的处理器
            request.getRequestDispatcher(TOKEN_BAD_EXCEPTION_URL).forward(request, response);
            return;
        }
        // 构建AuthenticationToken
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(redisLoginUser, null, redisLoginUser.getAuthorities());
        authentication.setDetails(redisLoginUser);
        // 把AuthenticationToken放到当前线程,表示认证完成
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }


    private List<String> getAllIgnoreUrl() {
        List<String> ignoreTokenAuthentication = securityCommonConfig.getIgnoreTokenAuthentication();
        List<String> ignoreUrls = securityCommonConfig.getIgnoreUrls();
        List<String> defaultIgnores = SecurityCoreUtils.getDefaultIgnoresAuthUrl();
        List<String> resultList = Lists.newArrayList();
        if (ObjectUtil.isNotEmpty(ignoreTokenAuthentication)) {
            resultList.addAll(ignoreTokenAuthentication);
        }
        if (ObjectUtil.isNotEmpty(ignoreUrls)) {
            resultList.addAll(ignoreUrls);
        }
        if (ObjectUtil.isNotEmpty(defaultIgnores)) {
            resultList.addAll(defaultIgnores);
        }
        return resultList;
    }


}