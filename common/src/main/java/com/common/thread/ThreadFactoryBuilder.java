package com.common.thread;

import com.google.common.base.Preconditions;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2024/1/24 4:10 下午
 * description：
 */
public class ThreadFactoryBuilder {
    private String name = null;
    private Boolean daemon = null;
    private Integer priority = null;
    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler = null;

    public ThreadFactoryBuilder(String name) {
        this.name = name;
    }

    public ThreadFactoryBuilder setDaemon(boolean daemon) {
        this.daemon = daemon;
        return this;
    }

    public ThreadFactoryBuilder setPriority(int priority) {
        Preconditions.checkArgument(priority >= 1, "Thread priority (%s) must be >= %s", new Object[]{priority, 1});
        Preconditions.checkArgument(priority <= 10, "Thread priority (%s) must be <= %s", new Object[]{priority, 10});
        this.priority = priority;
        return this;
    }

    public ThreadFactoryBuilder setUncaughtExceptionHandler(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        this.uncaughtExceptionHandler = (Thread.UncaughtExceptionHandler) Preconditions.checkNotNull(uncaughtExceptionHandler);
        return this;
    }

    public ThreadFactory build() {
        return build(this);
    }

    private static ThreadFactory build(ThreadFactoryBuilder builder) {
        final String namePrefix = builder.name;
        final Boolean daemon = builder.daemon;
        final Integer priority = builder.priority;
        final Thread.UncaughtExceptionHandler uncaughtExceptionHandler = builder.uncaughtExceptionHandler;
        final ThreadFactory backingThreadFactory = Executors.defaultThreadFactory();
        final AtomicLong count = namePrefix != null ? new AtomicLong(0L) : null;
        return new ThreadFactory() {
            @Override
            public Thread newThread(Runnable runnable) {
                Thread thread = backingThreadFactory.newThread(runnable);
                if (namePrefix != null) {
                    thread.setName(namePrefix + "_" + count.getAndIncrement());
                }

                if (daemon != null) {
                    thread.setDaemon(daemon);
                }

                if (priority != null) {
                    thread.setPriority(priority);
                }

                if (uncaughtExceptionHandler != null) {
                    thread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
                }

                return thread;
            }
        };
    }

    public String getName() {
        return this.name;
    }
}
