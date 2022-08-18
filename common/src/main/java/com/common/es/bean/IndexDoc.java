package com.common.es.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2022/1/27 11:55 上午
 * description：
 */
@Getter
@Setter
public class IndexDoc extends BaseEsDoc {

    /**
     * 覆盖文档
     */
    private String indexDoc;

    public IndexDoc(String docId, String indexName, String type) {
        super(docId, indexName, type);
    }
}
