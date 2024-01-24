package com.common.trace;

import com.common.trace.spi.DubboTraceExtension;
import com.common.utils.RpcResultDecoder;
import com.common.utils.ServiceProviders;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.support.RpcUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.common.constant.CommonConstant.KEY_REVERSE_PARAM_PREFIX;
import static com.common.constant.CommonConstant.TRACE_BIZ_BAGGAGE_PREFIX;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2024/1/24 8:26 下午
 * description： 消费者trace拦截器，将线程上下文写入传递参数
 */
@Activate(group = "consumer")
public class DubboConsumerTraceFilter implements Filter {

    private final List<DubboTraceExtension> dubboTraceExtensionList = ServiceProviders
            .loadFromSpi(DubboTraceExtension.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        dubboTraceExtensionList.forEach(ext -> ext.beforeClientInvoke(invoker, invocation));
        Result result = null;
        try {
            result = invoker.invoke(invocation);
            return result;
        } finally {
            if (RpcUtils.isAsync(invoker.getUrl(), invocation) || result instanceof AsyncRpcResult) {
                FutureContext.getContext().setFuture(FutureContext.getContext()
                        .getCompletableFuture()
                        .whenComplete((res, err) -> {
                            AppResponse appResponse = new AppResponse();
                            appResponse.setValue(res);
                            appResponse.setException(err);
                            loadReverseParam(appResponse);
                        }));
            } else {
                /**
                 * load逆向参数到线程上下文
                 */
                loadReverseParam(result);
            }
            dubboTraceExtensionList.forEach(ext -> ext.afterClientInvoke(invoker, invocation));
        }
    }

    private void loadReverseParam(Result result) {
        Map<String, String> reverseItermMap = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : RpcResultDecoder.decodeAttachments(result).entrySet()) {
            if (entry.getKey().startsWith(TRACE_BIZ_BAGGAGE_PREFIX + KEY_REVERSE_PARAM_PREFIX)) {
                reverseItermMap.put(entry.getKey(), entry.getValue());
            }
        }
        TraceContext.getContext().fromReverseBaggageItermMap(reverseItermMap);
    }
}
