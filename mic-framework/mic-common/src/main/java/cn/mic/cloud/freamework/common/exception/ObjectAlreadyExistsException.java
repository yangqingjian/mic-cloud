package cn.mic.cloud.freamework.common.exception;

/**
 * 对象已存在异常
 *
 * @author yangqingjian
 * @date 2020-10-11
 */
public class ObjectAlreadyExistsException extends RuntimeException {

    public ObjectAlreadyExistsException() {
        super();
    }

    public ObjectAlreadyExistsException(String message) {
        super(message);
    }

    /**
     * 这个是格式化，如“计算项【%s】设置错误：有重复id[%s]
     *
     * @param code
     * @param params
     */
    public ObjectAlreadyExistsException(Object code, Object... params) {
        super(String.format((String) code, params));
    }

}
