package cn.mic.cloud.security.controller;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.mic.cloud.freamework.common.exception.BusinessException;
import cn.mic.cloud.freamework.common.exception.InvalidParameterException;
import cn.mic.cloud.freamework.common.vos.Result;
import cn.mic.cloud.freamework.common.vos.ResultStatusEnum;
import cn.mic.cloud.security.vo.LoginRequest;
import cn.mic.cloud.security.vo.TokenResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author : YangQingJian
 * @date : 2022/12/23
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private final Integer SIX_HOURS = 6;

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result<TokenResult> login(@Valid @RequestBody LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getLoginName(), loginRequest.getLoginSecret());
        // 尝试对传递的Authentication对象进行身份Authentication ，如果成功，则返回完全填充的Authentication对象（包括授予的权限）。
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        if (ObjectUtil.isNull(authentication)) {
            throw new BusinessException("用户名或者密码错误");
        }
        // 把authentication放到当前线程,便是认证完成
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String fastUUID = IdUtil.fastUUID();

        DateTime expireDate = DateUtil.offset(new Date(), DateField.HOUR, SIX_HOURS);
        // 把用户信息存到redis,并设置有效期
        redisTemplate.opsForValue().set(fastUUID, authentication.getPrincipal(), SIX_HOURS, TimeUnit.HOURS);

        TokenResult tokenResult = new TokenResult();
        tokenResult.setToken(fastUUID);
        tokenResult.setExpireDate(expireDate);
        return Result.ok(tokenResult);
    }

    /**
     * 登出
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request) {
        // 清除认证信息
        String authorization = getAuthorization(request);
        if (StrUtil.isBlank(authorization)) {
            throw new InvalidParameterException("token为空");
        }
        SecurityContextHolder.clearContext();
        redisTemplate.delete(authorization);
        return Result.ok("退出成功");
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