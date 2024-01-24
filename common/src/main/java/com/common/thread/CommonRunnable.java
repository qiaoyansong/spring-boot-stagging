package com.common.thread;

import com.common.trace.TraceContext;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2024/1/24 5:10 下午
 * description：
 */
public class CommonRunnable implements Runnable {

    private Runnable targetRunnable;

    private volatile Throwable throwable;

    private String traceId;

    public CommonRunnable(Runnable targetRunnable) {
        this.targetRunnable = targetRunnable;
        this.traceId = TraceContext.getContext().getTraceId();
    }

    @Override
    public void run() {
        if(targetRunnable == null) {
            return;
        }

        /**
         * 注意FutureTask默认是捕获异常的，并保存起来）java.util.concurrent.FutureTask#run()
         */
        try {
            targetRunnable.run();
        } catch (Throwable throwable) {
            this.throwable = throwable;
        }
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
