package com.param.merchant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2022/1/25 7:55 下午
 * description：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MerchantSearchParam implements Serializable {

    private static final long serialVersionUID = -2218049181632475212L;

    /**
     * 市场id
     */
    private List<Long> marketIdList;

    /**
     * 商户id
     */
    private List<Long> merchantIdList;

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 页码
     */
    private Integer pageNum;

    /**
     * 页数
     */
    private Integer pageSize;
}