package cn.mic.cloud.es.utils;

import cn.mic.cloud.es.config.ElasticProperties;
import cn.mic.cloud.es.vo.IdxVo;
import cn.mic.cloud.freamework.common.exception.BusinessException;
import cn.mic.cloud.freamework.common.exception.SystemException;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.IOException;
import java.text.MessageFormat;

/**
 * 搜索引擎索引操作
 *
 * @author yangqingjian
 * @date 2020-11-21
 */
@Slf4j
@Component
public class ElasticIndexKit {

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
     * 创建索引(一旦创建好就不允许修改)
     *
     * @param idxVo
     */
    public void createIndex(IdxVo idxVo) {
        try {
            Assert.notNull(idxVo, "idxVo对象不能为空");
            Assert.hasText(idxVo.getIdxName(), "索引名称不能为空");
            Assert.notNull(idxVo.getIdxSql(), "索引的sql不能为空");
            String idxName = idxVo.getIdxName();
            String idxSQL = JSON.toJSONString(idxVo.getIdxSql());
            if (this.isExistsIndex(idxName)) {
                log.error(" idxName={} 已经存在,idxSql={}", idxName, idxSQL);
                throw new Exception("索引已存在");
            }
            CreateIndexRequest request = new CreateIndexRequest(idxName);
            buildSettings(request);
            request.mapping(idxSQL, XContentType.JSON);
            /**
             * request.settings() 可以手工指定Setting
             */
            CreateIndexResponse res = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
            if (!res.isAcknowledged()) {
                throw new SystemException("创建索引失败");
            }
        } catch (Exception e) {
            throw new SystemException("创建索引【%s】出现异常，异常信息【%s】", idxVo.getIdxName(), e.getMessage());
        }
    }

    private void buildSettings(CreateIndexRequest request) {
        request.settings(
                Settings.builder()
                        .put("index.number_of_shards", elasticProperties.getShardsNumbers())
                        .put("index.number_of_replicas", elasticProperties.getReplicasNumbers())
                        .put("index.max_result_window", elasticProperties.getMaxResultWindow())
                        .put("index.refresh_interval", elasticProperties.getRefreshInterval())
        );
    }

    /**
     * 判断某个index是否存在
     *
     * @param idxName
     * @return
     * @throws Exception
     */
    public boolean isExistsIndex(String idxName) {
        Assert.hasText(idxName, "索引名称不能为空");
        try {
            return restHighLevelClient.indices().exists(new GetIndexRequest(idxName), RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            throw new SystemException("判断索引【%s】出现异常，异常信息【%s】", idxName, e.getMessage());
        }
    }


    /**
     * 删除index
     *
     * @param idxName
     */
    public void deleteIndex(String idxName) {
        Assert.hasText(idxName, "索引名称不能为空");
        if (!this.isExistsIndex(idxName)) {
            log.error(" idxName={} 已经不存在", idxName);
            throw new BusinessException(MessageFormat.format("idxName={0} 已经不存在", idxName));
        }
        try {
            restHighLevelClient.indices().delete(new DeleteIndexRequest(idxName), RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("com.ihome.common.es.ElasticKit.deleteIndex,e={}", e);
            throw new SystemException("删除索引【%s】出现异常，异常信息【%s】", idxName, e.getMessage());
        }
    }


}
