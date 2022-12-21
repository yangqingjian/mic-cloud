package cn.mic.cloud.biz.swagger.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : YangQingJian
 * @date : 2022/12/20
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

    @GetMapping("/testHello")
    public String testHello(){
        return "hello";
    }

}
