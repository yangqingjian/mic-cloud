package cn.mic.cloud.freamework.common.exception;

/**
 * @author yangqingjian
 * @date 2020-06-15
 * 参数异常
 */
public class InvalidParameterException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidParameterException() {
    }

    public InvalidParameterException(String message) {
        super(message);
    }

    public InvalidParameterException(String parameterName, String message) {
        super(parameterName + message);
    }

    public InvalidParameterException(Object code, Object... params) {
        super(String.format((String) code, params));
    }
}
