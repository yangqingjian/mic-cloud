package cn.mic.cloud.biz.caas.api.code;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 编码规则 接口抽象层类
 *
 * @author : YangQingJian
 * @date : 2023/1/7
 */
public interface CaasCodeRuleApi {

    /**
     * 编码生成接口
     *
     * @param code
     * @param bizKey
     * @return
     */
    @PostMapping("/generateCode/{code}")
    String generateCode(@PathVariable("code") String code, @RequestParam("bizKey") String bizKey);


}
