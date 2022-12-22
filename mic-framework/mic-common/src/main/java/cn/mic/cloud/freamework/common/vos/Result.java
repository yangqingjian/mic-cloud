package cn.mic.cloud.freamework.common.vos;

import lombok.Getter;

import java.io.Serializable;


/**
 * @Author simon
 * @Description R
 * @Date 2020/4/28
 */
@Getter
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int code;

    private String message;

    private T data;

    /**
     * 最底层抛出的错误路径（feignException）
     */
    private String errorPath;
    /**
     * 最底层的错误系统
     */
    private String errorSystem;

    private Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private Result(int code, String message, String errorPath) {
        this.code = code;
        this.message = message;
        this.errorPath = errorPath;
    }

    private Result(int code, String message, String errorPath, String errorSystem) {
        this.code = code;
        this.message = message;
        this.errorPath = errorPath;
        this.errorSystem = errorSystem;
    }

    public static Result ok() {
        return new Result<>(ResultStatusEnum.SUCCESS.getCode(), ResultStatusEnum.SUCCESS.getMessage());
    }

    public static <T> Result<T> ok(T data) {
        Result<T> result = new Result<>(ResultStatusEnum.SUCCESS.getCode(), ResultStatusEnum.SUCCESS.getMessage());
        result.put(data);
        return result;
    }

    public static <T> Result<T> error(int code, String message) {
        Result<T> result = new Result<>(code, message);
        return result;
    }

    public static <T> Result<T> error(int code, String message, String errorPath) {
        Result<T> result = new Result<>(code, message, errorPath);
        return result;
    }

    public static <T> Result<T> error(int code, String message, String errorPath, String errorSystem) {
        Result<T> result = new Result<>(code, message, errorPath, errorSystem);
        return result;
    }

    public Result<T> put(T data) {
        this.data = data;
        return this;
    }

    public Result<T> msg(String msg) {
        this.message = msg;
        return this;
    }


}
