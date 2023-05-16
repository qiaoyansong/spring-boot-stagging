package com.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.function.Function;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 5/16/23 2:02 下午
 * description：
 * Java 8 的 CompletableFuture 并没有 timeout 机制，虽然可以在 get 的时候指定 timeout，但是我们知道get 是一个同步堵塞的操作。
 * 怎样让 timeout 也是异步的呢？Java 8 内有内建的机制支持，一般的实现方案是启动一个 ScheduledThreadpoolExecutor 线程在 timeout
 * 时间后直接调用 CompletableFuture.completeExceptionally(new TimeoutException())，然后用 acceptEither() 或者 applyToEither 看是先计算完成还是先超时。
 * 参考地址 https://www.qyyshop.com/info/602361.html
 */
public class CompletableFutureUtils {

    public static final Logger log = LoggerFactory.getLogger(CompletableFutureUtils.class);

    /**
     * 哪个先完成 就apply哪一个结果 这是一个关键的API,exceptionally出现异常后返回默认值
     * 异步timeout
     * @param t
     * @param future
     * @param timeout
     * @param unit
     * @param <T>
     * @return
     */
    public static <T> CompletableFuture<T> completeOnTimeout(T t, CompletableFuture<T> future, long timeout, TimeUnit unit, String ext) {
        final CompletableFuture<T> timeoutFuture = timeoutAfter(timeout, unit);
        return future.applyToEither(timeoutFuture, Function.identity()).exceptionally((throwable) -> {
            log.info("completeOnTimeout：message={}", ext);
            return t;
        });
    }

    /**
     * 哪个先完成 就apply哪一个结果 这是一个关键的API，不设置默认值，超时后抛出异常
     *
     * @param t
     * @param future
     * @param timeout
     * @param unit
     * @param <T>
     * @return
     */
    public static <T> CompletableFuture<T> orTimeout(T t, CompletableFuture<T> future, long timeout, TimeUnit unit) {
        final CompletableFuture<T> timeoutFuture = timeoutAfter(timeout, unit);
        return future.applyToEither(timeoutFuture, Function.identity()).exceptionally((throwable) -> t);
    }

    public static <T> CompletableFuture<T> timeoutAfter(long timeout, TimeUnit unit) {
        CompletableFuture<T> result = new CompletableFuture<T>();
        // timeout 时间后 抛出TimeoutException 类似于sentinel / watcher
        CompletableFutureUtils.Delayer.delayer.schedule(() ->
                result.completeExceptionally(new TimeoutException()), timeout, unit);
        return result;
    }

    public static <T> T get(T t, CompletableFuture<T> future) {
        try {
            return future.get();
        } catch (Exception e) {
            log.error("CompletableFuture.get error", e);
            return t;
        }
    }

    static final class Delayer {
        static ScheduledFuture<?> delay(Runnable command, long delay,
                                        TimeUnit unit) {
            return delayer.schedule(command, delay, unit);
        }

        static final class DaemonThreadFactory implements ThreadFactory {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setDaemon(true);
                t.setName("CompletableFutureDelayScheduler");
                return t;
            }
        }

        static final ScheduledThreadPoolExecutor delayer;

        // 注意，这里使用一个线程就可以搞定 因为这个线程并不真的执行请求 而是仅仅抛出一个异常
        static {
            (delayer = new ScheduledThreadPoolExecutor(
                    1, new CompletableFutureUtils.Delayer.DaemonThreadFactory())).
                    setRemoveOnCancelPolicy(true);
        }
    }

}
