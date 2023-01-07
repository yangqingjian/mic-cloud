package cn.mic.cloud.biz.caas.api.basic;

import cn.mic.cloud.biz.caas.domain.basic.CaasRegion;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

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

    /**
     * 根据编码，进行查询，顶层编码为0
     *
     * @param code
     * @return
     * @see cn.mic.cloud.biz.caas.domain.constants.CaasConstants#REGION_TOP_PARENT_CODE
     */
    @GetMapping("/selectByCode/{code}")
    CaasRegion selectByCode(@PathVariable("code") String code);

    /**
     * 根据编码，进行查询，顶层编码为0
     *
     * @param parentCode
     * @return
     * @see cn.mic.cloud.biz.caas.domain.constants.CaasConstants#REGION_TOP_PARENT_CODE
     */
    @GetMapping("/selectByParentCode/{parentCode}")
    List<CaasRegion> selectByParentCode(@PathVariable("parentCode") String parentCode);

}
