package com.common.utils;

import com.common.rpc.RpcResult;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2024/1/24 8:32 下午
 * description：
 */
public interface RpcResultDecoder {

    /**
     * 解析逆向参数Attachments
     *
     * @param result
     * @return
     */
    @Nonnull
    static Map<String, String> decodeAttachments(@Nullable Result result) {
        // 无返回值
        if (result == null) {
            return new LinkedHashMap<>();
        }

        // 是RPC异常
        if (result.getException() instanceof RpcException) {
            return new LinkedHashMap<>();
        }

        // 非泛化调用场景
        if (result.getValue() instanceof RpcResult) {
            RpcResult<?> rpcResult = (RpcResult<?>) result.getValue();
            return rpcResult.getAttachments();
        }

        //
        // 泛化调用场景
        //
        if (result.getValue() instanceof Map) {
            //noinspection unchecked
            Map<String, Object> data = (Map<String, Object>) result.getValue();
            try {
                Map<String, String> attachments = Optional.ofNullable(data.get("attachments")).map(it -> {
                    return (Map<String, String>) data.get("attachments");
                }).orElse(new LinkedHashMap<String, String>());
                return attachments;
            } catch (Exception e) {
                return new LinkedHashMap<>();
            }
        }
        // 非标准返回值
        return new LinkedHashMap<>();
    }
}
