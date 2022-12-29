package cn.mic.cloud.security.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.mic.cloud.freamework.common.core.login.LoginAuthInterface;
import cn.mic.cloud.freamework.common.core.login.LoginRequest;
import cn.mic.cloud.freamework.common.core.login.LoginUser;
import cn.mic.cloud.freamework.common.exception.InvalidParameterException;
import cn.mic.cloud.freamework.common.exception.SystemException;
import cn.mic.cloud.freamework.common.utils.SecurityCoreUtils;
import cn.mic.cloud.freamework.common.vos.Result;
import cn.mic.cloud.freamework.common.vos.login.LoginSmsCodeSendRequest;
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

    /**
     * 登录
     */
    @ApiOperation("登录")
    @PostMapping("/login")
    public Result<TokenResult> login(@Valid @RequestBody LoginRequest loginRequest) {
        Optional<LoginTypeInterface> loginInterfaceOptional = loginInterfaces.stream().filter(temp -> temp.support(loginRequest)).findFirst();
        if (!loginInterfaceOptional.isPresent()) {
            throw new SystemException("登录类型【%s】未实现", loginRequest.getLoginType().getDesc());
        }
        LoginUser loginUser = loginInterfaceOptional.get().auth(loginRequest);
        Authentication authentication = getAuthentication(loginUser);
        // 把authentication放到当前线程,便是认证完成
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String fastUUID = IdUtil.fastUUID();
        Date expireDate = loginAuthInterface.redisStoreToken(fastUUID, loginUser);
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
        String authorization = SecurityCoreUtils.getAuthorization(request);
        if (StrUtil.isBlank(authorization)) {
            throw new InvalidParameterException("token为空");
        }
        loginAuthInterface.redisRemoveToken(authorization);
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
        String result = loginAuthInterface.sendSmsCode(request);
        log.info("getSmsCode , request = {} , result = {}", JSON.toJSONString(request), result);
        return Result.ok();
    }


}