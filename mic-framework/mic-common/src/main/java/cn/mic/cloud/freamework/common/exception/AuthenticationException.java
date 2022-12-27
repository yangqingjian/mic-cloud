package cn.mic.cloud.freamework.common.exception;
/**
 * 认证异常
 * @author yangqingjian
 * @date   2019年7月2日
 */
public class AuthenticationException extends RuntimeException {

    private static final long serialVersionUID = 1558778506589427470L;

    public AuthenticationException(){
        super();
    }

    //自己组装message
    public AuthenticationException(String message){
        super(message);
    }

    public AuthenticationException(Object code, Object ... params) {
        super(String.format((String) code, params));
    }
    
    
}
