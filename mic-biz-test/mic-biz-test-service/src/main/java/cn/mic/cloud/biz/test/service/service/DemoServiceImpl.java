package cn.mic.cloud.biz.test.service.service;

import cn.mic.cloud.biz.test.domain.Demo;
import cn.mic.cloud.biz.test.service.repository.DemoRepository;
import cn.mic.cloud.mybatis.plus.core.BaseEntityServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : YangQingJian
 * @date : 2022/12/22
 */
@Service
@RequiredArgsConstructor
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

}
