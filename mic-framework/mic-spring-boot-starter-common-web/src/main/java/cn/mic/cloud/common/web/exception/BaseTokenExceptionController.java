package cn.mic.cloud.common.web.exception;

import cn.mic.cloud.freamework.common.constants.SecurityConstants;
import cn.mic.cloud.freamework.common.exception.InvalidParameterException;
import cn.mic.cloud.freamework.common.exception.TokenExpireException;
import cn.mic.cloud.freamework.common.utils.SecurityCoreUtils;
import cn.mic.cloud.freamework.common.vos.Result;
import cn.mic.cloud.freamework.common.vos.ResultStatusEnum;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static cn.mic.cloud.freamework.common.constants.SecurityConstants.TOKEN_BAD_EXCEPTION_ATTR;

/**
 * @author : YangQingJian
 * @date : 2022/12/29
 */
public interface BaseTokenExceptionController {

    /**
     * 异常的处理
     *
     * @param request
     */
    @RequestMapping(SecurityConstants.TOKEN_BAD_EXCEPTION_URL)
    default void handleException(HttpServletRequest request) throws Exception {
        Exception exception = (Exception) request.getAttribute(TOKEN_BAD_EXCEPTION_ATTR);
        throw exception;
    }


}
