package com.service.impl;

import com.alibaba.fastjson.JSON;
import com.param.SayHelloParam;
import com.result.SayHelloResult;
import com.service.HelloService;
import com.utils.LogFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2022/1/25 8:46 下午
 * description：
 */
@Service
public class HelloServiceImpl implements HelloService {

    private static final Logger LOG = LogFactory.BIZ_SERVICE_LOG;

    @Override
    public SayHelloResult sayHello(SayHelloParam param) {
        LOG.info("[HelloServiceImpl#sayHello] begin,param={}", JSON.toJSONString(param));
        return new SayHelloResult(param.getName() + ",你好");
    }

}
