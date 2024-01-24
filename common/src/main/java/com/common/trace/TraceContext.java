package com.common.trace;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import static com.common.constant.CommonConstant.*;


/**
 * @author ：Qiao Yansong
 * @date ：Created in 2024/1/24 4:54 下午
 * description：
 */
public class TraceContext {

    private static final ThreadLocal<TraceContext> LOCAL = ThreadLocal.withInitial(TraceContext::new);

    /**
     * 链路透传系统参数
     */
    private final Map<String, String> sysBaggageItems = new LinkedHashMap<>();

    /**
     * 链路透传业务参数(供业务使用)
     */
    private final Map<String, String> bizBaggageItems = new LinkedHashMap<>();

    @Nonnull
    public static TraceContext getContext() {
        return LOCAL.get();
    }

    public static void clear() {
        LOCAL.remove();
    }

    public String getTraceId() {
        return getSysBaggageItems().get(KEY_TRACE);
    }

    public void setTraceId(String traceId) {
        getSysBaggageItems().put(KEY_TRACE, traceId == null || traceId.isEmpty()
                ? UUID.randomUUID().toString()
                : traceId);
    }

    public void fromBaggageItemMap(@Nonnull Map<String, String> rawMap) {
        sysBaggageItems.clear();
        bizBaggageItems.clear();

        for (Map.Entry<String, String> entry : rawMap.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            if (entry.getKey().startsWith(TRACE_SYS_BAGGAGE_PREFIX)) {
                String key = entry.getKey().substring(TRACE_SYS_BAGGAGE_PREFIX.length());
                String value = entry.getValue();
                getSysBaggageItems().put(key, value);
            } else if (entry.getKey().startsWith(TRACE_BIZ_BAGGAGE_PREFIX)) {
                String key = entry.getKey().substring(TRACE_BIZ_BAGGAGE_PREFIX.length());
                String value = entry.getValue();
                getBizBaggageItems().put(key, value);
            }
        }
        setTraceId(rawMap.get(TRACE_SYS_BAGGAGE_PREFIX + KEY_TRACE));
    }

    public Map<String, String> baggageItemMap() {
        Map<String, String> context = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : getBizBaggageItems().entrySet()) {
            context.put(TRACE_BIZ_BAGGAGE_PREFIX + entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, String> entry : getSysBaggageItems().entrySet()) {
            context.put(TRACE_SYS_BAGGAGE_PREFIX + entry.getKey(), entry.getValue());
        }
        context.put(TRACE_SYS_BAGGAGE_PREFIX + KEY_TRACE, getTraceId());
        return context;
    }

    public Map<String, String> reverseBaggageItermMap() {
        Map<String, String> context = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : getBizBaggageItems().entrySet()) {
            if (entry.getKey().startsWith(KEY_REVERSE_PARAM_PREFIX)) {
                context.put(TRACE_BIZ_BAGGAGE_PREFIX + entry.getKey(), entry.getValue());
            }
        }
        for (Map.Entry<String, String> entry : getSysBaggageItems().entrySet()) {
            if (entry.getKey().startsWith(KEY_REVERSE_PARAM_PREFIX)) {
                context.put(TRACE_SYS_BAGGAGE_PREFIX + entry.getKey(), entry.getValue());
            }
        }
        return context;
    }

    public void fromReverseBaggageItermMap(@Nonnull Map<String, String> reverseItermMap) {
        for (Map.Entry<String, String> entry : reverseItermMap.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            if (entry.getKey().startsWith(TRACE_SYS_BAGGAGE_PREFIX)) {
                String key = entry.getKey().substring(TRACE_SYS_BAGGAGE_PREFIX.length());
                String value = entry.getValue();
                getSysBaggageItems().put(key, value);
            } else if (entry.getKey().startsWith(TRACE_BIZ_BAGGAGE_PREFIX)) {
                String key = entry.getKey().substring(TRACE_BIZ_BAGGAGE_PREFIX.length());
                String value = entry.getValue();
                getBizBaggageItems().put(key, value);
            }
        }
    }

    public Map<String, String> getSysBaggageItems() {
        return sysBaggageItems;
    }

    public Map<String, String> getBizBaggageItems() {
        return bizBaggageItems;
    }



    private TraceContext() {
    }
}
