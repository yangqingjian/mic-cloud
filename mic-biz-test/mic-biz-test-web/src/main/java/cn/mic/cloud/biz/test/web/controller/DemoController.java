package cn.mic.cloud.biz.test.web.controller;

import cn.mic.cloud.biz.test.domain.Demo;
import cn.mic.cloud.biz.test.feign.DemoFeign;
import cn.mic.cloud.biz.test.vo.DemoConverterVo;
import cn.mic.cloud.biz.test.vo.DemoMessageVo;
import cn.mic.cloud.freamework.common.vos.Result;
import cn.mic.cloud.rocket.mq.constants.MqDelayLevel;
import cn.mic.cloud.rocket.mq.mq.RocketMqKit;
import cn.mic.cloud.web.core.AbstractBaseEntityController;
import com.alibaba.fastjson2.JSON;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;


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
    private final RocketMqKit rocketMqKit;

    /**
     * 测试
     *
     * @param word
     * @return
     */
    @ApiOperation("测试微服务")
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
    @ApiOperation("测试seata-3000秒回滚")
    @GlobalTransactional(rollbackFor = Exception.class, timeoutMills = 1000)
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

    /**
     * 消息测试
     *
     * @param userName
     */
    @GetMapping("/testRocketMq")
    @ApiOperation("消息测试")
    public void testRocketMq(@RequestParam("userName") String userName) {
        DemoMessageVo demoMessageVo = new DemoMessageVo();
        demoMessageVo.setUserName(userName);
        rocketMqKit.send(demoMessageVo);
    }

    /**
     * 延迟消息测试,延迟五分钟
     *
     * @param userName
     */
    @ApiOperation("延迟消息测试")
    @GetMapping("/testRocketMqDelay")
    public void testRocketMqDelay(@RequestParam("userName") String userName) {
        DemoMessageVo demoMessageVo = new DemoMessageVo();
        demoMessageVo.setUserName(userName);
        rocketMqKit.sendDelay(demoMessageVo, MqDelayLevel.DELAY_1M);
    }

    /**
     * 测试枚举转换
     * @param request
     * @return
     */
    @ApiOperation("测试枚举转换")
    @PostMapping("/testEnum")
    public Result<DemoConverterVo> testEnum(@RequestBody DemoConverterVo request){
        log.info("request = {}" , JSON.toJSONString(request));
        return Result.ok(demoFeign.testEnum(request));
    }


}
