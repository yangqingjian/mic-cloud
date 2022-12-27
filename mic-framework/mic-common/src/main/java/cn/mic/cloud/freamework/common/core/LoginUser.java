package cn.mic.cloud.freamework.common.core;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author : YangQingJian
 * @date : 2022/12/23
 */
@Data
@NoArgsConstructor
public class LoginUser implements org.springframework.security.core.userdetails.UserDetails {
    /**
     * 角色集合
     */
    private Collection<SimpleGrantedAuthority> authorities;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 当前职位
     */
    private Serializable currentDepartPosition;

    /**
     * 是否过期
     */
    private boolean accountNonExpired = false;

    /**
     * 是否锁定
     */
    private boolean accountNonLocked = false;

    /**
     * 是否过期
     */
    private boolean credentialsNonExpired = false;

    /**
     * 是否启用
     */
    private boolean enabled = true;

}
