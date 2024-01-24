package com.common.trace;

import com.common.thread.spi.ExecutorExtension;

import java.util.Map;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2024/1/24 4:53 下午
 * description：
 */
public class ExecutorTraceExtension implements ExecutorExtension {

    @Override
    public void onExecute(String executorName, Runnable task, Map<String, String> context) {
        TraceContext traceContext = TraceContext.getContext();
        context.putAll(traceContext.baggageItemMap());
    }

    @Override
    public void onStart(String executorName, Runnable task, Map<String, String> context) {
        TraceContext traceContext = TraceContext.getContext();
        traceContext.fromBaggageItemMap(context);
    }

    @Override
    public void onFinished(String executorName, Runnable task, Map<String, String> context) {
        TraceContext.clear();
    }

    @Override
    public void onThrow(String executorName, Runnable task, Throwable err, Map<String, String> context) {
        TraceContext.clear();
    }
}
