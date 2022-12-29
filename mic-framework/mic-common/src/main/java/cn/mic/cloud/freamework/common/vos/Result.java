package cn.mic.cloud.freamework.common.vos;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.MDC;

import java.io.Serializable;


/**
 * @Author simon
 * @Description R
 * @Date 2020/4/28
 */
@Getter
@Setter
public class Result<T> implements Serializable {

    private static final String REQUEST_ID = "request_id";

    private static final long serialVersionUID = 1L;
    /**
     * 错误代码
     */
    private int code;
    /**
     * 消息提示
     */
    private String message;
    /**
     * 请求ID
     */
    private String requestId;
    /**
     * 数据
     */
    private T data;

    /**
     * 最底层抛出的错误路径（feignException）
     */
    private String errorPath;
    /**
     * 最底层的错误系统
     */
    private String errorSystem;

    /**
     * 默认构造函数
     */
    public Result(){

    }
    /**
     * 增加requestId
     *
     * @param code
     * @param message
     * @param data
     * @param errorPath
     * @param errorSystem
     */
    private Result(int code, String message, T data, String errorPath, String errorSystem) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.errorPath = errorPath;
        this.errorSystem = errorSystem;
        this.requestId = MDC.get(REQUEST_ID);
    }

    public static Result ok() {
        return new Result<>(ResultStatusEnum.SUCCESS.getCode(), ResultStatusEnum.SUCCESS.getMessage(), null, null, null);
    }

    public static <T> Result<T> ok(T data) {
        return new Result<>(ResultStatusEnum.SUCCESS.getCode(), ResultStatusEnum.SUCCESS.getMessage(), data, null, null);
    }

    public static <T> Result<T> error(int code, String message, String errorPath, String errorSystem, T data) {
        return new Result<>(code, message, data, errorPath, errorSystem);
    }
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null, null, null);
    }
    public static <T> Result<T> error(int code, String message , T data) {
        return new Result<>(code, message, data, null, null);
    }

}
