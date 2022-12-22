package cn.mic.cloud.biz.test.web.controller;

import cn.mic.cloud.biz.test.domain.Demo;
import cn.mic.cloud.biz.test.feign.DemoFeign;
import cn.mic.cloud.web.core.AbstractBaseEntityController;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : YangQingJian
 * @date : 2022/12/22
 */
@RestController
@RequestMapping("/demo")
@RequiredArgsConstructor
public class DemoController extends AbstractBaseEntityController<Demo> {

    private final DemoFeign demoFeign;

    /**
     * 测试
     *
     * @param word
     * @return
     */
    @GetMapping("/sayHello")
    public String sayHello(@RequestParam("word") String word) {
        return demoFeign.sayHello(word);
    }

}
