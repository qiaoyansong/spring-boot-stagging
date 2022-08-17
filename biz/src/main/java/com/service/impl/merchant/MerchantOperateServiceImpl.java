package com.service.impl.merchant;

import com.dto.MerchantInfoDto;
import com.es.EsWrapper;
import com.es.bean.UpdateDoc;
import com.service.merchant.MerchantOperateService;
import com.utils.BeanUtil;
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

    private static final String DEFAULT_TYPE = "type";

    @Override
    public void addMerchant(MerchantInfoDto merchantInfoDto) {
        Map<String, Object> data = BeanUtil.bean2Map(merchantInfoDto);
        UpdateDoc updateDoc = new UpdateDoc(merchantInfoDto.getMerchantId().toString(), indexName, DEFAULT_TYPE);
        updateDoc.setUpdateDoc(data);
        updateDoc.setDocAsUpsert(true);
        commonEsWrapper.updateAsync(updateDoc);
    }

}
