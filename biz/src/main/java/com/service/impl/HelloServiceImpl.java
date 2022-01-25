package com.service.impl;

import com.param.SayHelloParam;
import com.result.SayHelloResult;
import com.service.HelloService;
import org.springframework.stereotype.Service;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2022/1/25 8:46 下午
 * description：
 */
@Service
public class HelloServiceImpl implements HelloService {

    @Override
    public SayHelloResult sayHello(SayHelloParam param) {
        return new SayHelloResult(param.getName() + ",你好");
    }

}
