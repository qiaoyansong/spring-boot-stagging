package com.common.trace;

import com.common.rpc.RpcResult;
import com.common.trace.spi.DubboTraceExtension;
import com.common.utils.ServiceProviders;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

import java.util.List;
import java.util.Map;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2024/1/24 8:10 下午
 * description：提供者trace拦截器, 将传递参数写入线程上下文，并在调用结束后回收线程上下文
 */
@Activate(group = "provider")
public class DubboProviderTraceFilter implements Filter {

    private final List<DubboTraceExtension> dubboTraceExtensionList = ServiceProviders.loadFromSpi(DubboTraceExtension.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        TraceContext traceContext = TraceContext.getContext();

        // 解析透传标识
        dubboTraceExtensionList.forEach(ext -> ext.beforeServerInvoke(invoker, invocation));

        // 将标识写入到线程上下文中去
        dubboTraceExtensionList.forEach(ext -> ext.prepareThreadLocal(TraceContext.getContext()));
        Result result = null;
        try {
            result = invoker.invoke(invocation);
            return result;
        } finally {
            //将逆向标识set到dubbo隐式参数透传到调用方
            if (result != null && result.getValue() instanceof RpcResult) {
                for (Map.Entry<String, String> entry : traceContext.reverseBaggageItermMap().entrySet()) {
                    ((RpcResult) result.getValue()).getAttachments().put(entry.getKey(), entry.getValue());
                }
            }
            TraceContext.clear();
            dubboTraceExtensionList.forEach(ext -> ext.afterServerInvoke(invoker, invocation));
            // Do not invoke RpcContext.getContext().clearAttachments();
        }
    }
}
