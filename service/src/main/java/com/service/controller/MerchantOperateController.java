package com.service.controller;

import com.biz.merchant.MerchantOperateService;
import com.service.vo.param.AddMerchantParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 8/18/22 7:16 下午
 * description：
 */
@RestController
@RequestMapping(value = "/merchant")
public class MerchantOperateController {

    @Autowired
    private MerchantOperateService merchantOperateService;

    @PostMapping(value = "/add")
    public void addMerchant(@RequestBody AddMerchantParam addMerchantParam) {
        Map<String, Object> merchantInfo = addMerchantParam.getMerchantInfo();
        merchantOperateService.addMerchant(merchantInfo, Long.valueOf((String) merchantInfo.get("merchantId")));
    }

}
