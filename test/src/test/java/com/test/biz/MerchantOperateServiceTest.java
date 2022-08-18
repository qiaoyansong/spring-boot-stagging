package com.test.biz;

import com.biz.dto.MerchantInfoDto;
import com.biz.merchant.MerchantOperateService;
import com.test.BaseTestApplication;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 8/18/22 11:37 上午
 * description：测试的时候需要使用同步，防止es连接被提前关闭
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
        merchantOperateService.addMerchant(merchantInfoDto);
    }

    @Test
    public void testPartialUpdateMerchant() {
        MerchantInfoDto merchantInfoDto = new MerchantInfoDto();
        merchantInfoDto.setMerchantId(1L);
        merchantInfoDto.setMerchantName("新测试商户1名字");
        merchantOperateService.addMerchant(merchantInfoDto);
    }

}
