package cn.mic.cloud.biz.test.service.service;

import cn.mic.cloud.biz.test.domain.Demo;
import cn.mic.cloud.biz.test.service.repository.DemoRepository;
import cn.mic.cloud.biz.test.vo.DemoConverterVo;
import cn.mic.cloud.freamework.common.core.LoginUser;
import cn.mic.cloud.mybatis.plus.core.BaseEntityServiceImpl;
import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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

    /**
     * 根据用户名查询
     *
     * @param username
     * @return
     */
    @Override
    public LoginUser selectByLoginName(String username) {
        return getLoginUser(username, "13880981076");
    }

    @NotNull
    private LoginUser getLoginUser(String username, String mobile) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUsername(username);
        loginUser.setPassword(new BCryptPasswordEncoder().encode("123456"));
        loginUser.setMobile(mobile);
        loginUser.setCurrentDepartPosition("123");
        SimpleGrantedAuthority adminRole = new SimpleGrantedAuthority("admin");
        SimpleGrantedAuthority clientRole = new SimpleGrantedAuthority("client");
        List<SimpleGrantedAuthority> authorities = Lists.newArrayList(adminRole, clientRole);
        loginUser.setAuthorities(authorities);
        return loginUser;
    }

    /**
     * 根据手机号查询
     *
     * @param mobile
     * @return
     */
    @Override
    public LoginUser selectByMobile(String mobile) {
        return getLoginUser("admin", "13880981076");

    }


}
