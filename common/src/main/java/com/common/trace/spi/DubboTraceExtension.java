package com.common.trace.spi;

import com.common.trace.TraceContext;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2024/1/24 8:12 下午
 * description：
 */
public interface DubboTraceExtension {

    /**
     * 服务端被调用前
     *
     * @param invoker    服务提供者
     * @param invocation 调用参数
     */
    void beforeServerInvoke(Invoker<?> invoker, Invocation invocation);

    /**
     * 将解析参数填充到线程上下文中去
     *
     * @param context didi-tracer上下文
     */
    void prepareThreadLocal(TraceContext context);

    /**
     * 服务端调用返回
     *
     * @param invoker    服务提供者
     * @param invocation 调用参数
     */
    void afterServerInvoke(Invoker<?> invoker, Invocation invocation);

    /**
     * 客户端调用前
     *
     * @param invoker     服务提供者
     * @param invocation  调用参数
     */
    void beforeClientInvoke(Invoker<?> invoker, Invocation invocation);

    /**
     * 客户端调用返回
     *
     * @param invoker    服务提供者
     * @param invocation 调用参数
     */
    void afterClientInvoke(Invoker<?> invoker, Invocation invocation);
}
