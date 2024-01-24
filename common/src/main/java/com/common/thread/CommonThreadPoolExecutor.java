package com.common.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2024/1/24 4:07 下午
 * description：
 */
public class CommonThreadPoolExecutor extends ThreadPoolExecutor {

    /**
     * 线程池运行日志类
     */
    private static final Logger runLogger = LoggerFactory.getLogger("treadPoolRunLogger");

    /**
     * 线程统计日志类
     */
    private static final Logger staLogger = LoggerFactory.getLogger("treadPoolStaLogger");

    /**
     * 线程变量，用于保存线程开始执行的时间
     */
    private final ThreadLocal<Long> startTime;

    /**
     * 线程名称
     */
    private ThreadLocal<String> threadName;

    /**
     * 线程池名称
     */
    private String threadPoolName;

    /**
     * 线程已执行的任务数
     */
    private long executedTaskCount;

    /**
     * 线程池统计任务
     */
    private ScheduledExecutorService scheduledExecutorService;

    /**
     * newTaskFor为threadPoolExecutor submit时候调用
     */
//    @Override
//    protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
//        return new CommonFutureTask(runnable, value);
//    }

    /**
     * newTaskFor为threadPoolExecutor submit时候调用
     */
//    @Override
//    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
//        return new CommonFutureTask(callable);
//    }

    public CommonThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, new ThreadFactoryBuilder("common-pool-thread"), new AbortPolicy());
    }

    public CommonThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, new ThreadFactoryBuilder("common-pool-thread"), handler);
    }

    public CommonThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactoryBuilder threadFactoryBuilder) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactoryBuilder, new AbortPolicy());
    }

    public CommonThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactoryBuilder threadFactoryBuilder, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactoryBuilder.build(), handler);
        this.startTime = new ThreadLocal();
        this.threadName = new ThreadLocal();
        this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(threadFactoryBuilder.build());
        this.scheduledExecutorService.scheduleWithFixedDelay(new CommonThreadPoolExecutor.ThreadPoolStatistics(), 5L, 60L, TimeUnit.SECONDS);
        this.executedTaskCount = 0L;
        this.threadPoolName = threadFactoryBuilder.getName();
    }

    /**
     * TracerRunnable负责处理 放threadLocal快照 && 清除线程池执行线程threadLocal
     */
    @Override
    public void execute(Runnable command) {
        if (command == null) {
            throw new NullPointerException();
        } else {
            CommonRunnable run = new CommonRunnable(new TracerRunnable((String) Optional.ofNullable(this.threadPoolName).orElse("default"), command));
            super.execute(run);
        }
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        startTime.set(System.currentTimeMillis());
        String treadName = t.getName();
        threadName.set(treadName);
        super.beforeExecute(t, r);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        int result = 0;
        if (hasException(t, r)) {
            result = 1;
        }
        long useTime = System.currentTimeMillis() - startTime.get();

        if (useTime > 0) {
            StringBuffer sb = new StringBuffer();
            sb.append(threadName.get());
            sb.append(",");
            sb.append(result);
            sb.append(",");
            sb.append(useTime);

            runLogger.info(sb.toString());
        }
        startTime.remove();
    }

    @Override
    public void shutdown() {
        try {
            scheduledExecutorService.shutdown();
        } catch (Exception e) {
            runLogger.error("关闭CommonThreadPoolExecutor内部scheduledExecutorService异常：" + e.getMessage(), e);
        }

        super.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        try {
            scheduledExecutorService.shutdownNow();
        } catch (Exception e) {
            runLogger.error("关闭CommonThreadPoolExecutor内部scheduledExecutorService异常：" + e.getMessage(), e);
        }

        return super.shutdownNow();
    }

    /**
     * 判断该Runnable是否有异常
     *
     * @param t
     * @param r
     * @return
     */
    private boolean hasException(Throwable t, Runnable r) {
        if (t != null) {
            return true;
        }
        if (r instanceof CommonRunnable) {
            return ((CommonRunnable) r).getThrowable() != null;
        } else if (r instanceof FutureTask) {
            try {
                ((Future<?>) r).get();
            } catch (InterruptedException ce) {
                Thread.currentThread().interrupt();
            } catch (Exception ee) {
                return true;
            }
        }
        return false;
    }

    class ThreadPoolStatistics implements Runnable {
        public ThreadPoolStatistics() {
        }

        @Override
        public void run() {
            int maxPoolSize = CommonThreadPoolExecutor.this.getMaximumPoolSize();
            int corePoolSize = CommonThreadPoolExecutor.this.getCorePoolSize();
            int activeCount = CommonThreadPoolExecutor.this.getActiveCount();
            int idleCount = CommonThreadPoolExecutor.this.getPoolSize() - activeCount;
            long lastPeroidProcTotalTaskNum = CommonThreadPoolExecutor.this.executedTaskCount;
            long curProcTotalTaskNum = CommonThreadPoolExecutor.this.getCompletedTaskCount();
            long curProcTaskNum = curProcTotalTaskNum - lastPeroidProcTotalTaskNum;
            long toBeExecuteTaskNum = CommonThreadPoolExecutor.this.getTaskCount() - curProcTotalTaskNum;
            CommonThreadPoolExecutor.this.executedTaskCount = curProcTotalTaskNum;
            StringBuffer sb = new StringBuffer();
            sb.append(CommonThreadPoolExecutor.this.threadPoolName);
            sb.append(",");
            sb.append(maxPoolSize);
            sb.append(",");
            sb.append(corePoolSize);
            sb.append(",");
            sb.append(activeCount);
            sb.append(",");
            sb.append(idleCount);
            sb.append(",");
            sb.append(curProcTaskNum);
            sb.append(",");
            sb.append(toBeExecuteTaskNum);
            sb.append(",");
            sb.append(CommonThreadPoolExecutor.this.getQueue().size());
            CommonThreadPoolExecutor.staLogger.info(sb.toString());
        }
    }
}
