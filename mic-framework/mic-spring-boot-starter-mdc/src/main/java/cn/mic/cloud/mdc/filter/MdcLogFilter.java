package cn.mic.cloud.mdc.filter;

import cn.hutool.core.util.StrUtil;
import cn.mic.cloud.mdc.util.MdcRequestIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 生成一个uuid
 *
 * @author : YangQingJian
 * @date : 2022/12/6
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@WebFilter(filterName = "MdcLogFilter", urlPatterns = "/*")
@Slf4j
public class MdcLogFilter implements Filter {

    @Override
    public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest reqs = (HttpServletRequest) req;
        String requestId = reqs.getHeader(MdcRequestIdUtil.REQUEST_ID);
        if (StrUtil.isBlank(requestId)) {
            requestId = MdcRequestIdUtil.getRequestId();
        }
        MDC.put(MdcRequestIdUtil.REQUEST_ID, requestId);
        log.debug("MdcLogFilter触发request_id:{}", MDC.get(MdcRequestIdUtil.REQUEST_ID));
        try {
            chain.doFilter(req, res);
        } finally {
            MDC.remove(MdcRequestIdUtil.REQUEST_ID);
        }
    }

    @Override
    public void init(final FilterConfig filterConfig) {
        log.debug("MdcLogFilter初始化");
    }

    @Override
    public void destroy() {
        MDC.remove(MdcRequestIdUtil.REQUEST_ID);
    }


}
