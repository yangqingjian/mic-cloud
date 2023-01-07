package cn.mic.cloud.biz.caas.service.basic.impl;


import cn.hutool.core.util.ObjectUtil;
import cn.mic.cloud.biz.caas.domain.basic.CaasRegion;
import cn.mic.cloud.biz.caas.service.basic.CaasRegionService;
import cn.mic.cloud.freamework.common.exception.BusinessException;
import cn.mic.cloud.freamework.common.exception.SystemException;
import cn.mic.cloud.mybatis.plus.core.BaseEntityServiceImpl;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static cn.mic.cloud.biz.caas.domain.constants.CaasConstants.*;

/**
 * 行政区域 service实现接口
 *
 * @author : YangQingJian
 * @date : 2023/1/6
 */
@Service
@Slf4j
public class CaasRegionServiceImpl extends BaseEntityServiceImpl<CaasRegion> implements CaasRegionService {

    private static final int TRY_TIMES = 5;

    /**
     * 初始化省市区（暂时不加事物注解）
     *
     * @return
     * @throws Exception
     */
    @Override
    public String init() {
        if (super.getRepository().count() > 0) {
            throw new BusinessException("已有区域数据，不用重复初始化");
        }
        List<CaasRegion> regionList = Lists.newArrayList();
        Document doc;
        try {
            doc = Jsoup.connect(REGION_TOP_URL).get();
        } catch (IOException e) {
            e.printStackTrace();
            throw new SystemException("解析地址[%s]失败，错误信息[%s]", REGION_TOP_URL, e.getMessage());
        }
        Elements rows = doc.select("table").get(3).select("tr");
        if (rows.size() < REGION_FIRST_ROW_SIZE) {
            log.info("没有结果");
            throw new SystemException("查询的结果错误");
        }
        for (int i = 3; i < rows.size(); i++) {
            Element row = rows.get(i);
            Elements columns = row.select("td");
            for (int j = 0; j < columns.size(); j++) {
                String text = columns.get(j).text();
                String href = columns.get(j).select("a").attr("href");
                if (ObjectUtil.isNotEmpty(text) && ObjectUtil.isNotEmpty(href)) {
                    if (assembleAllInitRegion(regionList, text, href)) {
                        return "操作失败";
                    }
                }
            }
        }
        if (ObjectUtil.isNotEmpty(regionList)) {
            super.getRepository().saveBatch(regionList);
        }
        return "操作成功";
    }

    private boolean assembleAllInitRegion(List<CaasRegion> regionList, String text, String href) {
        Element rowElement;
        Elements colElements;
        String code = href.replace(".html", "") + "0000000000";
        log.info("name = " + text + " code = " + code + " parentCode = ");
        CaasRegion region = CaasRegion.builder().regionCode(code).regionName(text).parentCode(REGION_TOP_PARENT_CODE).build();
        region.fillOperationInfo("admin");
        regionList.add(region);

        String secondHref = REGION_BASE_URL + "/" + href;
        Document secondDoc = null;
        try {
            secondDoc = getDocumentByUrl(secondHref);
        } catch (Exception e) {
            log.error("assembleAllInitRegion异常信息,接口地址{},错误信息{}", href, e.getMessage(), e);
            throw new SystemException("assembleAllInitRegion异常信息,接口地址[%s],错误信息[%s]", href, e.getMessage());
        }
        Elements secondRows = secondDoc.select("table").get(4).select("tr");
        if (secondRows.size() < REGION_SECOND_ROW_SIZE) {
            log.info("没有结果");
            return true;
        }
        for (int x = 1; x < secondRows.size(); x++) {
            rowElement = secondRows.get(x);
            colElements = rowElement.select("td");
            String secondCode = colElements.get(0).text();
            String secondText = colElements.get(1).text();

            CaasRegion secondRegion = CaasRegion.builder().regionCode(secondCode).regionName(secondText).parentCode(code).build();
            secondRegion.setIsDeleted(0);
            secondRegion.setCreatedBy("admin");
            regionList.add(secondRegion);

            log.info("name = " + secondText + " code = " + secondCode + " parentCode = " + code);
            String tempSecondHref = colElements.get(0).select("a").attr("href");
            if (ObjectUtil.isEmpty(tempSecondHref)) {
                continue;
            }
            String thirdHref = REGION_BASE_URL + "/" + tempSecondHref;

            Document thirdDoc = getDocumentByUrl(thirdHref);
            if (thirdDoc.select("table").size() < 5) {
                log.info("html = {}", thirdHref);
                log.info("thirdHtmlText = " + thirdHref);
            }
            Elements thirdRows = thirdDoc.select("table").get(4).select("tr");
            if (thirdRows.size() < REGION_THIRD_ROW_SIZE) {
                log.info("没有结果");
                continue;
            }

            for (int y = 1; y < thirdRows.size(); y++) {
                rowElement = thirdRows.get(y);
                colElements = rowElement.select("td");
                String thirdCode = colElements.get(0).text();
                String thirdText = colElements.get(1).text();
                log.info("name = " + thirdText + " code = " + thirdCode + " parentCode = " + secondCode);
                CaasRegion thirdRegion = CaasRegion.builder().regionCode(thirdCode).regionName(thirdText).parentCode(secondCode).build();
                thirdRegion.setIsDeleted(0);
                thirdRegion.setCreatedBy("admin");
                regionList.add(thirdRegion);
            }
        }
        return false;
    }

    private Document getDocumentByUrl(String url) {
        Document dock = null;
        for (int i = 0; i < TRY_TIMES; i++) {
            try {
                dock = Jsoup.connect(url).get();
                return dock;
            } catch (Exception ex) {
                log.info("第{}次尝试错误：url={}", i + 1, url);
                try {
                    Thread.sleep(2000 * i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return dock;
    }


}
