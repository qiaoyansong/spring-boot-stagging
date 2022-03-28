package spock.service.tdd

import com.constant.RpcCode
import com.domain.User
import com.param.SayHelloParam
import com.result.RpcResult
import com.result.SayHelloResult
import org.junit.Assert
import org.mockito.Mockito
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import spock.base.SpockBddBaseTest
import spock.lang.Unroll

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2022/3/28 23:23
 * description：${description}
 */
class TddHelloRpcServiceTest extends SpockBddBaseTest{

    def "testSayHello"() {
        setup: "mock数据"
        Mockito.when(userMapper.fetchUserInfo(Mockito.any())).thenAnswer(new Answer<Object>() {
            @Override
            Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                User user = new User();
                user.setId(1);
                user.setUsername("XXX");
                return user;
            }
        });

        when: "调用方法"
        RpcResult<SayHelloResult> rpcResult = helloRpcService.sayHello(new SayHelloParam(1));

        then: "比较结果"
        with(rpcResult) {
            Assert.assertNotNull(rpcResult);
            Assert.assertEquals(rpcResult.getCode(), RpcCode.SUCCESS);
            Assert.assertNotNull(rpcResult.getData());
            System.out.println(rpcResult.getData().getMessage());
        }

    }

}
