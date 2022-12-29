package cn.mic.cloud.biz.test.api;

import cn.mic.cloud.biz.test.vo.DemoConverterVo;
import cn.mic.cloud.freamework.common.core.login.LoginRequest;
import cn.mic.cloud.freamework.common.core.login.LoginUser;
import cn.mic.cloud.freamework.common.vos.login.LoginSmsCodeSendRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author : YangQingJian
 * @date : 2022/12/22
 */
public interface DemoApi {

    /**
     * sayHello方法
     *
     * @param word
     * @return
     */
    @GetMapping("/sayHello")
    String sayHello(@RequestParam("word") String word);

    /**
     * 测试枚举
     *
     * @param request
     * @return
     */
    @PostMapping("/testEnum")
    DemoConverterVo testEnum(@RequestBody DemoConverterVo request);




}
