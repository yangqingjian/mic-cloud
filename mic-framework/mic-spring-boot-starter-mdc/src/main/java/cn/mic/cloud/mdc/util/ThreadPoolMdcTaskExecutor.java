package cn.mic.cloud.mdc.util;

import org.slf4j.MDC;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * 线程池需要继承ThreadPoolMdcTaskExecutor
 *
 * @author : YangQingJian
 * @date : 2022/12/6
 */
public class ThreadPoolMdcTaskExecutor extends ThreadPoolTaskExecutor {

    public ThreadPoolMdcTaskExecutor() {
        super();
    }

    @Override
    public void execute(Runnable task) {
        super.execute(MdcRequestIdUtil.wrap(task, MDC.getCopyOfContextMap()));
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return super.submit(MdcRequestIdUtil.wrap(task, MDC.getCopyOfContextMap()));
    }

    @Override
    public Future<?> submit(Runnable task) {
        return super.submit(MdcRequestIdUtil.wrap(task, MDC.getCopyOfContextMap()));
    }

}
