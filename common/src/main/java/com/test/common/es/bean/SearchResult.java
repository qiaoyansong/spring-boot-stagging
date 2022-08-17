package com.test.common.es.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 8/17/22 5:51 下午
 * description：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResult<T> implements Serializable {

    private static final long serialVersionUID = -2651784918070567279L;


    /**
     * totalHits
     */
    private Long totalHits;

    /**
     * items
     */
    private List<T> items;
}
