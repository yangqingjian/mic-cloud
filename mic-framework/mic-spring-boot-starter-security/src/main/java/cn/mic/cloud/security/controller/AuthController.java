package cn.mic.cloud.security.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.mic.cloud.freamework.common.core.login.LoginAuthInterface;
import cn.mic.cloud.freamework.common.core.login.request.LoginAuthRequest;
import cn.mic.cloud.freamework.common.core.login.LoginAuthUser;
import cn.mic.cloud.freamework.common.core.login.request.LoginTokenRedisRemoveRequest;
import cn.mic.cloud.freamework.common.core.login.request.LoginTokenRedisStoreRequest;
import cn.mic.cloud.freamework.common.core.login.response.LoginAuthSmsCodeSendResponse;
import cn.mic.cloud.freamework.common.core.login.response.LoginTokenRedisStoreResponse;
import cn.mic.cloud.freamework.common.exception.InvalidParameterException;
import cn.mic.cloud.freamework.common.exception.SystemException;
import cn.mic.cloud.freamework.common.utils.SecurityCoreUtils;
import cn.mic.cloud.freamework.common.vos.Result;
import cn.mic.cloud.freamework.common.core.login.request.LoginSmsCodeSendRequest;
import cn.mic.cloud.security.config.SecurityCommonConfig;
import cn.mic.cloud.security.core.LoginTypeInterface;
import cn.mic.cloud.security.vo.TokenResult;
import com.alibaba.fastjson2.JSON;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.Optional;

/**
 * @author : YangQingJian
 * @date : 2022/12/23
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Resource
    private ObjectProvider<LoginTypeInterface> loginInterfaces;
    /**
     * 此实现交给web去实现
     */
    @Resource
    private LoginAuthInterface loginAuthInterface;

    @Resource
    private SecurityCommonConfig securityCommonConfig;

    /**
     * 登录
     */
    @ApiOperation("登录")
    @PostMapping("/login")
    public Result<TokenResult> login(@Valid @RequestBody LoginAuthRequest loginAuthRequest) {
        Optional<LoginTypeInterface> loginInterfaceOptional = loginInterfaces.stream().filter(temp -> temp.support(loginAuthRequest)).findFirst();
        if (!loginInterfaceOptional.isPresent()) {
            throw new SystemException("登录类型【%s】未实现", loginAuthRequest.getLoginType().getDesc());
        }
        LoginAuthUser loginUser = loginInterfaceOptional.get().auth(loginAuthRequest);
        Authentication authentication = getAuthentication(loginUser);
        // 把authentication放到当前线程,便是认证完成
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Date expireDate = DateUtil.offsetSecond(new Date(), securityCommonConfig.getExpireTimeSeconds());
        String token = SecurityCoreUtils.createToken(loginUser, securityCommonConfig.getPublicKey(), expireDate);
        /**
         * 同是也存入redis(把前缀去掉)
         */
        LoginTokenRedisStoreResponse response = loginAuthInterface.redisStoreToken(LoginTokenRedisStoreRequest.builder().key(token).expireSeconds(securityCommonConfig.getExpireTimeSeconds()).loginUser(loginUser).build());
        TokenResult tokenResult = new TokenResult();
        tokenResult.setToken(token);
        tokenResult.setExpireDate(response.getExpireDate());
        return Result.ok(tokenResult);
    }

    private Authentication getAuthentication(LoginAuthUser loginUser) {
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
        String authorization = SecurityCoreUtils.getAuthorization(request);
        if (StrUtil.isBlank(authorization)) {
            throw new InvalidParameterException("token为空");
        }
        loginAuthInterface.redisRemoveToken(LoginTokenRedisRemoveRequest.builder().key(authorization).build());
        SecurityContextHolder.clearContext();
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
        LoginAuthSmsCodeSendResponse response = loginAuthInterface.sendSmsCode(request);
        log.info("getSmsCode , request = {} , result = {} ", JSON.toJSONString(request), response);
        return Result.ok();
    }


}