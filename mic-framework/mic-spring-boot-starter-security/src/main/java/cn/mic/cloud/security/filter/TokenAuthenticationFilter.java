package cn.mic.cloud.security.filter;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.mic.cloud.framework.redis.comp.RedisKit;
import cn.mic.cloud.freamework.common.core.login.LoginUser;
import cn.mic.cloud.freamework.common.exception.AuthenticationException;
import cn.mic.cloud.security.core.HttpSecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author : YangQingJian
 * @date : 2022/12/23
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final RedisKit redisKit;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取AuthorizationToken
        String authorization = HttpSecurityUtils.getAuthorization(request);
        if (StrUtil.isBlank(authorization)) {
            filterChain.doFilter(request, response);
            return;
        }
        // 从缓存中获取用户信息
        LoginUser loginUser = redisKit.getObj(authorization, LoginUser.class);
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

}