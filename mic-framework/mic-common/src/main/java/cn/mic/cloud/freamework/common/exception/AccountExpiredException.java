package cn.mic.cloud.freamework.common.exception;
/**
 * 
 * <B>中文类名：帐户过期异常</B><BR>
 * <B>概要说明：帐户过期异常</B><BR>
 * <B>@version：v1.0</B><BR>
 * <B>版本		修改人		备注</B><BR>
 * @author yangqingjian
 * @date   2019年7月2日
 */
public class AccountExpiredException extends RuntimeException {

    private static final long serialVersionUID = 1558778506589427470L;

    public AccountExpiredException(){
        super();
    }

    //自己组装message
    public AccountExpiredException(String message){
        super(message);
    }

    public AccountExpiredException(Object code, Object ... params) {
        super(String.format((String) code, params));
    }
    
    
}
