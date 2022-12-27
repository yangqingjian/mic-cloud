package cn.mic.cloud.freamework.common.constants;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 枚举基类
 *
 * @author yangqingjian
 * @date 2022/12/21
 */
public interface CommonEnum {

    @JsonValue
    String getCode();

    String getDesc();

}
