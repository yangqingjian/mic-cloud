package cn.mic.cloud.security.core;

import cn.hutool.core.util.ObjectUtil;
import cn.mic.cloud.freamework.common.core.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author : YangQingJian
 * @date : 2022/12/23
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (ObjectUtil.equals("admin", username)) {
            LoginUser loginUser = new LoginUser();
            loginUser.setUsername(username);
            loginUser.setPassword(new BCryptPasswordEncoder().encode("123456"));
            return loginUser;
        }
        return null;
    }

}
