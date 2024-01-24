package com.api;
import com.param.SayHelloParam;
import com.common.rpc.RpcResult;
import com.api.result.SayHelloResult;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2022/1/25 8:02 下午
 * description：简单rpc服务
 */
public interface HelloRpcService {

    /**
     * 简单对外的dubbo接口
     * @param param
     * @return
     */
    RpcResult<SayHelloResult> sayHello(SayHelloParam param);

}
