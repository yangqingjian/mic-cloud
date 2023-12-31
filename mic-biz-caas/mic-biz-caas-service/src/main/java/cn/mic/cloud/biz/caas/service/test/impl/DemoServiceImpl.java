package cn.mic.cloud.biz.caas.service.test.impl;

import cn.mic.cloud.biz.caas.domain.test.Demo;
import cn.mic.cloud.biz.caas.mapper.test.DemoMapper;
import cn.mic.cloud.biz.caas.service.test.DemoService;
import cn.mic.cloud.mybatis.plus.core.BaseEntityServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
* 测试 service实现接口
*
* @author : YangQingJian
* @date : 2023/1/6
*/
@Service
@Slf4j
public class DemoServiceImpl extends BaseEntityServiceImpl<Demo> implements DemoService {


}
