package cn.mic.cloud.security.config;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.mic.cloud.freamework.common.utils.SecurityCoreUtils;
import cn.mic.cloud.security.filter.TokenAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @author : YangQingJian
 * @date : 2022/12/23
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    private final TokenAuthenticationFilter tokenAuthenticationFilter;

    private final SecurityCommonConfig securityCommonConfig;

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
        assembleIgnoreUrls(SecurityCoreUtils.getDefaultIgnoresAuthUrl(), http);
        /**
         * 组装自定义的免登陆
         */
        assembleIgnoreUrls(securityCommonConfig.getIgnoreUrls(), http);
        /**
         * 组装不需要转换token的地址
         */
        assembleIgnoreUrls(securityCommonConfig.getIgnoreTokenAuthentication(), http);

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


    private void assembleIgnoreUrls(List<String> ignores, HttpSecurity http) throws Exception {
        if (ObjectUtil.isEmpty(ignores)) {
            return;
        }
        for (String temp : ignores) {
            http.authorizeRequests().antMatchers(temp).permitAll();
        }
    }

    // 配置 RestTemplate
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        // 创建一个 httpClient 简单工厂
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        // 设置连接超时
        factory.setConnectTimeout(securityCommonConfig.getConnectTimeOut());
        // 设置读取超时
        factory.setReadTimeout(securityCommonConfig.getReadTimeOut());
        RestTemplate restTemplate = new RestTemplate(factory);
        // 解决返回值乱码
        List<HttpMessageConverter<?>> httpMessageConverters = restTemplate.getMessageConverters();
        httpMessageConverters.stream().forEach(httpMessageConverter -> {
            if (httpMessageConverter instanceof StringHttpMessageConverter) {
                StringHttpMessageConverter messageConverter = (StringHttpMessageConverter) httpMessageConverter;
                //设置编码为UTF-8
                messageConverter.setDefaultCharset(Charset.forName(CharsetUtil.UTF_8));
            }
        });
        return restTemplate;
    }


}