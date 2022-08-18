package com.biz.merchant;

import com.common.es.bean.SearchResult;
import com.param.merchant.MerchantSearchParam;
import com.api.result.merchant.MerchantSearchItem;

import java.util.Map;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2022/1/27 11:55 上午
 * description：
 */
public interface MerchantSearchService {

    /**
     * 搜索商户
     * @param  searchParam searchParam
     * @return result
     */
    SearchResult<MerchantSearchItem> searchMerchant(MerchantSearchParam searchParam);

    /**
     * upsert商户所索引
     *
     * @param productLine productLine
     * @param merchantId  merchantId
     * @param data        data
     * @return bool
     */
    boolean upsertMerchantDoc(Integer productLine, Long merchantId, Map<String, Object> data);

    /**
     * delete商户所索引
     *
     * @param productLine productLine
     * @param merchantId  merchantId
     * @return bool
     */
    boolean deleteMerchantDoc(Integer productLine, Long merchantId);

    /**
     * delete商户所索引
     *
     * @param productLine productLine
     * @param merchantId  merchantId
     * @return bool
     */
    boolean deleteMerchantDoc(Integer productLine, Long merchantId,String indexName);

    /**
     * index商户所索引
     *
     * @param productLine productLine
     * @param merchantId  merchantId
     * @param data        data
     * @return bool
     */
    boolean indexMerchantDoc(Integer productLine, Long merchantId, String data);
}
