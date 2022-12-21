package cn.mic.cloud.freamework.common.vos;

import lombok.Getter;

import java.io.Serializable;


/**
 * @Author simon
 * @Description R
 * @Date 2020/4/28
 */
@Getter
public class R<T> implements Serializable {
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

    private R(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private R(int code, String message, String errorPath) {
        this.code = code;
        this.message = message;
        this.errorPath = errorPath;
    }

    private R(int code, String message, String errorPath, String errorSystem) {
        this.code = code;
        this.message = message;
        this.errorPath = errorPath;
        this.errorSystem = errorSystem;
    }

    public static R ok() {
        return new R<>(ResultStatusEnum.SUCCESS.getCode(), ResultStatusEnum.SUCCESS.getMessage());
    }

    public static <T> R<T> ok(T data) {
        R<T> r = new R<>(ResultStatusEnum.SUCCESS.getCode(), ResultStatusEnum.SUCCESS.getMessage());
        r.put(data);
        return r;
    }

    public static <T> R<T> error(int code, String message) {
        R<T> r = new R<>(code, message);
        return r;
    }

    public static <T> R<T> error(int code, String message, String errorPath) {
        R<T> r = new R<>(code, message, errorPath);
        return r;
    }

    public static <T> R<T> error(int code, String message, String errorPath, String errorSystem) {
        R<T> r = new R<>(code, message, errorPath, errorSystem);
        return r;
    }

    public R<T> put(T data) {
        this.data = data;
        return this;
    }

    public R<T> msg(String msg) {
        this.message = msg;
        return this;
    }


}
