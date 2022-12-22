package cn.mic.cloud.biz.test.service.service;

import cn.mic.cloud.biz.test.domain.Demo;
import cn.mic.cloud.mybatis.plus.core.BaseEntityServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author : YangQingJian
 * @date : 2022/12/22
 */
@Service
public class DemoServiceImpl extends BaseEntityServiceImpl<Demo> implements DemoService {

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

}
