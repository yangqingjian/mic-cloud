package cn.mic.cloud.freamework.common.exception;

/**
 * token过期
 *
 * @author yangqingjian
 * @date 2019年7月2日
 */
public class TokenExpireException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public TokenExpireException() {
        super();
    }

    //自己组装message
    public TokenExpireException(String message) {
        super(message);
    }

    public TokenExpireException(Object code, Object... params) {
        super(String.format((String) code, params));
    }


}
