package cn.mic.cloud.security.core;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.mic.cloud.freamework.common.exception.SystemException;
import cn.mic.cloud.freamework.common.vos.Result;
import cn.mic.cloud.security.config.SecurityCommonConfig;
import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static cn.mic.cloud.freamework.common.constants.SecurityConstants.LB_PRE_STR;


/**
 * @author : YangQingJian
 * @date : 2022/12/27
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class HttpSecurityUtils {

    private final SecurityCommonConfig securityCommonConfig;

    private final RestTemplate restTemplate;

    public <T> T getRemoteObject(String remoteUrl, Method method, Object body, Class<T> clazz) {
        boolean lbFlag = isLoadBalancedUrl(remoteUrl);
        if (lbFlag) {
            return executeLb(remoteUrl, method, body, clazz);
        }
        HttpRequest httpRequest = HttpUtil.createRequest(method, remoteUrl)
                .setConnectionTimeout(securityCommonConfig.getConnectTimeOut())
                .setReadTimeout(securityCommonConfig.getReadTimeOut());
        String requestBodyStr = getRequestBodyStr(method, body);
        if (StrUtil.isNotBlank(requestBodyStr)) {
            httpRequest = httpRequest.body(requestBodyStr);
        }
        HttpResponse httpResponse = httpRequest.execute();
        if (!httpResponse.isOk()) {
            log.error("获取用户信息失败，url = {} , response = {}", remoteUrl, httpResponse.body());
            throw new SystemException(getErrorMessage(httpResponse.body()));
        }
        return JSON.parseObject(httpResponse.body(), clazz);
    }

    private String getErrorMessage(String httpResponseBody) {
        String errorMessage = "获取用户信息失败";
        if (StrUtil.isBlank(httpResponseBody)) {
            return errorMessage;
        }
        Result result = JSON.parseObject(httpResponseBody, Result.class);
        if (null != result && StrUtil.isNotBlank(result.getMessage())) {
            errorMessage = result.getMessage();
        }
        return errorMessage;
    }

    private String getRequestBodyStr(Method method, Object body) {
        if (null == body || ObjectUtil.notEqual(method, Method.POST)) {
            return null;
        }
        if (body instanceof String) {
            return (String) body;
        }
        return JSON.toJSONString(body);
    }

    private <T> T executeLb(String remoteUrl, Method method, Object body, Class<T> clazz) {
        remoteUrl = remoteUrl.replace(LB_PRE_STR, "http://");
        ResponseEntity<String> responseEntity;
        switch (method) {
            case GET:
                responseEntity = restTemplate.getForEntity(remoteUrl, String.class);
                break;
            case POST:
                responseEntity = restTemplate.postForEntity(remoteUrl, body, String.class);
                break;
            default:
                throw new SystemException("请求方式【%s】不对", method.name());
        }
        if (ObjectUtil.notEqual(responseEntity.getStatusCode(), HttpStatus.OK)) {
            log.error("获取用户信息失败，url = {} ", remoteUrl);
            throw new SystemException(getErrorMessage(responseEntity));
        }
        return JSON.parseObject(responseEntity.getBody(), clazz);
    }

    private String getErrorMessage(ResponseEntity<String> responseEntity) {
        return getErrorMessage(responseEntity.getBody());
    }

    private boolean isLoadBalancedUrl(String remoteUrl) {
        if (remoteUrl.startsWith(LB_PRE_STR)) {
            return true;
        }
        return false;
    }


}
