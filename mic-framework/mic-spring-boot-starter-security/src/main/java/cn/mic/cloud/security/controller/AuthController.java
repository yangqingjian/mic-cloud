package cn.mic.cloud.security.controller;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.mic.cloud.framework.redis.comp.RedisKit;
import cn.mic.cloud.freamework.common.core.LoginUser;
import cn.mic.cloud.freamework.common.exception.AuthenticationException;
import cn.mic.cloud.freamework.common.exception.InvalidParameterException;
import cn.mic.cloud.freamework.common.vos.Result;
import cn.mic.cloud.security.constants.LoginTypeEnum;
import cn.mic.cloud.security.vo.LoginRequest;
import cn.mic.cloud.security.vo.TokenResult;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static cn.mic.cloud.security.constants.SecurityConstants.AUTHORIZATION_HEADER;
import static cn.mic.cloud.security.constants.SecurityConstants.CACHE_SMS_CODE;

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

    @Resource
    private RedisKit redisKit;

    private final Integer SIX_HOURS = 6;

    /**
     * 登录
     */
    @ApiOperation("登录")
    @PostMapping("/login")
    public Result<TokenResult> login(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = getAuthentication(loginRequest);
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

    private Authentication getAuthentication(LoginRequest loginRequest) {
        if (ObjectUtil.equals(loginRequest.getLoginType(), LoginTypeEnum.USERNAME_PASSWORD)) {
            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getLoginName(), loginRequest.getLoginSecret());
            try{
                return authenticationManager.authenticate(authenticationToken);
            }catch (Exception exception){
                log.error("用户名密码认证异常，错误信息={}" , exception.getMessage() , exception);
                throw new AuthenticationException("用户名或者密码错误");
            }
        }
        /**
         * 手机验证码登录
         */
        if (ObjectUtil.equals(loginRequest.getLoginType(), LoginTypeEnum.SMS_CODE)) {
            String smsCode = redisKit.getStr(CACHE_SMS_CODE + loginRequest.getLoginName());
            if (StrUtil.isBlank(smsCode)) {
                throw new AuthenticationException("手机验证码不对或者手机验证码已超时");
            }
            if (ObjectUtil.equals(loginRequest.getLoginSecret(), smsCode)) {
                LoginUser loginUser = new LoginUser();
                loginUser.setUsername("admin");
                loginUser.setPassword("123456");
                loginUser.setCurrentDepartPosition("123456");
                SimpleGrantedAuthority adminRole = new SimpleGrantedAuthority("admin");
                SimpleGrantedAuthority clientRole = new SimpleGrantedAuthority("client");
                List<SimpleGrantedAuthority> authorities = Lists.newArrayList(adminRole, clientRole);
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginUser.getUsername(),
                        null, authorities);
                usernamePasswordAuthenticationToken.setDetails(loginUser);
                return usernamePasswordAuthenticationToken;
            }
            throw new  AuthenticationException("手机号或者验证码错误");
        }
        throw new  AuthenticationException("登录类型【%s】未实现",loginRequest.getLoginType().getDesc());
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
        String authorization = request.getHeader(AUTHORIZATION_HEADER);
        if (StrUtil.isBlank(authorization)) {
            throw new InvalidParameterException("token为空");
        }
        SecurityContextHolder.clearContext();
        redisTemplate.delete(authorization);
        return Result.ok("退出成功");
    }

    /**
     * 获取手机验证码
     *
     * @param mobile
     * @return
     */
    @ApiOperation("获取手机验证码")
    @PostMapping("/getSmsCode")
    public Result<String> getSmsCode(@RequestParam("mobile") String mobile, @RequestParam(name = "timeout", required = false, defaultValue = "300") Long timeout) {
        Integer code = new Random().nextInt(100000);
        String cacheCode = String.format("%06d", code);
        log.info("mobile = {} , cacheCode = {}" , mobile , cacheCode);
        redisKit.set(CACHE_SMS_CODE + mobile,cacheCode , timeout, TimeUnit.SECONDS);
        return Result.ok();
    }


}