package cn.mic.cloud.service.filter;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.mic.cloud.freamework.common.core.login.LoginAuthInterface;
import cn.mic.cloud.freamework.common.core.login.LoginAuthUser;
import cn.mic.cloud.freamework.common.core.login.request.LoginTokenRedisGetRequest;
import cn.mic.cloud.freamework.common.exception.InvalidParameterException;
import cn.mic.cloud.freamework.common.exception.TokenExpireException;
import cn.mic.cloud.freamework.common.utils.SecurityCoreUtils;
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

import static cn.mic.cloud.freamework.common.constants.SecurityConstants.TOKEN_BAD_EXCEPTION_ATTR;
import static cn.mic.cloud.freamework.common.constants.SecurityConstants.TOKEN_BAD_EXCEPTION_URL;

/**
 * @author : YangQingJian
 * @date : 2022/12/23
 */
@Component
@RequiredArgsConstructor
@Order(value = Integer.MIN_VALUE + 1)
@Slf4j
public class ServiceCommonTokenAuthenticationFilter extends OncePerRequestFilter {

    @Value("${security:/loginUser}")
    private String loginUserPath;

    /**
     * jwt密钥
     */
    @Value("${security.publicKey:123456789}")
    private String publicKey;

    private final LoginAuthInterface loginAuthInterface;


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
        if (StrUtil.startWith(requestURI, loginUserPath)) {
            filterChain.doFilter(request, response);
            return;
        }
        Authentication currentAuthentication = SecurityContextHolder.getContext().getAuthentication();
        if (ObjectUtil.isNotNull(currentAuthentication)) {
            filterChain.doFilter(request, response);
            return;
        }
        String authorization = SecurityCoreUtils.getAuthorization(request);
        if (StrUtil.isBlank(authorization)) {
            filterChain.doFilter(request, response);
            return;
        }
        /**
         * 获取token中的信息,已经在里面处理了异常
         */
        LoginAuthUser redisLoginUser = null;
        try {
            LoginAuthUser tokenLoginUser = SecurityCoreUtils.parseToken(authorization, publicKey, response);
            redisLoginUser = loginAuthInterface.redisGetToken(LoginTokenRedisGetRequest.builder().key(authorization).build());
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


}