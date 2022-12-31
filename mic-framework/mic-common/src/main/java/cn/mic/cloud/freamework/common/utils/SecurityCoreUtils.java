package cn.mic.cloud.freamework.common.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTHeader;
import cn.hutool.jwt.RegisteredPayload;
import cn.mic.cloud.freamework.common.constants.SecurityConstants;
import cn.mic.cloud.freamework.common.core.login.LoginAuthUser;
import cn.mic.cloud.freamework.common.core.login.SimpleAuthority;
import cn.mic.cloud.freamework.common.exception.InvalidParameterException;
import cn.mic.cloud.freamework.common.exception.TokenExpireException;
import cn.mic.cloud.freamework.common.vos.Result;
import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import static cn.mic.cloud.freamework.common.constants.SecurityConstants.*;

/**
 * @author : YangQingJian
 * @date : 2022/12/28
 */
@Slf4j
public class SecurityCoreUtils {

    /**
     * 从 request 的 header 中获取 Authorization
     *
     * @param request 请求
     * @return JWT
     */
    public static String getAuthorization(HttpServletRequest request) {
        String bearerToken = request.getHeader(SecurityConstants.HEADER_TOKEN_PARAM);
        return StrUtil.isBlank(bearerToken) ? request.getParameter(SecurityConstants.HEADER_TOKEN_PARAM) : bearerToken;
    }

    /**
     * 判断指定url地址是否匹配指定url集合中的任意一个
     *
     * @param urlPath 指定url地址
     * @return 是否匹配  匹配返回true，不匹配返回false
     */
    public static boolean matches(String urlPath, List<String> urls) {
        if (ObjectUtil.isEmpty(urls)) {
            return false;
        }
        for (String url : urls) {
            if (isMatch(url, urlPath)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断url是否与规则配置:
     * ? 表示单个字符
     * * 表示一层路径内的任意字符串，不可跨层级
     * ** 表示任意层路径
     *
     * @param url     匹配规则
     * @param urlPath 需要匹配的url
     * @return
     */
    private static boolean isMatch(String url, String urlPath) {
        AntPathMatcher matcher = new AntPathMatcher();
        return matcher.match(url, urlPath);
    }

    /**
     * 登录以及swagger放开
     *
     * @return
     */
    public static List<String> getDefaultIgnoresAuthUrl() {
        return Lists.newArrayList("/", "/api/auth/**", "/v2/**", "/doc.html", "/swagger-resources/**", "/webjars/**");
    }

    /**
     * 打印result
     *
     * @param response
     * @param result
     */
    @SneakyThrows
    public static void printTokenException(HttpServletResponse response, Result result) {
        response.setContentType(APPLICATION_JSON_UTF8);
        PrintWriter writer = response.getWriter();
        writer.print(JSON.toJSONString(result));
    }

    /**
     * 创建token
     *
     * @param loginUser
     * @param publicKey
     * @param expireDate
     * @return
     */
    public static String createToken(LoginAuthUser loginUser, String publicKey, Date expireDate) {
        String token = JWT.create()
                .setPayload(JWT_USER_NAME, loginUser.getUsername())
                .setPayload(JWT_LOGIN_PASSWORD, loginUser.getPassword())
                .setPayload(JWT_DEPART_POSITION_ID, loginUser.getDepartPositionId())
                .setPayload(JWT_AUTHORITIES, JSON.toJSONString(loginUser.getAuthorities()))
                /**
                 * 设置过期时间
                 */
                .setExpiresAt(expireDate)
                /**
                 * 设置发行日期
                 */
                .setIssuedAt(new Date())
                /**
                 * 设置生效日期
                 */
                .setNotBefore(new Date())
                .setKey(publicKey.getBytes())
                .sign();
        byte[] encodeToken = Base64.getEncoder().encode(token.getBytes());
        return TOKEN_PRE + new String(encodeToken);
    }

    /**
     * 转换token，异常返回的是空对象
     *
     * @param token
     * @return
     */
    public static LoginAuthUser parseToken(String token, String publicKey, HttpServletResponse response) {
        token = token.replace(TOKEN_PRE, "");
        /**
         * base64解码
         */
        try {
            token = new String(Base64.getDecoder().decode(token), UTF8);
        } catch (Exception e) {
            log.error("base64解码解码异常,token = {} , 异常信息={}", token, e.getMessage(), e);
            throw new InvalidParameterException("token解码解码异常");
        }
        JWT jwt = JWT.of(token);
        boolean verifyFlag = jwt.setKey(publicKey.getBytes()).verify();
        if (!verifyFlag) {
            log.error("token公钥不符合,token={}", token);
            throw new InvalidParameterException("token公钥不符合");
        }
        Date expireDate = new Date(Long.valueOf(jwt.getPayload(RegisteredPayload.EXPIRES_AT).toString()) * 1000);
        if (System.currentTimeMillis() > expireDate.getTime()) {
            log.error("token过期,token={} , token日期={}", token, DateUtil.format(expireDate, "yyyy-MM-dd HH:mm:ss"));
            throw new TokenExpireException("token过期");
        }
        // JWT
        Object header = jwt.getHeader(JWTHeader.TYPE);
        // HS256(带有 SHA-256 的 HMAC 是一种对称算法, 双方之间仅共享一个 密钥。由于使用相同的密钥生成签名和验证签名, 因此必须注意确保密钥不被泄密)
        String ALGORITHM = (String) jwt.getHeader(JWTHeader.ALGORITHM);
        // loginCode
        Object username = jwt.getPayload(JWT_USER_NAME);
        Object password = jwt.getPayload(JWT_LOGIN_PASSWORD);
        Object departPositionId = jwt.getPayload(JWT_DEPART_POSITION_ID);
        Object authorities = jwt.getPayload(JWT_AUTHORITIES);
        Assert.notNull(username, "jwt中的用户名为空");
        //Assert.notNull(password, "jwt中的密钥为空");
        /**
         * 设置loginUser
         */
        LoginAuthUser loginUser = new LoginAuthUser();
        loginUser.setUsername((String) username);
        loginUser.setPassword((String) password);
        loginUser.setDepartPositionId((String) departPositionId);
        if (ObjectUtil.isNotNull(authorities)) {
            loginUser.setAuthorities(JSON.parseArray((String) authorities, SimpleAuthority.class));
        }
        return loginUser;
    }

    /**
     * 去掉前缀
     */
    public static String removeHeaderPrefix(String token) {
        if (StrUtil.isBlank(token)) {
            return token;
        }
        return token.replace(TOKEN_PRE, "");
    }

    /**
     * token的md5
     *
     * @param token
     * @return
     */
    public static String getTokenRedisKey(String token) {
        Assert.hasText(token, "token不能为空");
        token = removeHeaderPrefix(token);
        return DigestUtil.md5Hex(token);
    }


}
