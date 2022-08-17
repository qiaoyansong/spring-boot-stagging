package com.test.common.es;

import com.alibaba.fastjson.JSON;
import com.test.common.es.bean.*;
import com.test.common.utils.LogFactory;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.collapse.CollapseBuilder;
import org.elasticsearch.xcontent.XContentType;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2022/1/27 11:55 上午
 * description：
 */
public class EsWrapper {

    private static final Logger LOGGER = LogFactory.COMMON_LOG;

    @Setter
    private RestHighLevelClient esClient;

    private static final int MAX_PAGE_SIZE = 10000;

    /**
     * 去重统计数据
     */
    public <T> List<T> listDistinctBySearch(BaseEs baseEs, SearchSourceBuilder searchSourceBuilder, String field, Class<T> cls) {
        CollapseBuilder collapseBuilder = new CollapseBuilder(field);
        searchSourceBuilder.collapse(collapseBuilder);
        searchSourceBuilder.size(MAX_PAGE_SIZE);
        SearchRequest request = new SearchRequest();
        request.indices(baseEs.getIndexName());
        request.types(baseEs.getType());
        request.source(searchSourceBuilder);
        try {
            SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);
            SearchHits searchHits = response.getHits();

            return Arrays.stream(searchHits.getHits()).map(item -> {
                DocumentField documentField = item.getFields().get(field);
                if (documentField != null) {
                    return cls.cast(documentField.getValue());
                }
                return null;
            }).filter(Objects::nonNull).collect(Collectors.toList());
        } catch (IOException e) {
            LOGGER.warn("listDistinctBySearch查询失败：{}_{}",
                    e.getMessage(), searchSourceBuilder.toString(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 同步查询
     *
     * @param searchRequest searchRequest
     * @param clazz         class
     * @param <T>           class type
     * @return SearchResult
     */
    public <T> SearchResult<T> selectList(SearchRequest searchRequest, Class<T> clazz) {
        if (searchRequest == null) {
            return null;
        }
        List<T> list = Collections.emptyList();
        long totalHits = 0L;
        try {
            SearchResponse response = esClient.search(searchRequest, RequestOptions.DEFAULT);
            if (response == null) {
                return null;
            }
            SearchHits searchHits = response.getHits();
            if (searchHits == null) {
                return null;
            }
            list = Arrays.stream(searchHits.getHits()).map(item -> JSON.parseObject(item.getSourceAsString(), clazz)).collect(Collectors.toList());
            totalHits = searchHits.getTotalHits().value;
        } catch (Exception e) {
            LOGGER.error("EsWrapper#selectList error searchRequest:{}, clazz:{}, e:{}", JSON.toJSONString(searchRequest), clazz, e.getMessage(), e);
        }
        SearchResult<T> searchResult = new SearchResult<>();
        searchResult.setItems(list);
        searchResult.setTotalHits(totalHits);
        return searchResult;
    }

    /**
     * 异步更新
     *
     * @param updateDoc updateDoc
     * @return bool
     */
    public boolean updateAsync(UpdateDoc updateDoc) {
        if (updateDoc == null || updateDoc.getDocId() == null || updateDoc.getIndexName() == null || updateDoc.getType() == null) {
            return false;
        }
        UpdateRequest updateRequest = new UpdateRequest(updateDoc.getIndexName(), updateDoc.getType(), updateDoc.getDocId()).doc(updateDoc.getUpdateDoc());
        if (updateDoc.isDocAsUpsert()) {
            updateRequest.docAsUpsert(true);
        }

        // 版本冲突最大重试次数
        updateRequest.retryOnConflict(3);

        try {
            esClient.updateAsync(updateRequest, RequestOptions.DEFAULT, new ActionListener<UpdateResponse>() {
                @Override
                public void onResponse(UpdateResponse updateResponse) {
                    LOGGER.info("EsWrapper#upsertAsync success, updateDoc:{}, updateResponse = {}", JSON.toJSONString(updateDoc), updateResponse);
                }

                @Override
                public void onFailure(Exception e) {
                    LOGGER.error("EsWrapper#upsertAsync failure updateDoc:{}, e:{}", JSON.toJSONString(updateDoc), e.getMessage(), e);
                }
            });
        } catch (Exception e) {
            LOGGER.error("EsWrapper#upsertAsync failure updateDoc:{}, e:{}", JSON.toJSONString(updateDoc), e.getMessage(), e);
        }

        return true;
    }

    /**
     * 批量异步update
     *
     * @param updateDocs updateDocs
     * @return bool
     */
    public boolean bulkUpdateAsync(List<UpdateDoc> updateDocs) {
        if (CollectionUtils.isEmpty(updateDocs)) {
            return false;
        }
        BulkRequest bulkRequest = new BulkRequest();
        updateDocs.forEach(updateDoc -> {
            UpdateRequest updateRequest = new UpdateRequest(updateDoc.getIndexName(), updateDoc.getType(), updateDoc.getDocId()).doc(updateDoc.getUpdateDoc());
            if (updateDoc.isDocAsUpsert()) {
                updateRequest.docAsUpsert(true);
            }
            bulkRequest.add(updateRequest);
        });

        try {
            esClient.bulkAsync(bulkRequest, RequestOptions.DEFAULT, new ActionListener<BulkResponse>() {
                @Override
                public void onResponse(BulkResponse bulkItemResponses) {

                }

                @Override
                public void onFailure(Exception e) {
                    LOGGER.error("EsWrapper#bulkUpdateAsync failure updateDocs:{}, e:{}", JSON.toJSONString(updateDocs), e.getMessage(), e);
                }
            });
        } catch (Exception e) {
            LOGGER.error("EsWrapper#bulkUpdateAsync failure updateDocs:{}, e:{}", JSON.toJSONString(updateDocs), e.getMessage(), e);
        }

        return true;
    }

    /**
     * 异步覆盖
     *
     * @param indexDoc indexDoc
     * @return bool
     */
    public boolean indexAsync(IndexDoc indexDoc) {
        if (indexDoc == null || indexDoc.getDocId() == null || indexDoc.getIndexName() == null || indexDoc.getType() == null ||
            StringUtils.isBlank(indexDoc.getIndexDoc())) {
            return false;
        }
        IndexRequest indexRequest =
            new IndexRequest(indexDoc.getIndexName(), indexDoc.getType(), indexDoc.getDocId()).source(indexDoc.getIndexDoc(), XContentType.JSON);

        try {
            esClient.indexAsync(indexRequest, RequestOptions.DEFAULT, new ActionListener<IndexResponse>() {
                @Override
                public void onResponse(IndexResponse indexResponse) {

                }

                @Override
                public void onFailure(Exception e) {
                    LOGGER.error("EsWrapper#indexAsync failure indexDoc:{}, e:{}", JSON.toJSONString(indexRequest), e.getMessage(), e);
                }
            });
        } catch (Exception e) {
            LOGGER.error("EsWrapper#indexAsync failure indexDoc:{}, e:{}", JSON.toJSONString(indexRequest), e.getMessage(), e);
        }

        return true;
    }

    /**
     * 异步删除
     *
     * @param deleteDoc deleteDoc
     * @return bool
     */
    public boolean deleteAsync(DeleteDoc deleteDoc) {
        if (deleteDoc == null || deleteDoc.getDocId() == null || deleteDoc.getIndexName() == null || deleteDoc.getType() == null) {
            return false;
        }
        DeleteRequest deleteRequest = new DeleteRequest(deleteDoc.getIndexName(), deleteDoc.getType(), deleteDoc.getDocId());

        try {
            esClient.deleteAsync(deleteRequest, RequestOptions.DEFAULT, new ActionListener<DeleteResponse>() {
                @Override
                public void onResponse(DeleteResponse deleteResponse) {

                }

                @Override
                public void onFailure(Exception e) {
                    LOGGER.error("EsWrapper#deleteAsync failure deleteDoc:{}, e:{}", JSON.toJSONString(deleteDoc), e.getMessage(), e);
                }
            });
        } catch (Exception e) {
            LOGGER.error("EsWrapper#deleteAsync failure deleteDoc:{}, e:{}", JSON.toJSONString(deleteDoc), e.getMessage(), e);
        }

        return true;
    }

    public Integer transferFrom(Integer pageNum, Integer pageSize) {
        if (pageNum == null || pageNum < 1) {
            return 0;
        }
        return (pageNum - 1) * pageSize;
    }

    public void buildFilter(BoolQueryBuilder boolQueryBuilder, String field, Object must, Object notMust) {
        if (must != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery(field, must));
        }
        if (notMust != null) {
            boolQueryBuilder.mustNot(QueryBuilders.termQuery(field, notMust));
        }
    }

    public void buildRange(BoolQueryBuilder boolQueryBuilder, String field, Object lower, Object upper) {
        RangeQueryBuilder queryBuilder = QueryBuilders.rangeQuery(field);
        queryBuilder.gt(lower);
        queryBuilder.lte(upper);
        boolQueryBuilder.filter(queryBuilder);
    }

    public void buildFilter(BoolQueryBuilder boolQueryBuilder, String field, List<?> must, List<?> notMust) {
        if (must != null) {
            if (must.size() == 1) {
                boolQueryBuilder.filter(QueryBuilders.termQuery(field, must.get(0)));
            } else {
                boolQueryBuilder.filter(QueryBuilders.termsQuery(field, must));
            }
        }
        if (notMust != null) {
            if (notMust.size() == 1) {
                boolQueryBuilder.mustNot(QueryBuilders.termQuery(field, notMust.get(0)));
            } else {
                boolQueryBuilder.mustNot(QueryBuilders.termsQuery(field, notMust));
            }
        }
    }

    public void buildFuzzy(BoolQueryBuilder boolQueryBuilder, String field, String fuzzy) {
        if (StringUtils.isNotBlank(fuzzy)) {
            boolQueryBuilder.filter(QueryBuilders.matchPhraseQuery(field, fuzzy));
        }
    }
}
