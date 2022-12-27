package cn.mic.cloud.security.filter;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.ResponseUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
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
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取AuthorizationToken
        String authorization = getAuthorization(request);
        if (StrUtil.isBlank(authorization)) {
            filterChain.doFilter(request, response);
            return;
        }
        // 从缓存中获取用户信息
        UserDetails userDetails = (UserDetails) redisTemplate.opsForValue().get(authorization);
        if (ObjectUtil.isNull(userDetails)){
            throw new CredentialsExpiredException("证书过期");
        }

        // 构建AuthenticationToken
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        // 把AuthenticationToken放到当前线程,表示认证完成
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    /**
     * 从 request 的 header 中获取 Authorization
     *
     * @param request 请求
     * @return JWT
     */
    public String getAuthorization(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        return bearerToken;
    }

}