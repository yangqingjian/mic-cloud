package cn.mic.cloud.freamework.common.vos;

import lombok.Getter;

/**
 * @Author simon
 * @Description ResultStatusEnum
 * @Date 2020/4/28
 */
public enum ResultStatusEnum {

    /**
     * 成功
     */
    SUCCESS(200, "成功"),
    OBJECT_ALREADY_EXISTS(305, "对象已存在"),
    CALCULATE_EXCEPTION(400, "计算式异常"),
    ACCOUNT_EXCEPTION(417, "认证异常"),
    BUSINESS_EXCEPTION(500, "业务异常"),
    SYSTEM_EXCEPTION(600, "系统异常"),
    REPEAT_REQUEST_EXCEPTION(700, "重复请求异常"),
    TOO_MANY_RESULTS_EXCEPTION(710, "数据库重复异常"),
    BIND_ACCOUNT_EXCEPTION(720, "帐号绑定异常"),
    NO_BIND_ACCOUNT_EXCEPTION(721, "帐号未绑定"),
    CLIENT_EXCEPTION(710, "微服务未发现异常"),
    INVALID_PARAM(300, "无效参数"),
    FEIGN_EXCEPTION(1000,"远程调用异常"),
    AUTHENTICATION_EXCEPTION(401, "认证异常"),
    MYBATIS_SYSTEM_EXCEPTION(711, "数据库查询异常"),
    UNKNOWN_EXCEPTION(800, "未知异常"),
    PROCESS_ENGINE_EXCEPTION(730, "流程异常"),
    RATE_LIMIT_EXCEPTION(429, "流量控制");

    @Getter
    private int code;

    @Getter
    private String message;

    ResultStatusEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
