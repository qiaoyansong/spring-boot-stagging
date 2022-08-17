package com.test.service.impl.merchant;

import com.test.common.es.EsWrapper;
import com.test.common.es.bean.DeleteDoc;
import com.test.common.es.bean.IndexDoc;
import com.test.common.es.bean.SearchResult;
import com.test.common.es.bean.UpdateDoc;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.param.merchant.MerchantSearchParam;
import com.api.result.merchant.MerchantSearchItem;
import com.test.service.merchant.MerchantSearchService;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2022/1/27 11:55 上午
 * description：
 */
@Repository
public class MerchantSearchServiceImpl implements MerchantSearchService {

    @Value("${es.merchant.index.name}")
    private String indexName;

    @Resource
    private EsWrapper commonEsWrapper;

    private static final String DEFAULT_TYPE = "type";

    private static final List<String> SCRIPT_SORT_KEYS = Arrays.asList("today_order_publish_num", "today_order_finish_num");

    private static final String SORT_SCRIPT =
        "if(doc['%s'].size() == 0 || doc['%s_date'].size() == 0) {\n" +
            "\treturn 0;\n" +
            "}\n" +
            "if (doc['%s'].value == null || doc['%s_date'].value == null) {\n" +
            "\treturn 0;\n" +
            "}\n" +
            "String stateDate = doc['%s_date'].value;\n" +
            "String todayDateStr = params.todayDateStr;\n" +
            "if(todayDateStr.equals(stateDate)) {\n" +
            "\treturn doc['%s'].value;\t\n" +
            "}\n" +
            "return 0;";

    private static final Map<String, String> SORT_SCRIPT_MAP = Maps.newHashMapWithExpectedSize(SCRIPT_SORT_KEYS.size());

    static {
        for (String fieldKey : SCRIPT_SORT_KEYS) {
            SORT_SCRIPT_MAP.put(fieldKey, String.format(SORT_SCRIPT, fieldKey, fieldKey, fieldKey, fieldKey, fieldKey, fieldKey));
        }
    }

    @Override
    public SearchResult<MerchantSearchItem> searchMerchant(MerchantSearchParam searchParam) {
        Preconditions.checkArgument(searchParam != null, "入参错误");
        Preconditions.checkArgument(searchParam.getPageNum() != null, "pageNum null");
        Preconditions.checkArgument(searchParam.getPageSize() != null, "pageSize null");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(searchParam.getPageSize());
        searchSourceBuilder.from(commonEsWrapper.transferFrom(searchParam.getPageNum(), searchParam.getPageSize()));

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        setFilter(searchParam, boolQueryBuilder);
        setFuzzy(searchParam, boolQueryBuilder);
        setRange(searchParam, boolQueryBuilder);

        //term
        searchSourceBuilder.query(boolQueryBuilder);

        //sort
        setSort(searchParam, searchSourceBuilder);

        return commonEsWrapper.selectList(new SearchRequest(indexName).source(searchSourceBuilder), MerchantSearchItem.class);
    }

    @Override
    public boolean upsertMerchantDoc(Integer productLine, Long merchantId, Map<String, Object> data) {
        if (productLine == null || merchantId == null || MapUtils.isEmpty(data)) {
            return false;
        }
        UpdateDoc updateDoc = new UpdateDoc(merchantId.toString(), indexName, DEFAULT_TYPE);
        updateDoc.setUpdateDoc(data);
        updateDoc.setDocAsUpsert(true);
        return commonEsWrapper.updateAsync(updateDoc);
    }

    @Override
    public boolean deleteMerchantDoc(Integer productLine, Long merchantId) {
        if (productLine == null || merchantId == null) {
            return false;
        }
        DeleteDoc deleteDoc = new DeleteDoc(merchantId.toString(), indexName, DEFAULT_TYPE);
        return commonEsWrapper.deleteAsync(deleteDoc);
    }

    @Override
    public boolean deleteMerchantDoc(Integer productLine, Long merchantId, String indexName) {
        if (productLine == null || merchantId == null) {
            return false;
        }
        DeleteDoc deleteDoc = new DeleteDoc(merchantId.toString(), indexName, DEFAULT_TYPE);
        return commonEsWrapper.deleteAsync(deleteDoc);
    }

    @Override
    public boolean indexMerchantDoc(Integer productLine, Long merchantId, String data) {
        if (productLine == null || merchantId == null || StringUtils.isBlank(data)) {
            return false;
        }
        IndexDoc indexDoc = new IndexDoc(merchantId.toString(), indexName, DEFAULT_TYPE);
        indexDoc.setIndexDoc(data);
        return commonEsWrapper.indexAsync(indexDoc);
    }

    private void setFilter(MerchantSearchParam searchParam, BoolQueryBuilder boolQueryBuilder) {
        // 分为两个分别是需要匹配的 以及需要排除掉的
        commonEsWrapper.buildFilter(boolQueryBuilder, "market_id", searchParam.getMarketIdList(), null);
        commonEsWrapper.buildFilter(boolQueryBuilder, "merchant_id", searchParam.getMerchantIdList(), null);
    }

    private void setFuzzy(MerchantSearchParam searchParam, BoolQueryBuilder boolQueryBuilder) {
        commonEsWrapper.buildFuzzy(boolQueryBuilder, "merchant_name", searchParam.getMerchantName());
    }

    private void setRange(MerchantSearchParam searchParam, BoolQueryBuilder boolQueryBuilder) {
//        TaskPriorityParam taskPriorityParam = searchParam.getTaskPriority();
//        if (taskPriorityParam != null) {
//            if (taskPriorityParam.getStartLast1() != null) {
//                commonEsWrapper.buildRange(boolQueryBuilder, "task_priority.last1", taskPriorityParam.getStartLast1(), null);
//            }
//            if (taskPriorityParam.getStartLast3() != null) {
//                commonEsWrapper.buildRange(boolQueryBuilder, "task_priority.last3", taskPriorityParam.getStartLast3(), null);
//            }
//            if (taskPriorityParam.getStartLast7() != null) {
//                commonEsWrapper.buildRange(boolQueryBuilder, "task_priority.last7", taskPriorityParam.getStartLast7(), null);
//            }
//            if (taskPriorityParam.getStartLater7() != null) {
//                commonEsWrapper.buildRange(boolQueryBuilder, "task_priority.later7", taskPriorityParam.getStartLater7(), null);
//            }
//        }
    }

    private void setSort(MerchantSearchParam searchParam, SearchSourceBuilder searchSourceBuilder) {
//        if (StringUtils.isBlank(searchParam.getSorts())) {
//            return;
//        }
//        String[] sortArr = searchParam.getSorts().split(",");
//        for (String sortItem : sortArr) {
//            String[] sortItemArr = sortItem.split(" ");
//            if (sortItemArr.length != 2) {
//                continue;
//            }
//            String sortScript = SORT_SCRIPT_MAP.get(sortItemArr[0]);
//            if (sortItemArr.length == 2 && sortScript != null) {
//                Map<String, Object> params = Maps.newHashMap();
//                params.put("todayDateStr", LocalDate.now().toString());
//                Script script = new Script(ScriptType.INLINE, Script.DEFAULT_SCRIPT_LANG, sortScript, params);
//                searchSourceBuilder.sort(new ScriptSortBuilder(script, ScriptSortBuilder.ScriptSortType.NUMBER).order(SortOrder.fromString(sortItemArr[1])));
//            } else {
//                searchSourceBuilder.sort(sortItemArr[0], SortOrder.fromString(sortItemArr[1]));
//            }
//
//        }
    }
}
