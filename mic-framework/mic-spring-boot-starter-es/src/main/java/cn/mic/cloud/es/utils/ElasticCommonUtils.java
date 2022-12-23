package cn.mic.cloud.es.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.List;

/**
 * 搜索引擎工具类
 *
 * @author yangqingjian
 * @date 2020/11/26
 */
public class ElasticCommonUtils {

    /**
     * 数据转换
     *
     * @param current    当前页
     * @param pageSize   每页显示条数
     * @param searchHits 搜索结果
     * @param clazz      目标数据
     * @param <S>        原始数据
     * @param <T>        目标数据
     * @return
     */
    public static <S, T> Page<T> convertViewPage(int current, int pageSize, SearchHits<S> searchHits, Class<T> clazz) {
        Page<T> resultPage = new Page<>(current, pageSize);
        long total = searchHits.getTotalHits();
        resultPage.setTotal(total);
        List<SearchHit<S>> searchHitList = searchHits.getSearchHits();
        List targetList = Lists.newArrayList();
        if (ObjectUtil.isEmpty(searchHitList)) {
            searchHitList.forEach(temp -> {
                if (ObjectUtil.isNotNull(temp.getContent())) {
                    targetList.add(BeanUtil.copyProperties(temp.getContent(), clazz));
                }
            });
        }
        resultPage.setRecords(targetList);
        return resultPage;
    }


}
