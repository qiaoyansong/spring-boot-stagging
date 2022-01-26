package com.service.tdd;

import com.BaseTddTest;
import com.constant.RpcCode;
import com.param.SayHelloParam;
import com.result.RpcResult;
import com.result.SayHelloResult;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2022/1/26 10:33 上午
 * description：
 */
public class TddHelloRpcService extends BaseTddTest {

    @Test
    public void testSayHello(){
        RpcResult<SayHelloResult> rpcResult = helloRpcService.sayHello(new SayHelloParam("XXX"));
        Assert.assertNotNull(rpcResult);
        Assert.assertEquals(rpcResult.getCode(), RpcCode.SUCCESS);
        Assert.assertNotNull(rpcResult.getData());
        System.out.println(rpcResult.getData().getMessage());
    }
    
}
