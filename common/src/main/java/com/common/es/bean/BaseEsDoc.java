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
public class BaseEsDoc extends BaseEs {

    /**
     * 主键
     */
    private String docId;

    public BaseEsDoc(String docId, String indexName, String type) {
        super(indexName, type);
        this.docId = docId;
    }
}
