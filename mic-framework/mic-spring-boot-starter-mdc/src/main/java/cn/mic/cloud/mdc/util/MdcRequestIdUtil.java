package cn.mic.cloud.mdc.util;

import org.slf4j.MDC;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * mdc工具类
 *
 * @author : YangQingJian
 * @date : 2022/12/6
 */
public class MdcRequestIdUtil {

    /**
     * 字段的名称
     */
    public static final String REQUEST_ID = "request_id";

    /**
     * 获取唯一性标识
     *
     * @return
     */
    public static String getRequestId() {
        return UUID.randomUUID().toString();
    }

    public static void setRequestIdIfAbsent() {
        if (MDC.get(REQUEST_ID) == null) {
            MDC.put(REQUEST_ID, getRequestId());
        }
    }

    /**
     * 用于父线程向线程池中提交任务时，将自身MDC中的数据复制给子线程
     *
     * @param callable
     * @param context
     * @param <T>
     * @return
     */
    public static <T> Callable<T> wrap(final Callable<T> callable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setRequestIdIfAbsent();
            try {
                return callable.call();
            } finally {
                MDC.clear();
            }
        };
    }

    /**
     * 用于父线程向线程池中提交任务时，将自身MDC中的数据复制给子线程
     *
     * @param runnable
     * @param context
     * @return
     */
    public static Runnable wrap(final Runnable runnable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setRequestIdIfAbsent();
            try {
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }

}
