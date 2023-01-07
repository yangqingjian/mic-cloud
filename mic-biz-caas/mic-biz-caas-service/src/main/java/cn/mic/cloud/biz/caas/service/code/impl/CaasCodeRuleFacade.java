package cn.mic.cloud.biz.caas.service.code.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.mic.cloud.biz.caas.config.CaasCodeRuleConfig;
import cn.mic.cloud.biz.caas.domain.code.CaasCodeRule;
import cn.mic.cloud.biz.caas.domain.code.CaasCodeSeriesNumber;
import cn.mic.cloud.biz.caas.repository.code.CaasCodeRuleRepository;
import cn.mic.cloud.biz.caas.repository.code.CaasCodeSeriesNumberRepository;
import cn.mic.cloud.freamework.common.exception.BusinessException;
import cn.mic.cloud.freamework.common.exception.InvalidParameterException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author YangQingJian
 * @date 2020-06-03
 * 编码策略的facade
 */
@Component
@Slf4j
public class CaasCodeRuleFacade {

    @Resource
    private CaasCodeRuleConfig caasCodeRuleConfig;

    @Resource
    private CaasCodeRuleRepository caasCodeRuleRepository;

    @Resource
    private CaasCodeSeriesNumberRepository codeSeriesNumberRepository;

    /**
     * @param key    编码策略的key
     * @param bizKey 业务前缀
     * @return 生成后的编码
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public String generateCodeByJava(String key, String bizKey) throws InvalidParameterException, BusinessException {
        List<CaasCodeRule> codeRuleList = caasCodeRuleRepository.list(new QueryWrapper<CaasCodeRule>().eq("code_key", key));
        if (ObjectUtil.isEmpty(codeRuleList)) {
            throw new InvalidParameterException(MessageFormat.format("无效的key【{0}】", key));
        }
        if (codeRuleList.size() != 1) {
            throw new InvalidParameterException(MessageFormat.format("key【{0}】配置重复", key));
        }
        CaasCodeRule codeRule = codeRuleList.get(0);
        /**
         * 校验规则
         */
        checkCodeRule(key, codeRule);
        LambdaQueryWrapper<CaasCodeSeriesNumber> queryWrapper = new LambdaQueryWrapper<CaasCodeSeriesNumber>();
        queryWrapper.eq(CaasCodeSeriesNumber::getCodeRuleId, codeRule.getId());
        if (ObjectUtil.isNotEmpty(bizKey)) {
            queryWrapper.eq(CaasCodeSeriesNumber::getBizKey, bizKey);
        }
        String periodStr = "";
        if (ObjectUtil.isNotEmpty(codeRule.getPeriod())) {
            periodStr = DateUtil.format(new Date(), caasCodeRuleConfig.getDataBase2JavaMap().get(codeRule.getPeriod()));
            queryWrapper.eq(CaasCodeSeriesNumber::getDateStr , periodStr);
        }
        /**
         * 进行分组
         */
        List<CaasCodeSeriesNumber> insertList = Lists.newArrayList();
        List<String> deleteList = Lists.newArrayList();
        List<CaasCodeSeriesNumber> updateList = Lists.newArrayList();

        Long startNumber = codeRule.getStartNumber();
        List<CaasCodeSeriesNumber> codeSeriesNumberList = codeSeriesNumberRepository.list(queryWrapper);
        if (ObjectUtil.isEmpty(codeSeriesNumberList)) {
            CaasCodeSeriesNumber insertEntity = CaasCodeSeriesNumber.builder().bizKey(bizKey).codeRuleId(codeRule.getId()).dateStr(periodStr).seqNumber(startNumber).build();
            insertEntity.setIsDeleted(0);
            insertList.add(insertEntity);
        } else {
            startNumber = getLongStartNumber(codeRule, deleteList, updateList, codeSeriesNumberList);
        }
        /**
         * bizKey处理
         */
        if (ObjectUtil.isNull(bizKey)) {
            bizKey = "";
        }
        StringBuilder sb = new StringBuilder();
        String preStr = codeRule.getPrefix() == null ? "" : codeRule.getPrefix();
        sb.append(preStr);

        /**
         * 日期格式
         */
        String dateStr = "";
        if (StringUtils.isNotBlank(codeRule.getDateFormat())) {
            dateStr = DateUtil.format(new Date(), caasCodeRuleConfig.getDataBase2JavaMap().get(codeRule.getDateFormat()));
        }

