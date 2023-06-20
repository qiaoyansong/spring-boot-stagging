package com.test;

import com.alibaba.fastjson.JSON;
import com.api.HelloRpcService;
import com.param.SayHelloParam;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.service.GenericService;

/**
 *
 */
public class DubboGenericServiceTest {

    private static final String zk      = "zookeeper://127.0.0.1:2181";
    private static final String version = "1.0.0_sim";

    // 如果是企业级系统调用的话，一般会设置interfaceName维度的ReferenceConfig缓存，用于避免开销问题
    public static Object callGenericService(String interfaceName, String methodName, String[] parameterTypes,
                                            Object[] args) {

        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("dubbo-generic-Service");

        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress(zk);

        ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
        reference.setRegistry(registryConfig);
        reference.setProtocol("dubbo");
        reference.setApplication(applicationConfig);
        reference.setInterface(interfaceName);
        reference.setVersion(version);
        reference.setTimeout(10000);
        reference.setGeneric(true);
//        reference.setFilter("");
//        reference.setRetries(0);

        GenericService genericService = reference.get();

        return genericService.$invoke(methodName, parameterTypes, args);
    }

    public static void main(String[] args) throws Exception {

        String name = HelloRpcService.class.getName();
        String method = "sayHello";
        String[] paramTypes = new String[] { SayHelloParam.class.getName() };


        SayHelloParam param = new SayHelloParam();
        param.setUid(0);


        Object[] paramValues = new Object[] { param};
        Object result = callGenericService(name, method, paramTypes, paramValues);
        System.out.println(JSON.toJSONString(result));
    }
}
