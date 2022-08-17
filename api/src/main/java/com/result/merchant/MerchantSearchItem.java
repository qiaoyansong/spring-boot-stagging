package com.result.merchant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2022/1/25 7:55 下午
 * description：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MerchantSearchItem implements Serializable {

    private static final long serialVersionUID = 9097543861786723569L;

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
