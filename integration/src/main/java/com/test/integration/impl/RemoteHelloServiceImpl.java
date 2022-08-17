package com.test.integration.impl;

import com.test.integration.RemoteHelloService;
import com.alibaba.fastjson.JSON;
import com.test.integration.bean.SayHelloResult;
import com.constant.RpcCode;
import com.test.common.enums.ErrorEnum;
import com.test.common.exception.BizException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.param.SayHelloParam;
import com.api.result.RpcResult;
import com.test.common.utils.HttpUtil;
import com.test.common.utils.JsonUtil;
import com.test.common.utils.LogFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2022/2/9 5:54 下午
 * description：
 */
@Service
public class RemoteHelloServiceImpl implements RemoteHelloService {

    private static final Logger log = LogFactory.INTEGRATION_LOG;

    @Autowired
    private HttpUtil httpUtil;

    @Value("${sayHello-api}")
    private String url;

    @Override
    public SayHelloResult sayHello(SayHelloParam param) {
        try {
            String result = httpUtil.post(url, JSON.toJSONString(param));
            RpcResult<SayHelloResult> sayHelloResult = JsonUtil.toObject(result, new TypeReference<RpcResult<SayHelloResult>>() {
            });
            if (sayHelloResult == null || sayHelloResult.getCode() != RpcCode.SUCCESS) {
                // 异常情况
                throw new BizException(ErrorEnum.DOWNSTREAM_SERVICES_ERROR_WITH_MSG.getCode(), String.format(ErrorEnum.DOWNSTREAM_SERVICES_ERROR_WITH_MSG.getMsg(), "简单demo"));
            }
            return sayHelloResult.getData();
        } catch (BizException e) {
            log.error("[RemoteHelloServiceImpl#sayHello] bizError,e={}", e.getMessage());
            throw new BizException(e.getCode(), e.getMsg());
        } catch (Exception e) {
            log.error("[RemoteHelloServiceImpl#sayHello] error,e={}", e);
            throw new BizException(ErrorEnum.INNER_ERROR.getCode(), ErrorEnum.INNER_ERROR.getMsg());
        }
    }

}

