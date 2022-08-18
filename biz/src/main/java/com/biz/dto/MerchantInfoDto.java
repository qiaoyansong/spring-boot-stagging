package com.biz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 8/17/22 7:17 下午
 * description：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MerchantInfoDto {

    /**
     * 商户id
     */
    private Long merchantId;

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 市场id
     */
    private Long marketId;

}
