package com.common.trace.spi;

import com.common.trace.TraceContext;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcContext;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2024/1/24 8:21 下午
 * description：
 */
public class CommonDubboTraceExtension  implements DubboTraceExtension {

    public CommonDubboTraceExtension() {
    }

    @Override
    public void beforeServerInvoke(Invoker<?> invoker, Invocation invocation) {

    }

    @Override
    public void prepareThreadLocal(TraceContext context) {
        RpcContext rpcContext = RpcContext.getContext();
        TraceContext traceContext = TraceContext.getContext();
        traceContext.fromBaggageItemMap(rpcContext.getAttachments());
    }

    @Override
    public void afterServerInvoke(Invoker<?> invoker, Invocation invocation) {
    }

    @Override
    public void beforeClientInvoke(Invoker<?> invoker, Invocation invocation) {
        TraceContext traceContext = TraceContext.getContext();
        RpcContext context = RpcContext.getContext();
        traceContext.baggageItemMap().forEach(context::setAttachment);
    }

    @Override
    public void afterClientInvoke(Invoker<?> invoker, Invocation invocation) {
    }
}
