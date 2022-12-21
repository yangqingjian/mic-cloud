package cn.mic.cloud.freamework.common.exception;

public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1558778506589427470L;

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(Object code, Object... params) {
        super(String.format((String) code, params));
    }



}
