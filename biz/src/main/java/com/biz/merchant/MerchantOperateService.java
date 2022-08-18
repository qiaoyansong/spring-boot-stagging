package com.biz.merchant;

import java.util.Map;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 8/17/22 7:16 下午
 * description：
 */
public interface MerchantOperateService {

    /**
     * 添加商户
     *
     * @param merchantInfo 商户信息
     * @param merchantId   商户IDr
     */
    void addMerchant(Map<String, Object> merchantInfo, Long merchantId);

}
