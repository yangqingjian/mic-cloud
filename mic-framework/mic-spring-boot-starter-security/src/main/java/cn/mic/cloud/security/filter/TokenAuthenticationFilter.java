package cn.mic.cloud.security.filter;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.mic.cloud.freamework.common.core.login.LoginUser;
import cn.mic.cloud.freamework.common.exception.AuthenticationException;
import cn.mic.cloud.freamework.common.utils.SecurityCoreUtils;
import cn.mic.cloud.security.config.SecurityCommonConfig;
import cn.mic.cloud.security.feign.DefaultLoginUserFeign;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

/**
 * @author : YangQingJian
 * @date : 2022/12/23
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final SecurityCommonConfig securityCommonConfig;

    private final DefaultLoginUserFeign defaultLoginUserFeign;

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
        LoginUser loginUser = defaultLoginUserFeign.redisGetToken(authorization);
        if (ObjectUtil.isNull(loginUser)) {
            log.error("token=【%s】查询缓存失败", authorization);
            throw new AuthenticationException("token【%s】过期", authorization);
        }
        // 构建AuthenticationToken
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        authentication.setDetails(loginUser);
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