package cn.mic.cloud.biz.test.api;

import cn.mic.cloud.biz.test.vo.DemoConverterVo;
import org.springframework.web.bind.annotation.*;

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
