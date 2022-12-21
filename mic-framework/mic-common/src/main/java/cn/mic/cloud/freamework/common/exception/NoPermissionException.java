package cn.mic.cloud.freamework.common.exception;

/**
 * 无权限
 * @author Yorking
 * @date 2021/07/09
 */
public class NoPermissionException extends RuntimeException {

    public NoPermissionException() {
        super();
    }

    public NoPermissionException(String message) {
        super(message);
    }

    public NoPermissionException(Object code, Object... params) {
        super(String.format((String) code, params));
    }

}
