package com.common.thread.spi;

import java.util.Map;
import java.util.concurrent.Executor;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2024/1/24 4:52 下午
 * description：
 */
public interface ExecutorExtension {
    /**
     * 执行  {@link Executor#execute(Runnable)}
     *
     * @param executorName 执行名字
     * @param task         执行任务
     * @param context      执行上下文
     */
    void onExecute(String executorName, Runnable task, Map<String, String> context);

    /**
     * 开始执行  {@link Runnable#run()}
     *
     * @param executorName 执行名字
     * @param task         执行任务
     * @param context      执行上下文
     */
    void onStart(String executorName, Runnable task, Map<String, String> context);

    /**
     * {@link Runnable#run()} 执行结束并return
     *
     * @param executorName 执行名字
     * @param task         执行任务
     * @param context      执行上下文
     */
    void onFinished(String executorName, Runnable task, Map<String, String> context);

    /**
     * {@link Runnable#run()} 执行抛出异常
     *
     * @param executorName 执行名字
     * @param task         执行任务
     * @param context      执行上下文
     */
    void onThrow(String executorName, Runnable task, Throwable err, Map<String, String> context);
}
