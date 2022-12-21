package cn.mic.cloud.freamework.common.exception;

/**
 * 计算式异常
 *
 * @author yangqingjian
 * @date 2020-10-16
 */
public class CalculateException extends RuntimeException {

    private static final Long serialVersionUID = 1L;

    public CalculateException() {
        super();
    }

    public CalculateException(String message) {
        super(message);
    }

    public CalculateException(Object code, Object... params) {
        super(String.format((String) code, params));
    }

}
