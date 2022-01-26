package com;

import com.service.HelloRpcServiceImpl;
import com.service.HelloService;
import com.service.impl.HelloServiceImpl;
import com.utils.MockHelper;
import lombok.Getter;
import org.junit.Before;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2022/1/26 10:26 上午
 * description：
 */
@Getter
public class BaseTddTest {

    protected HelloRpcService helloRpcService;
    protected HelloService helloService;

    @Before
    public void init() throws Exception {
        // 确定的上下游服务
        helloRpcService = new HelloRpcServiceImpl();
        helloService = new HelloServiceImpl();
        MockHelper.mockField(helloRpcService, "helloService", helloService);
        // 不确定的上下游服务，直接mock出来
    }
}
