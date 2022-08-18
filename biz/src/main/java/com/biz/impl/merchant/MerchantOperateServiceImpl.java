package com.biz.impl.merchant;

import com.api.constant.RpcCode;
import com.biz.merchant.MerchantOperateService;
import com.common.es.EsWrapper;
import com.common.es.bean.UpdateDoc;
import com.common.exception.BizException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 8/17/22 7:18 下午
 * description：
 */
@Repository
public class MerchantOperateServiceImpl implements MerchantOperateService {

    @Value("${es.merchant.index.name}")
    private String indexName;

    @Resource
    private EsWrapper commonEsWrapper;

    @Override
    public void addMerchant(Map<String, Object> merchantInfo, Long merchantId) {
        if (Objects.isNull(merchantId)) {
            throw new BizException(RpcCode.SYSTEM_ERROR, "商户ID为空");
        }
        UpdateDoc updateDoc = new UpdateDoc(merchantId.toString(), indexName);
        updateDoc.setUpdateDoc(merchantInfo);
        updateDoc.setDocAsUpsert(true);
        commonEsWrapper.updateAsync(updateDoc);
    }

}
