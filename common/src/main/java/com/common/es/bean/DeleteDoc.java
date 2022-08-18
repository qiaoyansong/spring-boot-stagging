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
public class DeleteDoc extends BaseEsDoc {

    public DeleteDoc(String docId, String indexName) {
        super(docId, indexName);
    }
}
