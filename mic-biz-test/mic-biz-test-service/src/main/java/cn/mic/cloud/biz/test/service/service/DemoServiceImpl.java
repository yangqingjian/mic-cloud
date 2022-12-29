package cn.mic.cloud.biz.test.service.service;

import cn.mic.cloud.biz.test.domain.Demo;
import cn.mic.cloud.biz.test.service.repository.DemoRepository;
import cn.mic.cloud.biz.test.vo.DemoConverterVo;
import cn.mic.cloud.mybatis.plus.core.BaseEntityServiceImpl;
import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author : YangQingJian
 * @date : 2022/12/22
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DemoServiceImpl extends BaseEntityServiceImpl<Demo> implements DemoService {

    private final DemoRepository demoRepository;

    /**
     * sayHello方法
     *
     * @param word
     * @return
     */
    @Override
    public String sayHello(String word) {
        return word;
    }

    /**
     * 测试枚举
     *
     * @param request
     * @return
     */
    @Override
    public DemoConverterVo testEnum(DemoConverterVo request) {
        log.info("request = {}", JSON.toJSONString(request));
        return request;
    }




}
