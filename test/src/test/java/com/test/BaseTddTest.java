package com.test;

import com.api.HelloRpcService;
import com.biz.helper.Do2DtoHelper;
import com.biz.impl.HelloServiceImpl;
import com.biz.service.HelloService;
import com.dal.mapper.UserMapper;
import com.service.HelloRpcServiceImpl;
import com.test.utils.MockHelper;
import lombok.Getter;
import org.junit.Before;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2022/1/26 10:26 上午
 * description：
 */
@Getter
public class BaseTddTest {

    protected HelloRpcService helloRpcService;

    protected HelloService helloService;

    protected UserMapper userMapper;

    protected Do2DtoHelper do2DtoHelper;

    @Before
    public void init() throws Exception {
        // 确定的上下游服务
        helloRpcService = new HelloRpcServiceImpl();
        helloService = new HelloServiceImpl();
        MockHelper.mockField(helloRpcService, "helloService", helloService);
        do2DtoHelper = Mappers.getMapper( Do2DtoHelper.class);
        MockHelper.mockField(helloService, "do2DtoHelper", do2DtoHelper);

        // 不确定的上下游服务，直接mock出来
        userMapper = Mockito.mock(UserMapper.class);
        MockHelper.mockField(helloService, "userMapper", userMapper);
    }
}
