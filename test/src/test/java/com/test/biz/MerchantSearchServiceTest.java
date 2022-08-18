package com.test.biz;

import com.api.result.merchant.MerchantSearchItem;
import com.biz.merchant.MerchantSearchService;
import com.common.es.bean.SearchResult;
import com.param.merchant.MerchantSearchParam;
import com.test.BaseTestApplication;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 8/18/22 7:44 下午
 * description：
 */
public class MerchantSearchServiceTest extends BaseTestApplication {

    @Autowired
    private MerchantSearchService merchantSearchService;

    @Test
    public void testSearchMerchant() {
        MerchantSearchParam merchantSearchParam = new MerchantSearchParam();
        merchantSearchParam.setPageNum(1);
        merchantSearchParam.setPageSize(2);
        merchantSearchParam.setMerchantName("商户");
        SearchResult<MerchantSearchItem> merchantSearchItemSearchResult = merchantSearchService.searchMerchant(merchantSearchParam);
        System.out.println(merchantSearchItemSearchResult);
    }

}
