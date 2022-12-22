package cn.mic.cloud.biz.test.api;

import org.springframework.web.bind.annotation.GetMapping;
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


}
