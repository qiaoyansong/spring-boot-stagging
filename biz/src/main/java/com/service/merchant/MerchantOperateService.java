package com.service.merchant;

import com.dto.MerchantInfoDto;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 8/17/22 7:16 下午
 * description：
 */
public interface MerchantOperateService {

    /**
     * 添加商户
     *
     * @param merchantInfoDto
     */
    void addMerchant(MerchantInfoDto merchantInfoDto);

}
