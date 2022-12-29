package cn.mic.cloud.service.filter;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.mic.cloud.freamework.common.core.login.LoginUser;
import cn.mic.cloud.freamework.common.exception.AuthenticationException;
import cn.mic.cloud.freamework.common.utils.SecurityCoreUtils;
import cn.mic.cloud.service.feign.DefaultServiceLoginUserFeign;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
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

/**
 * @author : YangQingJian
 * @date : 2022/12/23
 */
@Component
@RequiredArgsConstructor
@Order(value = Integer.MIN_VALUE + 1)
@Slf4j
public class ServiceCommonTokenAuthenticationFilter extends OncePerRequestFilter {

    @Value("${loginUserPath:/loginUser}")
    private String loginUserPath;

    private final DefaultServiceLoginUserFeign defaultServiceLoginUserFeign;

    /**
     * Same contract as for {@code doFilter}, but guaranteed to be
     * just invoked once per request within a single request thread.
     * See {@link #shouldNotFilterAsyncDispatch()} for details.
     * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
     * default ServletRequest and ServletResponse ones.
     *
     * @param request
     * @param response
     * @param filterChain
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        /**
         * 如果是登录的uri则直接放行
         */
        String requestURI = request.getRequestURI();
        if (StrUtil.startWith(requestURI,loginUserPath)){
            filterChain.doFilter(request, response);
            return;
        }
        Authentication currentAuthentication = SecurityContextHolder.getContext().getAuthentication();
        if (ObjectUtil.isNotNull(currentAuthentication)){
            filterChain.doFilter(request, response);
            return;
        }
        String authorization = SecurityCoreUtils.getAuthorization(request);
        if (StrUtil.isBlank(authorization)) {
            filterChain.doFilter(request, response);
            return;
        }
        LoginUser loginUser = defaultServiceLoginUserFeign.redisGetToken(authorization);
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