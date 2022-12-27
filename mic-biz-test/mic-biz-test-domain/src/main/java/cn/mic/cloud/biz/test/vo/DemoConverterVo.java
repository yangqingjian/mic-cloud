package cn.mic.cloud.biz.test.vo;

import cn.mic.cloud.biz.test.constants.DemoEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : YangQingJian
 * @date : 2022/12/27
 */
@Data
@NoArgsConstructor
public class DemoConverterVo {

    /**
     * 姓名
     */
    private String name;

    /**
     * 测试枚举
     */
    private DemoEnum demoEnum;


}
