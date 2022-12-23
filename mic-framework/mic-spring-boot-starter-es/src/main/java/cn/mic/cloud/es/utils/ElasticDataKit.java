package cn.mic.cloud.es.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.mic.cloud.es.config.ElasticProperties;
import cn.mic.cloud.es.model.AbstractEsModel;
import cn.mic.cloud.es.request.BaseEsRequestVo;
import cn.mic.cloud.es.request.EsSearchPageRequestVo;
import cn.mic.cloud.es.response.EsSearchPageAggreationsResponseVo;
import cn.mic.cloud.es.response.EsSearchResponseVo;
import cn.mic.cloud.freamework.common.exception.SystemException;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 搜索引擎数据操作
 *
 * @author yangqingjian
 * @date 2020-11-21
 */
@Slf4j
@Component
public class ElasticDataKit {

    /**
     * rest
     */
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 配置文件
     */
    @Autowired
    private ElasticProperties elasticProperties;

    /**
     * 新增或者插入
     *
     * @param idxName
     * @param entity
     */
    public <T extends AbstractEsModel> void insertOrUpdateOne(String idxName, T entity) {
        Assert.hasText(idxName, "索引名称不能为空");
        Assert.notNull(entity, "对象不能为空");
        IndexRequest request = new IndexRequest(idxName);
        request.id(String.valueOf(entity.getId()));
        request.source(JSON.toJSONString(entity), XContentType.JSON);
        /**
         *  可以指定如下
         *  request.source(JSON.toJSONString(entity.getData()), XContentType.JSON)
         */
        try {
            restHighLevelClient.index(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("com.ihome.common.es.ElasticKit.insertOrUpdateOne,e={}", e);
            throw new SystemException("新增或者插入索引【%s】出现异常，异常信息【%s】", idxName, e.getMessage());
        }
    }


    /**
     * 批量插入数据
     * 如果ID已存在，则不会插入，也不会更新数据
     *
     * @param idxName
     * @param list
     */
    public <T extends AbstractEsModel> void insertBatch(String idxName, List<T> list) {
        Assert.hasText(idxName, "索引名称不能为空");
        Assert.notEmpty(list, "集合不能为空");
        BulkRequest request = new BulkRequest();
        list.forEach(item ->
                request.add(new IndexRequest(idxName)
                        .id(String.valueOf(item.getId()))
                        .source(JSON.toJSONString(item), XContentType.JSON))
        );
        try {
            restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("com.ihome.common.es.ElasticKit.insertBatch,e={}", e);
            throw new SystemException("批量插入索引【%s】出现异常，异常信息【%s】", idxName, e.getMessage());
        }
    }

    /**
     * 批量更新数据
     * 如果ID不存在，更新也不会报错
     *
     * @param idxName
     * @param list
     */
    public <T extends AbstractEsModel> void updateBatch(String idxName, List<T> list) {
        Assert.hasText(idxName, "索引名称不能为空");
        Assert.notEmpty(list, "集合不能为空");
        BulkRequest request = new BulkRequest();
        list.forEach(item ->
                request.add(new UpdateRequest(idxName, item.getId().toString()).doc(JSON.toJSONString(item), XContentType.JSON))
        );
        try {
            restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("com.ihome.common.es.ElasticKit.insertBatch,e={}", e);
            throw new SystemException("批量更新索引【%s】出现异常，异常信息【%s】", idxName, e.getMessage());
        }
    }

    /**
     * 批量删除
     *
     * @param idxName
     * @param idList
     * @param <T>
     */
    public <T> void deleteBatch(String idxName, Collection<T> idList) {
        Assert.hasText(idxName, "索引名称不能为空");
        Assert.notEmpty(idList, "集合不能为空");
        BulkRequest request = new BulkRequest();
        idList.forEach(item -> request.add(new DeleteRequest(idxName, item.toString())));
        try {
            restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("com.ihome.common.es.ElasticKit.deleteBatch,e={}", e);
            throw new SystemException("批量删除索引【%s】出现异常，异常信息【%s】", idxName, e.getMessage());
        }
    }

    /**
     * 根据ID进行查询
     *
     * @param idxName
     * @param id
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T findById(String idxName, Serializable id, Class<T> clazz) {
        Assert.hasText(idxName, "索引名称不能为空");
        Assert.notNull(id, "id不能为空");
        Assert.notNull(clazz, "clazz不能为空");
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.termQuery("id", id));
        builder.size(1);
        EsSearchResponseVo searchResult = getSearchResult(idxName, builder, clazz);
        if (ObjectUtil.isNotNull(searchResult) && ObjectUtil.isNotEmpty(searchResult.getList())) {
            return (T) searchResult.getList().get(0);
        }
        return null;
    }

    /**
     * 查询
     *
     * @param idxName
     * @param ids
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> findByIds(String idxName, List<? extends Serializable> ids, Class<T> clazz) {
        Assert.hasText(idxName, "索引名称不能为空");
        Assert.notEmpty(ids, "id不能为空");
        Assert.notNull(clazz, "clazz不能为空");
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.termsQuery("id", ids));
        builder.size(ids.size());
        EsSearchResponseVo searchResult = getSearchResult(idxName, builder, clazz);
        List<T> resultList = Lists.newArrayList();
        if (ObjectUtil.isNotNull(searchResult) && ObjectUtil.isNotEmpty(searchResult.getList())) {
            resultList = searchResult.getList();
        }
        return resultList;
    }

    /**
     * 查询（如果要设置分页数据，请注意设置from和to）
     * 期中from是从0开始
     * 如果未设置分页参数，最多返回ElasticProperties.maxResults
     *
     * @param baseSearchVo
     * @return
     */
    public <T> List<T> search(BaseEsRequestVo baseSearchVo) {
        EsSearchResponseVo searchResults = searchAggregations(baseSearchVo);
        return searchResults.getList();
    }

    /**
     * 搜索结果并且带统计结果
     * @param baseSearchVo
     * @param <T>
     * @return
     */
    public <T> EsSearchResponseVo searchAggregations(BaseEsRequestVo baseSearchVo){
        Assert.notNull(baseSearchVo, "请求参数不能为空");
        SearchSourceBuilder builder = baseSearchVo.getSourceBuilder();
        String idxName = baseSearchVo.getIndexName();
        Class<T> clazz = baseSearchVo.getClazz();
        Assert.hasText(idxName, "索引名称不能为空");
        Assert.notNull(clazz, "clazz不能为空");
        EsSearchResponseVo searchResults = getSearchResult(idxName, builder, clazz);
        return searchResults;
    }

    /**
     * 数据组装
     *
     * @param idxName
     * @param builder
     * @param clazz
     * @param <T>
     * @return
     */
    private <T> EsSearchResponseVo getSearchResult(String idxName, SearchSourceBuilder builder, Class<T> clazz) {
        EsSearchResponseVo result = new EsSearchResponseVo();
        /**
         * 设置最大返回值
         */
        if (builder.size() <= 0) {
            builder.size(elasticProperties.getMaxResultWindow());
        }
        SearchRequest request = new SearchRequest(idxName);
        request.source(builder);
        try {
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            SearchHit[] hits = response.getHits().getHits();
            if (ObjectUtil.isNotEmpty(hits)) {
                List<T> list = new ArrayList<>(hits.length);
                for (SearchHit hit : hits) {
                    list.add(JSON.parseObject(hit.getSourceAsString(), clazz));
                }
                result.setList(list);
                long value = response.getHits().getTotalHits().value;
                result.setTotal(value);
                /**
                 * 数据统计分析
                 */
                Aggregations aggregations = response.getAggregations();
                if (ObjectUtil.isNotNull(aggregations)) {
                    result.setAggregations(aggregations);
                }
            }
            return result;
        } catch (Exception e) {
            log.error("com.ihome.common.es.ElasticKit.search,e={}", e);
            throw new SystemException("查询索引【%s】出现异常，异常信息【%s】", idxName, e.getMessage());
        }
    }

    /**
     * 分页查询
     *
     * @param basePageSearchVo
     * @return
     */
    public <T> EsSearchPageAggreationsResponseVo<T> searchPageAggregations(EsSearchPageRequestVo basePageSearchVo) {
        EsSearchResponseVo searchResults = getElasticSearchResultVo(basePageSearchVo);
        Page<T> page = new Page<>(basePageSearchVo.getCurrent(), basePageSearchVo.getPageSize());
        page.setTotal(searchResults.getTotal());
        page.setRecords(searchResults.getList());
        EsSearchPageAggreationsResponseVo result = new EsSearchPageAggreationsResponseVo();
        result.setAggregations(searchResults.getAggregations());
        result.setPage(page);
        return result;
    }

    private EsSearchResponseVo getElasticSearchResultVo(EsSearchPageRequestVo basePageSearchVo) {
        Assert.notNull(basePageSearchVo, "请求参数不能为空");
        Assert.hasText(basePageSearchVo.getIndexName(), "索引名称不能为空");
        Assert.notNull(basePageSearchVo.getClazz(), "clazz不能为空");
        Assert.isTrue(basePageSearchVo.getCurrent() >= 1, "当前页必须大于等于1");
        Assert.isTrue(basePageSearchVo.getPageSize() >= 1, "每页显示条数必须大于1");
        SearchSourceBuilder builder = basePageSearchVo.getSourceBuilder();
        if (null == builder) {
            builder = new SearchSourceBuilder();
        }
        builder.from(basePageSearchVo.getCurrent() - 1);
        builder.size(basePageSearchVo.getPageSize());
        return getSearchResult(basePageSearchVo.getIndexName(), builder, basePageSearchVo.getClazz());
    }

    /**
     * 分页查询
     *
     * @param basePageSearchVo
     * @return
     */
    public <T> Page<T> searchPage(EsSearchPageRequestVo basePageSearchVo) {
        EsSearchResponseVo searchResults = getElasticSearchResultVo(basePageSearchVo);
        Page<T> page = new Page<>(basePageSearchVo.getCurrent(), basePageSearchVo.getPageSize());
        page.setTotal(searchResults.getTotal());
        page.setRecords(searchResults.getList());
        return page;
    }


    /**
     * 删除指定的
     *
     * @param idxName
     * @param builder
     */
    public void deleteByQuery(String idxName, QueryBuilder builder) {
        DeleteByQueryRequest request = new DeleteByQueryRequest(idxName);
        request.setQuery(builder);
        /**
         * 设置批量操作数量,最大为10000
         */
        request.setBatchSize(elasticProperties.getBatchSize());
        request.setConflicts("proceed");
        try {
            restHighLevelClient.deleteByQuery(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("com.ihome.common.es.ElasticKit.deleteByQuery,e={}", e);
            throw new SystemException("删除指定查询的索引【%s】出现异常，异常信息【%s】", idxName, e.getMessage());
        }
    }



}
