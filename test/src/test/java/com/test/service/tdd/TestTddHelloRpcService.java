package com.test.service.tdd;

import com.api.constant.RpcCode;
import com.api.result.RpcResult;
import com.api.result.SayHelloResult;
import com.dal.domain.User;
import com.param.SayHelloParam;
import com.test.BaseTddTest;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2022/1/26 10:33 上午
 * description：
 */
public class TestTddHelloRpcService extends BaseTddTest {


    @Test
    public void testSayHello(){
        Mockito.when(userMapper.fetchUserInfo(Mockito.any())).thenAnswer(
                invocationOnMock -> {
                    User user = new User();
                    user.setId(1);
                    user.setUsername("XXX");
                    return user;
                }
        );

        RpcResult<SayHelloResult> rpcResult = helloRpcService.sayHello(new SayHelloParam(1));
        Assert.assertNotNull(rpcResult);
        Assert.assertEquals(rpcResult.getCode(), RpcCode.SUCCESS);
        Assert.assertNotNull(rpcResult.getData());
        System.out.println(rpcResult.getData().getMessage());
    }
    
}
