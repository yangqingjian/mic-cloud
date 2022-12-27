package cn.mic.cloud.security.constants;

/**
 * @author : YangQingJian
 * @date : 2022/12/27
 */
public interface SecurityConstants {

    /**
     * 手机验证码前缀
     */
    String CACHE_SMS_CODE = "sms:code:";

    /**
     * token的有效期
     */
    Integer CACHE_TOKEN_HOURS = 6 ;

    /**
     * 微服务前缀
     */
    String LB_PRE_STR = "lb://";

    /**
     * header或者request的授权参数名称
     */
    String TOKEN_PARAM = "Authorization";

}
