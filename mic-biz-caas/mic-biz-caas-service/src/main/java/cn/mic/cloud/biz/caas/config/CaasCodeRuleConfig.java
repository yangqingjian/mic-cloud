package cn.mic.cloud.biz.caas.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author 杨青见
 * @date 2020-06-02
 * 编码生成策略的规则参数配置
 */
@ConfigurationProperties(prefix = "caas.code.rule")
@Component
@Data
@RefreshScope
public class CaasCodeRuleConfig {

    /**
     * 重试次数
     */
    private Integer generateCodeRetryTimes = 1;

    /**
     * 不用校验长度的编码
     */
    private List<String> noNeedCheckLengthCodeList = Lists.newArrayList();

    /**
     * key: 数据库识别的日期格式
     * value: java识别的日期格式
     */
    private Map<String, String> dataBase2JavaMap = Maps.newHashMap();

    /**
     * 重复校验编码的list长度
     */
    private Integer dataCacheRepeatListSize = 1000;


}
