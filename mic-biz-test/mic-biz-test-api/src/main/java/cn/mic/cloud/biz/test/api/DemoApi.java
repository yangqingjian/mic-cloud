package cn.mic.cloud.biz.test.api;

import cn.mic.cloud.biz.test.vo.DemoConverterVo;
import cn.mic.cloud.freamework.common.core.LoginUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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

    /**
     * 根据用户名查询
     *
     * @param username
     * @return
     */
    @PostMapping("/selectByLoginName")
    LoginUser selectByLoginName(@RequestParam("username") String username);

    /**
     * 根据手机号查询
     *
     * @param mobile
     * @return
     */
    @PostMapping("/selectByMobile")
    LoginUser selectByMobile(@RequestParam("mobile") String mobile);
}
