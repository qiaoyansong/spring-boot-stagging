package com.biz.impl.merchant;

import com.biz.dto.MerchantInfoDto;
import com.common.es.EsWrapper;
import com.common.es.bean.UpdateDoc;
import com.biz.merchant.MerchantOperateService;
import com.common.utils.BeanUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

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
    public void addMerchant(MerchantInfoDto merchantInfoDto) {
        Map<String, Object> data = BeanUtil.bean2Map(merchantInfoDto);
        UpdateDoc updateDoc = new UpdateDoc(merchantInfoDto.getMerchantId().toString(), indexName);
        updateDoc.setUpdateDoc(data);
        updateDoc.setDocAsUpsert(true);
        commonEsWrapper.updateAsync(updateDoc);
    }

}
