package com.test.biz;

import com.biz.dto.MerchantInfoDto;
import com.biz.merchant.MerchantOperateService;
import com.common.utils.BeanUtil;
import com.test.BaseTestApplication;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 8/18/22 11:37 上午
 * description：测试的时候需要使用同步，防止es连接被提前关闭
 * 为了验证是提前关闭的问题，可以包装controller层接口，启动容器不关闭 验证即可
 */
public class MerchantOperateServiceTest extends BaseTestApplication {

    @Autowired
    private MerchantOperateService merchantOperateService;

    @Test
    public void testAddMerchant() {
        MerchantInfoDto merchantInfoDto = new MerchantInfoDto();
        merchantInfoDto.setMerchantId(1L);
        merchantInfoDto.setMerchantName("测试商户1");
        merchantInfoDto.setMarketId(11L);
        merchantOperateService.addMerchant(BeanUtil.bean2Map(merchantInfoDto), 1L);
    }

    @Test
    public void testPartialUpdateMerchant() {
        Map<String, Object> merchantInfo = new HashMap<>();
        merchantInfo.put("merchantId", 1L);
        merchantInfo.put("merchantName", "新测试商户1名字");
        merchantOperateService.addMerchant(merchantInfo, 1L);
    }

}
