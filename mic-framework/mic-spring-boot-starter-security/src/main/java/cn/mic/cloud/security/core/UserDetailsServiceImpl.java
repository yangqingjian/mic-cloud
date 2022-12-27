package cn.mic.cloud.security.core;

import cn.hutool.core.util.ObjectUtil;
import cn.mic.cloud.freamework.common.core.LoginUser;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : YangQingJian
 * @date : 2022/12/23
 */
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (ObjectUtil.equals("admin", username)) {
            LoginUser loginUser = new LoginUser();
            loginUser.setUsername(username);
            loginUser.setPassword(new BCryptPasswordEncoder().encode("123456"));
            loginUser.setCurrentDepartPosition("654321");
            SimpleGrantedAuthority adminRole = new SimpleGrantedAuthority("admin");
            SimpleGrantedAuthority clientRole = new SimpleGrantedAuthority("client");
            /**
             * 设置角色信息
             */
            List<SimpleGrantedAuthority> authorities = Lists.newArrayList(adminRole, clientRole);
            loginUser.setAuthorities(authorities);
            return loginUser;
        }
        return null;
    }

}
