package com.common.es.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2022/1/27 11:55 上午
 * description：
 */
@Getter
@Setter
public class UpdateDoc extends BaseEsDoc {

    private Map<String, Object> updateDoc;

    /**
     * 是否是upsert操作
     */
    private boolean docAsUpsert;

    /**
     * 是否是部分更新
     */
    private boolean partialUpdate = true;

    public UpdateDoc(String docId, String indexName) {
        super(docId, indexName);
    }
}
