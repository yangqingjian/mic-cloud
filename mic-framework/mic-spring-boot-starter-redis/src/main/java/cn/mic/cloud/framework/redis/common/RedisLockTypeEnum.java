package cn.mic.cloud.framework.redis.common;

import cn.mic.cloud.freamework.common.constants.CommonEnum;

/**
 * @author : YangQingJian
 * @date : 2022/12/21
 */
public enum RedisLockTypeEnum implements CommonEnum {
    /**
     * 自定义 key 前缀
     */
    ONE("biz:one", "业务1"),
    TWO("biz:two", "业务2"),
    TEST("biz:test", "测试");

    private String code;
    private String desc;

    RedisLockTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