        boolean frontFlag = ObjectUtil.equal(codeRule.getBizkeyFrontDateFlag() , true);
        if (frontFlag) {
            sb.append(bizKey);
            sb.append(dateStr);
        } else {
            sb.append(dateStr);
            sb.append(bizKey);
        }

        /**
         * 序列补0操作
         */
        String targetStartNumber = String.format("%0" + codeRule.getSeqLength() + "d", startNumber);
        sb.append(targetStartNumber);
        /**
         * 编码的长度校验
         */
        if (!caasCodeRuleConfig.getNoNeedCheckLengthCodeList().contains(key)) {
            if (codeRule.getLength().compareTo(sb.length()) != 0) {
                throw new BusinessException(MessageFormat.format("key = {0},规则的长度为{1},实际生成的编码长度为{2}", key, codeRule.getLength(), sb.length()));
            }
        }
        if (ObjectUtil.isNotEmpty(insertList)) {
            codeSeriesNumberRepository.saveBatch(insertList);
        }
        if (ObjectUtil.isNotEmpty(updateList)) {
            codeSeriesNumberRepository.updateBatchById(updateList);
        }
        if (ObjectUtil.isNotEmpty(deleteList)) {
            codeSeriesNumberRepository.removeByIds(deleteList);
        }
        log.info("生成后编码={}", sb.toString());
        return sb.toString();
    }

    /**
     * 校验规则
     * @param key
     * @param codeRule
     */
    private void checkCodeRule(String key, CaasCodeRule codeRule) {
        if (ObjectUtil.isNotEmpty(codeRule.getPeriod()) && !caasCodeRuleConfig.getDataBase2JavaMap().keySet().contains(codeRule.getPeriod())) {
            throw new BusinessException(MessageFormat.format("key=[{0}]配置的周期规则[{1}],格式不符合", codeRule.getCode(), codeRule.getPeriod()));
        }
        if (ObjectUtil.isNotEmpty(codeRule.getDateFormat()) && !caasCodeRuleConfig.getDataBase2JavaMap().keySet().contains(codeRule.getDateFormat())) {
            throw new BusinessException(MessageFormat.format("key=[{0}]配置的日期规则[{1}],格式不符合", codeRule.getCode(), codeRule.getDateFormat()));
        }
        if (ObjectUtil.isNull(codeRule.getBizkeyFrontDateFlag())) {
            codeRule.setBizkeyFrontDateFlag(false);
        }
        if (ObjectUtil.isNull(codeRule.getSeqLength()) || 0 == codeRule.getSeqLength()) {
            throw new InvalidParameterException(MessageFormat.format("key={}的序列长度未设置", key));
        }
    }

    private Long getLongStartNumber(CaasCodeRule codeRule, List<String> deleteList, List<CaasCodeSeriesNumber> updateList, List<CaasCodeSeriesNumber> codeSeriesNumberList) {
        Long startNumber;
        /**
         * 从小到大的排序
         */
        if (codeSeriesNumberList.size() > 1) {
            log.error("需要处理数据");
            /**
             * 自动修复数据
             */
            CollectionUtil.sortByProperty(codeSeriesNumberList, "id");
            String maxId = codeSeriesNumberList.get(codeSeriesNumberList.size() - 1).getId();
            codeSeriesNumberList.stream().filter(temp -> temp.getId().compareTo(maxId) != 0).forEach(temp -> {
                deleteList.add(temp.getId());
            });
            codeSeriesNumberList.removeIf(temp -> temp.getId().compareTo(maxId) != 0);
        }
        /**
         * 计算下一个序列(注意获取当前的序列)
         */
        startNumber = codeSeriesNumberList.get(0).getSeqNumber() + codeRule.getStepLength();
        /**
         * 如果超过了最大长度，直接为初始值
         */
        if (String.valueOf(startNumber).length() > codeRule.getSeqLength()) {
            startNumber = codeRule.getStartNumber();
        }
        codeSeriesNumberList.get(0).setSeqNumber(startNumber);

        updateList.add(codeSeriesNumberList.get(0));
        return startNumber;
    }

    /**
     * 校验当前缓存中的编码（分布式肯定校验不到）
     */
    static class CodeRuleCheck {

        static volatile List<String> dataList = new CopyOnWriteArrayList<String>();

        public static void addAndCheck(String code, int cacheListSize) throws Exception {
            if (dataList.contains(code)) {
                throw new Exception(MessageFormat.format("编码={0}，已经生成过", code));
            }
            if (dataList.size() >= cacheListSize) {
                dataList.remove(0);
            }
            dataList.add(code);
        }

    }


}
