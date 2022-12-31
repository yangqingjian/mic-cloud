package cn.mic.cloud.freamework.common.core.login;

import cn.mic.cloud.freamework.common.core.login.request.*;
import cn.mic.cloud.freamework.common.core.login.response.LoginAuthSmsCodeSendResponse;
import cn.mic.cloud.freamework.common.core.login.response.LoginTokenRedisRemoveResponse;
import cn.mic.cloud.freamework.common.core.login.response.LoginTokenRedisStoreResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 登录抽象的接口
 *
 * @author : YangQingJian
 * @date : 2022/12/28
 */
public interface LoginAuthInterface {

    /**
     * 根据用户名查询
     *
     * @param request
     * @return
     */
    @PostMapping("/getLoginUser")
    LoginAuthUser getLoginUser(@Validated @RequestBody LoginAuthRequest request);

    /**
     * 发送验证码
     *
     * @param request
     */
    @PostMapping("/sendSmsCode")
    LoginAuthSmsCodeSendResponse sendSmsCode(@Validated @RequestBody LoginSmsCodeSendRequest request);

    /**
     * 存储token，返回过期时间
     *
     * @param request
     * @return
     */
    @PostMapping("/redisStoreToken")
    LoginTokenRedisStoreResponse redisStoreToken(@Validated @RequestBody LoginTokenRedisStoreRequest request);

    /**
     * 查询token
     *
     * @param request
     * @return
     */
    @PostMapping("/redisGetToken")
    LoginAuthUser redisGetToken(@Validated @RequestBody LoginTokenRedisGetRequest request);

    /**
     * 删除token
     *
     * @param request
     * @return
     */
    @PostMapping("/redisRemoveToken")
    LoginTokenRedisRemoveResponse redisRemoveToken(@Validated @RequestBody LoginTokenRedisRemoveRequest request);


}
