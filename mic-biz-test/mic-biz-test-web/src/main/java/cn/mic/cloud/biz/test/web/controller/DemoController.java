package cn.mic.cloud.biz.test.web.controller;

import cn.mic.cloud.biz.test.domain.Demo;
import cn.mic.cloud.biz.test.feign.DemoFeign;
import cn.mic.cloud.web.core.AbstractBaseEntityController;
import com.alibaba.fastjson2.JSON;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

/**
 * @author : YangQingJian
 * @date : 2022/12/22
 */
@RestController
@RequestMapping("/demo")
@Slf4j
@RequiredArgsConstructor
public class DemoController extends AbstractBaseEntityController<Demo> {

    private final DemoFeign demoFeign;

    /**
     * 测试
     *
     * @param word
     * @return
     */
    @ApiOperation("测试服务是否通")
    @GetMapping("/sayHello")
    public String sayHello(@RequestParam("word") String word) {
        return demoFeign.sayHello(word);
    }

    /**
     * 测试分部署事物,默认是60秒，这里设置为3秒
     *
     * @param sleep
     * @return
     */
    @ApiOperation("分布式事物大于3000即回滚")
    @GlobalTransactional(rollbackFor = Exception.class, timeoutMills = 3000)
    @GetMapping("/globalTransaction")
    public String globalTransaction(@RequestParam(value = "sleep") Integer sleep) throws Exception {
        log.info("globalTransaction start");
        Demo src = new Demo();
        src.fillOperationInfo("admin");
        src.setName("yangqingjian");
        Demo demo = demoFeign.saveOrUpdate(src);
        log.info("demo = {}", JSON.toJSONString(demo));
        Thread.sleep(sleep);
        /**
         * 校验阶段
         */
        checkGlobalTransaction(sleep, demo);
        log.info("globalTransaction end");
        return "success";
    }

    private void checkGlobalTransaction(Integer sleep, Demo srcDemo) {
        Demo targetDemo = demoFeign.selectById((String) srcDemo.getId());
        if (sleep > 3000) {
            Assert.isNull(targetDemo, "事物没回滚，请检查事物");
        } else {
            Assert.notNull(targetDemo, "事物没正常提交，请检查事物");
        }
    }


}
