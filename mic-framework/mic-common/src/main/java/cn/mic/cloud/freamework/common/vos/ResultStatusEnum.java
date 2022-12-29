package cn.mic.cloud.freamework.common.vos;

import lombok.Getter;

/**
 * @Author simon
 * @Description ResultStatusEnum
 * @Date 2020/4/28
 */
public enum ResultStatusEnum {

    SUCCESS(200, "成功"),
    BUSINESS_EXCEPTION(601, "业务异常"),
    SYSTEM_EXCEPTION(602, "系统异常"),
    INVALID_PARAM(603, "无效参数"),
    REPEAT_REQUEST_EXCEPTION(604, "重复请求异常"),
    INVALID_FORMAT_EXCEPTION(605, "类型转换异常"),
    TOO_MANY_RESULTS_EXCEPTION(701, "数据库重复异常"),
    CLIENT_EXCEPTION(800, "微服务未发现异常"),
    FEIGN_EXCEPTION(801, "远程调用异常"),
    AUTHENTICATION_EXCEPTION(401, "认证异常"),
    TOKEN_EXPIRE_EXCEPTION(801, "token过期"),
    UNKNOWN_EXCEPTION(500, "");//未知异常直接message为空

    @Getter
    private int code;

    @Getter
    private String message;

    ResultStatusEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
