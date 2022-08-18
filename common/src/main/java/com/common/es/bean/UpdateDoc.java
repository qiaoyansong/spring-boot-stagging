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

    private boolean docAsUpsert;

    public UpdateDoc(String docId, String indexName, String type) {
        super(docId, indexName, type);
    }
}
