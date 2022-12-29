package cn.mic.cloud.freamework.common.core.login;

import cn.mic.cloud.freamework.common.vos.login.LoginSmsCodeSendRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

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
     * @param loginRequest
     * @return
     */
    @PostMapping("/getLoginUser")
    LoginUser getLoginUser(@RequestBody LoginRequest loginRequest);

    /**
     * 发送验证码
     *
     * @param request
     */
    @PostMapping("/sendSmsCode")
    String sendSmsCode(@RequestBody LoginSmsCodeSendRequest request);

    /**
     * 存储token，返回过期时间
     *
     * @param loginUser
     * @return
     */
    @PostMapping("/redisStoreToken")
    Date redisStoreToken(@RequestParam("key") String key, @RequestParam(name = "expireSeconds", required = false, defaultValue = "3600") Integer expireSeconds, @RequestBody LoginUser loginUser);

    /**
     * 查询token
     *
     * @param key
     * @return
     */
    @PostMapping("/redisGetToken")
    LoginUser redisGetToken(@RequestParam("key") String key);

    /**
     * 删除token
     *
     * @param key
     * @return
     */
    @PostMapping("/redisRemoveToken")
    Boolean redisRemoveToken(@RequestParam("key") String key);


}
