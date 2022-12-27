package cn.mic.cloud.security.config;

import cn.hutool.core.util.ObjectUtil;
import cn.mic.cloud.security.filter.TokenAuthenticationFilter;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author : YangQingJian
 * @date : 2022/12/23
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenAuthenticationFilter tokenAuthenticationFilter;

    @Autowired
    private SecurityCommonConfig securityCommonConfig;

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /**
         * 组装默认的免登陆
         */
        assembleIgnoreUrls(getDefaultIgnores(), http);
        /**
         * 组装自定义的免登陆
         */
        assembleIgnoreUrls(securityCommonConfig.getIgnoreUrls(), http);

        // @formatter:off
        http.cors()
                // 关闭 CSRF
                .and().csrf().disable()
                // 登录行为由自己实现，参考 AuthController#login
                .formLogin().disable()
                .httpBasic().disable()
                // 认证请求
                .authorizeRequests()
                // 所有请求都需要登录访问
                .anyRequest()
                .authenticated()
                // 登出行为由自己实现，参考 AuthController#logout
                .and().logout().disable()
                // Session 管理
                .sessionManagement()
                // 因为使用了JWT，所以这里不管理Session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                // 异常处理

        http.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * 登录以及swagger放开
     *
     * @return
     */
    private List<String> getDefaultIgnores() {
        return Lists.newArrayList("/api/auth/**", "/v2/**", "/doc.html", "/swagger-resources/**", "/webjars/**");
    }

    private void assembleIgnoreUrls(List<String> ignores, HttpSecurity http) throws Exception {
        if (ObjectUtil.isEmpty(ignores)) {
            return;
        }
        for (String temp : ignores) {
            http.authorizeRequests().antMatchers(temp).permitAll();
        }
    }
}