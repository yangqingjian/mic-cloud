package cn.mic.cloud.biz.test.constants;

import cn.mic.cloud.freamework.common.constants.CommonEnum;
import lombok.Getter;

/**
 * @author : YangQingJian
 * @date : 2022/12/27
 */
public enum DemoEnum implements CommonEnum {


    FIRST("first", "第一名"),
    SECOND("second", "第二名");

    @Getter
    private String code;
    @Getter
    private String desc;

    DemoEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


}
