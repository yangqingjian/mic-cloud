package cn.mic.cloud.security.controller;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Method;
import cn.mic.cloud.framework.redis.comp.RedisKit;
import cn.mic.cloud.freamework.common.core.login.LoginUser;
import cn.mic.cloud.freamework.common.exception.InvalidParameterException;
import cn.mic.cloud.freamework.common.exception.SystemException;
import cn.mic.cloud.freamework.common.vos.Result;
import cn.mic.cloud.freamework.common.vos.login.LoginSmsCodeSendRequest;
import cn.mic.cloud.security.config.SecurityCommonConfig;
import cn.mic.cloud.security.core.HttpSecurityUtils;
import cn.mic.cloud.security.core.LoginInterface;
import cn.mic.cloud.freamework.common.core.login.LoginRequest;
import cn.mic.cloud.security.vo.TokenResult;
import com.alibaba.fastjson2.JSON;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static cn.mic.cloud.security.constants.SecurityConstants.*;

/**
 * @author : YangQingJian
 * @date : 2022/12/23
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Resource
    private  RedisKit redisKit;

    @Resource
    private  ObjectProvider<LoginInterface> loginInterfaces;

    @Resource
    private  HttpSecurityUtils httpSecurityUtils;

    @Resource
    private SecurityCommonConfig securityCommonConfig;

    /**
     * 登录
     */
    @ApiOperation("登录")
    @PostMapping("/login")
    public Result<TokenResult> login(@Valid @RequestBody LoginRequest loginRequest) {
        Optional<LoginInterface> loginInterfaceOptional = loginInterfaces.stream().filter(temp -> temp.support(loginRequest)).findFirst();
        if (!loginInterfaceOptional.isPresent()) {
            throw new SystemException("登录类型【%s】未实现", loginRequest.getLoginType().getDesc());
        }
        LoginUser loginUser = loginInterfaceOptional.get().auth(loginRequest);
        Authentication authentication = getAuthentication(loginUser);
        // 把authentication放到当前线程,便是认证完成
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String fastUUID = IdUtil.fastUUID();
        DateTime expireDate = DateUtil.offset(new Date(), DateField.HOUR, CACHE_TOKEN_HOURS);
        // 把用户信息存到redis,并设置有效期
        redisKit.set(fastUUID, JSON.toJSONString(authentication.getPrincipal()), CACHE_TOKEN_HOURS, TimeUnit.HOURS);
        TokenResult tokenResult = new TokenResult();
        tokenResult.setToken(fastUUID);
        tokenResult.setExpireDate(expireDate);
        return Result.ok(tokenResult);
    }

    private Authentication getAuthentication(LoginUser loginUser) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginUser,
                null, loginUser.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(loginUser);
        return usernamePasswordAuthenticationToken;
    }

    /**
     * 登出
     *
     * @param request
     * @return
     */
    @ApiOperation("登出")
    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request) {
        // 清除认证信息
        String authorization = httpSecurityUtils.getAuthorization(request);
        if (StrUtil.isBlank(authorization)) {
            throw new InvalidParameterException("token为空");
        }
        SecurityContextHolder.clearContext();
        redisKit.remove(authorization);
        return Result.ok("退出成功");
    }

    /**
     * 获取手机验证码
     *
     * @param request
     * @return
     */
    @ApiOperation("获取手机验证码")
    @PostMapping("/getSmsCode")
    public Result<String> getSmsCode(@Validated @RequestBody LoginSmsCodeSendRequest request) {
        String result = httpSecurityUtils.getRemoteObject(securityCommonConfig.getSendSmsCodeUrl(), Method.POST, request, String.class);
        log.info("getSmsCode , request = {} , result = {}" , JSON.toJSONString(request) , result);
        return Result.ok();
    }


}