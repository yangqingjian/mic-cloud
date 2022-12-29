package cn.mic.cloud.freamework.common.constants;

/**
 * @author : YangQingJian
 * @date : 2022/12/27
 */
public interface SecurityConstants {

    /**
     * 微服务前缀
     */
    String LB_PRE_STR = "lb://";

    /**
     * header或者request的授权参数名称
     */
    String HEADER_TOKEN_PARAM = "Authorization";
    /**
     * 输出的默认类型
     */
    String APPLICATION_JSON_UTF8 = "application/json;charset=UTF-8";

    /**
     * JWT相关常量
     */
    String JWT_USER_NAME = "username";

    String JWT_LOGIN_PASSWORD = "password";

    String JWT_DEPART_POSITION_ID = "departPositionId";

    String JWT_AUTHORITIES = "authorities";

    /**
     * 编码
     */
    String UTF8 = "UTF-8";

    /**
     * token的异常解析的url
     */
    String TOKEN_BAD_EXCEPTION_URL = "/api/auth/tokenBadException";

    /**
     * 放入到request中的attr
     */
    String TOKEN_BAD_EXCEPTION_ATTR = "tokenBadExceptionAtt";


}
