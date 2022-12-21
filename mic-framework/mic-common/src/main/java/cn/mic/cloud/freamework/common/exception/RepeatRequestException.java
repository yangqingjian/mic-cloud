package cn.mic.cloud.freamework.common.exception;
/**
 * 
 * <B>中文类名：重复请求异常</B><BR>
 * <B>概要说明：重复请求异常</B><BR>
 * <B>@version：v1.0</B><BR>
 * <B>版本		修改人		备注</B><BR>
 * @author yangqingjian
 * @date   2019年7月2日
 */
public class RepeatRequestException extends RuntimeException {

    private static final long serialVersionUID = 1558778506589427470L;

    public RepeatRequestException(){
        super();
    }

    //自己组装message
    public RepeatRequestException(String message){
        super(message);
    }

    //这个是格式化，如“计算项【%s】设置错误：有重复id[%s]”
    public RepeatRequestException(Object code, Object ... params) {
        super(String.format((String) code, params));
    }
    
    
}
