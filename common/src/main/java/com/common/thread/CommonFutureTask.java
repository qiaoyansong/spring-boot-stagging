package com.common.thread;

import com.common.trace.TraceContext;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2024/1/24 4:17 下午
 * description：不使用TracerRunnable自己维护threadLocal demo
 */
public class CommonFutureTask<V> extends FutureTask<V> {

    private String traceId;

    public CommonFutureTask(Runnable runnable, V result) {
        super(runnable, result);
    }

    public CommonFutureTask(Callable<V> callable) {
        super(callable);
        // 填充threadLocal信息 防止多线程threadLocal丢失
        this.traceId = TraceContext.getContext().getTraceId();
    }

    @Override
    public void run() {
        // 将threadLocal快照 放入
        super.run();
        // 将当前threadLocal 清除
    }

}
