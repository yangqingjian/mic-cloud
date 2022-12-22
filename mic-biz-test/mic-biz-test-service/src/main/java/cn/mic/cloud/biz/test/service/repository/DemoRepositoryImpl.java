package cn.mic.cloud.biz.test.service.repository;


import cn.mic.cloud.biz.test.domain.Demo;
import cn.mic.cloud.biz.test.service.mapper.DemoMapper;
import cn.mic.cloud.mybatis.plus.core.BaseEntityRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * @author : YangQingJian
 * @date : 2022/12/22
 */
@Repository
@RequiredArgsConstructor
public class DemoRepositoryImpl extends BaseEntityRepositoryImpl<Demo> implements DemoRepository {

    private final DemoMapper demoMapper;


}
