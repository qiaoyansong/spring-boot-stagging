package com.test.common.es.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2022/1/27 11:55 上午
 * description：
 */
@AllArgsConstructor
@Getter
@Setter
public class BaseEs {
    /**
     * 索引名
     */
    private String indexName;

    /**
     * type
     */
    private String type;
}
