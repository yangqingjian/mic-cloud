package cn.mic.cloud.biz.caas.web.controller.basic;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.mic.cloud.biz.caas.domain.basic.CaasRegion;
import cn.mic.cloud.biz.caas.feign.basic.CaasRegionFeign;
import cn.mic.cloud.common.web.core.AbstractBaseEntityController;
import cn.mic.cloud.freamework.common.vos.Result;
import com.alibaba.nacos.common.http.client.request.HttpClientRequest;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 行政区域 web接口
 *
 * @author : YangQingJian
 * @date : 2023/1/7
 */
@RestController
@RequestMapping("/caasRegion")
@Api(tags = "行政区域")
@Slf4j
public class CaasRegionController extends AbstractBaseEntityController<CaasRegion> {

    @Autowired
    private CaasRegionFeign caasRegionFeign;

    @GetMapping("/getCurrentIpRegion")
    public Result<String> getCurrentIpRegion(@RequestParam(name = "ip", required = false) String ip) {
        if (StrUtil.isBlank(ip)) {
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            ip = ServletUtil.getClientIP(servletRequestAttributes.getRequest());
        }
        return Result.ok(caasRegionFeign.getCurrentIpRegion(ip));
    }


}
