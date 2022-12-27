package cn.mic.cloud.security.core;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.mic.cloud.freamework.common.core.LoginUser;
import cn.mic.cloud.freamework.common.exception.InvalidParameterException;
import cn.mic.cloud.freamework.common.exception.RepeatRequestException;
import cn.mic.cloud.freamework.common.exception.SystemException;
import cn.mic.cloud.security.config.SecurityCommonConfig;
import cn.mic.cloud.security.constants.SecurityConstants;
import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static cn.mic.cloud.security.constants.SecurityConstants.LB_PRE_STR;

/**
 * @author : YangQingJian
 * @date : 2022/12/27
 */
@Component
@Slf4j
public class HttpSecurityUtils {

    @Resource
    private  SecurityCommonConfig securityCommonConfig;

    @Resource
    private  RestTemplate restTemplate;

    public LoginUser remoteGetLoginUser(String remoteUrl, Method method){
        boolean lbFlag = isLoadBalancedUrl(remoteUrl);
        if (lbFlag){
            return executeLb(remoteUrl , method);
        }
        HttpResponse httpResponse = HttpUtil.createRequest(method , remoteUrl)
                .setConnectionTimeout(securityCommonConfig.getConnectTimeOut())
                .setReadTimeout(securityCommonConfig.getReadTimeOut())
                .execute();
        if (!httpResponse.isOk()){
            log.error("获取用户信息失败，url = {} , response = {}" , remoteUrl , httpResponse.body());
            throw new SystemException("获取登录用户信息失败");
        }
        return JSON.parseObject(httpResponse.body() ,LoginUser.class);
    }

    private LoginUser executeLb(String remoteUrl, Method method){
        remoteUrl = remoteUrl.replace(LB_PRE_STR , "http://");
        ResponseEntity<String> responseEntity;
        switch (method){
            case GET:
                responseEntity = restTemplate.getForEntity(remoteUrl,String.class);
                break;
            case POST:
                responseEntity = restTemplate.postForEntity(remoteUrl,null , String.class);
                break;
            default:
                throw new SystemException("请求方式【%s】不对" , method.name());
        }
        if (null == responseEntity){
            log.error("获取用户信息失败，url = {} " , remoteUrl);
            throw new SystemException("获取登录用户信息失败");
        }
        return JSON.parseObject(responseEntity.getBody(),LoginUser.class) ;
    }

    private boolean isLoadBalancedUrl(String remoteUrl){
        if (remoteUrl.startsWith(LB_PRE_STR)){
            return true;
        }
        return false;
    }


    /**
     * 从 request 的 header 中获取 Authorization
     *
     * @param request 请求
     * @return JWT
     */
    public static String getAuthorization(HttpServletRequest request) {
        String bearerToken = request.getHeader(SecurityConstants.TOKEN_PARAM);
        return StrUtil.isBlank(bearerToken) ? request.getParameter(SecurityConstants.TOKEN_PARAM) : bearerToken;
    }


}
