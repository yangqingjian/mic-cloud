package cn.mic.cloud.web.exception;

import cn.hutool.core.util.StrUtil;
import cn.mic.cloud.freamework.common.exception.AuthenticationException;
import cn.mic.cloud.freamework.common.exception.BusinessException;
import cn.mic.cloud.freamework.common.exception.CalculateException;
import cn.mic.cloud.freamework.common.exception.RepeatRequestException;
import cn.mic.cloud.freamework.common.vos.Result;
import cn.mic.cloud.freamework.common.vos.ResultStatusEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.netflix.client.ClientException;
import feign.FeignException;
import feign.Request;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.omg.CORBA.SystemException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.security.auth.login.AccountExpiredException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Optional;

/**
 *
 */
@ControllerAdvice
@Configuration
@Slf4j
public class CommonExceptionHandler {
    /**
     * 服务模块
     */
    @Value("${pom.description:未知系统}")
    private String currentSystem;

    private static final String CODE_FIELD = "code";
    private static final String MESSAGE_FIELD = "message";
    private static final String ERROR_PATH_FIELD = "errorPath";
    private static final String ERROR_SYSTEM_FIELD = "errorSystem";
    private static final String CLIENT_EXCEPTION_START_WITH_STRING = "com.netflix.client.ClientException: Load balancer does not have available server for client: ";

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Result> exceptionHandler(MethodArgumentNotValidException e) {
        Result result = constructExceptionByCode(e, ResultStatusEnum.INVALID_PARAM);
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Result> handleFeignException(FeignException e) {
        Result result = constructExceptionByCode(e, ResultStatusEnum.FEIGN_EXCEPTION);
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CalculateException.class)
    public ResponseEntity<Result> handleCalulateException(CalculateException e) {
        Result result = constructExceptionByCode(e, ResultStatusEnum.CALCULATE_EXCEPTION);
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(TooManyResultsException.class)
    public ResponseEntity<Result> handleTooManyResultsException(TooManyResultsException e) {
        Result result = constructExceptionByCode(e, ResultStatusEnum.TOO_MANY_RESULTS_EXCEPTION);
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    /**
     * 针对Exception需要再次处理一下
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result> handleException(Exception e) {
        Result result = null;
        if (null != e.getCause() && null != e.getCause().getClass() && e.getCause().getClass().equals(ClientException.class)) {
            /**
             * 主动设置异常
             */
            String errorService = e.getMessage().replaceAll(CLIENT_EXCEPTION_START_WITH_STRING, "");
            errorService = "未发现的微服务【" + errorService + "】";
            e = new Exception(errorService);
            result = constructExceptionByCode(e, ResultStatusEnum.CLIENT_EXCEPTION);
        } else {
            result = constructExceptionByCode(e, ResultStatusEnum.UNKNOWN_EXCEPTION);
        }
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    /**
     * 类型转换错误
     *
     * @param e
     * @return
     */
    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<Result> handleInvalidFormatException(InvalidFormatException e) {
        Result result = constructExceptionByCode(e, ResultStatusEnum.INVALID_FORMAT_EXCEPTION);
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    /**
     * 认证异常
     * @param e
     * @return
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Result> handleAuthenticationException(AuthenticationException e) {
        Result result = constructExceptionByCode(e, ResultStatusEnum.AUTHENTICATION_EXCEPTION);
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Result> handleBusinessException(Exception e) {
        Result result = constructExceptionByCode(e, ResultStatusEnum.BUSINESS_EXCEPTION);
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    /**
     * 处理参数异常：IllegalArgumentException
     *
     * @param e
     * @return
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Result> handleIllegalArgumentException(Exception e) {
        Result result = constructExceptionByCode(e, ResultStatusEnum.INVALID_PARAM);
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<Result> handleInvalidParameterException(Exception e) {
        Result result = constructExceptionByCode(e, ResultStatusEnum.INVALID_PARAM);
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SystemException.class)
    public ResponseEntity<Result> handleSystemException(Exception e) {
        Result result = constructExceptionByCode(e, ResultStatusEnum.SYSTEM_EXCEPTION);
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ClientException.class)
    public ResponseEntity<Result> handleSystemException(ClientException e) {
        Result result = constructExceptionByCode(e, ResultStatusEnum.CLIENT_EXCEPTION);
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RepeatRequestException.class)
    public ResponseEntity<Result> handleRepeatRequestException(Exception e) {
        Result result = constructExceptionByCode(e, ResultStatusEnum.REPEAT_REQUEST_EXCEPTION);
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    /*
    @ExceptionHandler(AccountExpiredException.class)
    public ResponseEntity<Result> handleAccountExpiredException(Exception e) {
        Result result = constructExceptionByCode(e, ResultStatusEnum.ACCOUNT_EXPIRED_EXCEPTION);
        return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<Result> handleDisabledException(Exception e) {
        Result result = constructExceptionByCode(e, ResultStatusEnum.DISABLED_EXCEPTION);
        return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(LockedException.class)
    public ResponseEntity<Result> handleLockedException(Exception e) {
        Result result = constructExceptionByCode(e, ResultStatusEnum.LOCKED_EXCEPTION);
        return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(CredentialsExpiredException.class)
    public ResponseEntity<Result> handleCredentialsExpiredException(Exception e) {
        Result result = constructExceptionByCode(e, ResultStatusEnum.CREDENTIALS_EXPIRED_EXCEPTION);
        return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Result> handleUsernameNotFoundException(Exception e) {
        Result result = constructExceptionByCode(e, ResultStatusEnum.USERNAME_NOT_FOUND_EXCEPTION);
        return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Result> handleBadCredentialsException(Exception e) {
        Result result = constructExceptionByCode(e, ResultStatusEnum.AUTHENTICATION_EXCEPTION);
        return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
    }
    */

    protected Result constructExceptionByCode(Exception e, ResultStatusEnum statusEnum) {
        e = (Exception) recursionException(e);
        /**
         * 调用的path以及调用的系统
         */
        String errorPath = null;
        String errorSystem = currentSystem;
        String stackTrace = getStackTrace(e);
        log.error(stackTrace);
        String message = null != e.getMessage() ? e.getMessage() : "";
        /**
         * 处理校验异常
         */
        if (e instanceof MethodArgumentNotValidException) {
            BindingResult bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
            StringBuilder sb = new StringBuilder();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                sb.append(fieldError.getDefaultMessage()).append(";");
            }
            sb.deleteCharAt(sb.lastIndexOf(";"));
            message = sb.toString();
        }
        /**
         * 处理远程调用异常
         */
        if (e instanceof FeignException) {
            int errorCode = statusEnum.getCode();
            FeignException feignException = (FeignException) e;
            Request request = feignException.request();
            /**
             * 只记录最底层的错误信息
             */
            errorPath = request.url();
            Optional<ByteBuffer> optionByteBuffer = feignException.responseBody();
            if (optionByteBuffer.isPresent()) {
                try {
                    /**
                     * 最底层的错误信息
                     */
                    String feignExceptionMessage = getString(optionByteBuffer.get());
                    HashMap map = (new ObjectMapper()).readValue(feignExceptionMessage, HashMap.class);
                    message = (String) map.get(MESSAGE_FIELD);
                    errorCode = (int) map.get(CODE_FIELD);
                    /**
                     * 注意处理errorPath
                     */
                    String tempErrorPath = (String) map.get(ERROR_PATH_FIELD);
                    if (StrUtil.isNotBlank(tempErrorPath)) {
                        errorPath = tempErrorPath;
                    }
                    /**
                     * 注意处理errorSystem
                     */
                    String tempErrorSystem = (String) map.get(ERROR_SYSTEM_FIELD);
                    if (StrUtil.isNotBlank(tempErrorSystem)) {
                        errorSystem = tempErrorSystem;
                    } else {
                        /**
                         * 处理message(需要加上系统)
                         */
                        message = "【" + errorSystem + "】" + message;
                    }
                } catch (Exception ex) {
                    log.error("解析远程调用时异常：{}", ex.getMessage());
                }
            } else {
                message = "远程调用异常，接口【" + errorPath + "】不可用";
            }
            return Result.error(errorCode, message, errorPath, errorSystem, stackTrace);
        }
        if (StrUtil.isBlank(message)) {
            message = e.getClass().getName();
        }
        /**
         * 组装message
         */
        //message = "【" + errorSystem + "】" + statusEnum.getMessage() + ":" + message;
        message = "【"+statusEnum.getMessage()+ "】" + message;
        /**
         * 处理message(针对如下异常，只显示)
         */
        /*
        switch (statusEnum) {
            case TOO_MANY_RESULTS_EXCEPTION:
            case MYBATIS_SYSTEM_EXCEPTION:
//            case FEIGN_EXCEPTION:
            case UNKNOWN_EXCEPTION:
            case SYSTEM_EXCEPTION:
                message = statusEnum.getMessage();
                break;
        }
        */
        return Result.error(statusEnum.getCode(), message, null, errorSystem, stackTrace);
    }

    /**
     * 递归
     */
    private Throwable recursionException(Throwable cause) {
        if (cause.getCause() == null) {
            return cause;
        }
        return recursionException(cause.getCause());
    }

    /**
     * ByteBuffer 转换 String
     *
     * @param buffer
     * @return
     */
    public static String getString(ByteBuffer buffer) {
        return StandardCharsets.UTF_8.decode(buffer).toString();
    }

    private String getStackTrace(Throwable t) {
        if (null == t) {
            return "";
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            t.printStackTrace(pw);
            return sw.toString();
        } finally {
            pw.close();
            try {
                sw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
