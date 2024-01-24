package com.common.thread;

import com.common.thread.spi.ExecutorExtension;
import com.common.utils.ServiceProviders;
import org.elasticsearch.common.collect.CopyOnWriteHashMap;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2024/1/24 4:50 下午
 * description：
 */
public class TracerRunnable implements Runnable {

    private static final List<ExecutorExtension> INTERCEPTORS = ServiceProviders.loadFromSpi(ExecutorExtension.class);

    private final Runnable delegate;

    private final String executorName;

    private final Map<String, String> context = new CopyOnWriteHashMap<>();

    public TracerRunnable(String executorName, Runnable delegate) {
        this.executorName = executorName;
        this.delegate = delegate;
        INTERCEPTORS.forEach(interceptor -> interceptor.onExecute(executorName, delegate, context));
    }

    @Override
    public void run() {
        Map<String, String> asyncContext = Collections.unmodifiableMap(context);
        INTERCEPTORS.forEach(interceptor -> interceptor.onStart(executorName, delegate, asyncContext));
        try {
            delegate.run();
            INTERCEPTORS.forEach(interceptor -> interceptor.onFinished(executorName, delegate, asyncContext));
        } catch (Throwable err) {
            INTERCEPTORS.forEach(interceptor -> interceptor.onThrow(executorName, delegate, err, asyncContext));
            throw err;
        }
    }
}
