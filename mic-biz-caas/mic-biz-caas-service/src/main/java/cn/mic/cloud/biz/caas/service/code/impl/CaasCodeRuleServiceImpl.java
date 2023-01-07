package cn.mic.cloud.biz.caas.service.code.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.mic.cloud.biz.caas.config.CaasCodeRuleConfig;
import cn.mic.cloud.biz.caas.domain.code.CaasCodeRule;
import cn.mic.cloud.biz.caas.domain.code.CaasCodeSeriesNumber;
import cn.mic.cloud.biz.caas.service.code.CaasCodeRuleService;
import cn.mic.cloud.biz.caas.service.code.CaasCodeSeriesNumberService;
import cn.mic.cloud.freamework.common.exception.BusinessException;
import cn.mic.cloud.freamework.common.exception.InvalidParameterException;
import cn.mic.cloud.freamework.common.exception.SystemException;
import cn.mic.cloud.mybatis.plus.core.BaseEntityServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.concurrent.TimeUnit;

/**
 * 编码规则 service实现接口
 *
 * @author : YangQingJian
 * @date : 2023/1/6
 */
@Service
@Slf4j
public class CaasCodeRuleServiceImpl extends BaseEntityServiceImpl<CaasCodeRule> implements CaasCodeRuleService {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private CaasCodeRuleFacade caasCodeRuleFacade;

    @Autowired
    private CaasCodeRuleConfig caasCodeRuleConfig;

    @Autowired
    private CaasCodeSeriesNumberService caasCodeSeriesNumberService;

    /**
     * 修改时强制校验数据
     *
     * @param entity
     */
    @Override
    protected void beforeCheckSaveOrUpdate(CaasCodeRule entity) {
        if (ObjectUtil.isNull(entity.getId())) {
            return;
        }
        Assert.hasText(entity.getCode(), "编码不能为空");
        CaasCodeSeriesNumber caasCodeSeriesNumber = new CaasCodeSeriesNumber();
        caasCodeSeriesNumber.setCodeRuleId(entity.getId());
        long count = caasCodeSeriesNumberService.count(caasCodeSeriesNumber);
        if (count > 0) {
            throw new BusinessException("当前code[%s]已经产生了明细数据，请联系系统管理员", entity.getCode());
        }
    }

    @Override
    protected void beforeSearchWrapper(LambdaQueryWrapper<CaasCodeRule> wrapper, CaasCodeRule condition) {
        /**
         * 备注模糊查询
         */
        if (StrUtil.isNotBlank(condition.getRemark())) {
            wrapper.like(CaasCodeRule::getRemark, condition.getRemark());
            condition.setRemark(null);
        }
    }

    /**
     * 编码生成
     *
     * @param code
     * @param bizKey
     * @return
     */
    @Override
    public String generateCode(String code, String bizKey) {
        if (ObjectUtil.isEmpty(code)) {
            throw new InvalidParameterException("key不能为空");
        }
        /**
         * 去除空格
         */
        code = code.trim();
        bizKey = ObjectUtil.isNotEmpty(bizKey) ? bizKey.trim() : bizKey;
        String message = "";
        RLock lock = redissonClient.getLock(code + bizKey);
        for (int i = 0; i < caasCodeRuleConfig.getGenerateCodeRetryTimes(); i++) {
            try {
                lock.lock(30L, TimeUnit.SECONDS);
                return caasCodeRuleFacade.generateCodeByJava(code, bizKey);
            } catch (BusinessException e) {
                log.error("code={},biz_key={}获取编码失败，错误信息={}", code, bizKey, e.getMessage());
                throw e;
            } catch (InvalidParameterException e) {
                log.error("code={},biz_key={}获取编码失败，错误信息={}", code, bizKey, e.getMessage());
                throw e;
            } catch (Exception e) {
                message = e.getMessage();
                log.error(e.getMessage());
            } finally {
                if (lock.isLocked()) {
                    if (lock.isHeldByCurrentThread()) {
                        lock.unlock();
                    }
                }
            }
        }
        throw new SystemException("code[%s]bizKey[%s]生成编码失败,错误信息[%s]", code, bizKey, message);
    }
}
