package cn.mic.cloud.biz.caas.api.basic;

import org.springframework.web.bind.annotation.PostMapping;

/**
 * 行政区域 接口抽象层类
 *
 * @author : YangQingJian
 * @date : 2023/1/7
 */
public interface CaasRegionApi {

    /**
     * 初始化
     *
     * @return
     */
    @PostMapping("/init")
    String init();

}
