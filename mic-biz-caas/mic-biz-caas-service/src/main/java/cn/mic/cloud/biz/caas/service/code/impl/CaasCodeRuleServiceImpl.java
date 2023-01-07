package cn.mic.cloud.biz.caas.service.code.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.mic.cloud.biz.caas.config.CaasCodeRuleConfig;
import cn.mic.cloud.biz.caas.domain.code.CaasCodeRule;
import cn.mic.cloud.biz.caas.service.CaasCodeRuleService;
import cn.mic.cloud.freamework.common.exception.BusinessException;
import cn.mic.cloud.freamework.common.exception.InvalidParameterException;
import cn.mic.cloud.freamework.common.exception.SystemException;
import cn.mic.cloud.mybatis.plus.core.BaseEntityServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
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

    /**
     * 编码生成
     *
     * @param key
     * @param bizKey
     * @return
     */
    @Override
    public String generateCode(String key, String bizKey) {
        if (ObjectUtil.isEmpty(key)) {
            throw new InvalidParameterException("key不能为空");
        }
        /**
         * 去除空格
         */
        key = key.trim();
        bizKey = ObjectUtil.isNotEmpty(bizKey) ? bizKey.trim() : bizKey;
        String message = "";
        RLock lock = redissonClient.getLock(key + bizKey);
        for (int i = 0; i < caasCodeRuleConfig.getGenerateCodeRetryTimes(); i++) {
            try {
                lock.lock(30L, TimeUnit.SECONDS);
                return caasCodeRuleFacade.generateCodeByJava(key, bizKey);
            } catch (BusinessException e) {
                log.error("key={},biz_key={}获取编码失败，错误信息={}", key, bizKey, e.getMessage());
                throw e;
            } catch (InvalidParameterException e) {
                log.error("key={},biz_key={}获取编码失败，错误信息={}", key, bizKey, e.getMessage());
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
        throw new SystemException(MessageFormat.format("生成编码失败:{0}", message));
    }
}
