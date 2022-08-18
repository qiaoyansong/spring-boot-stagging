package com.service.vo.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 8/18/22 7:39 下午
 * description：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddMerchantParam {

    private Map<String, Object> merchantInfo;

}
