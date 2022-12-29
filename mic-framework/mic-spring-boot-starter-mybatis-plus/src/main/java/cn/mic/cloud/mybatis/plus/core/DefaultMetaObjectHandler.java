package cn.mic.cloud.mybatis.plus.core;

import cn.mic.cloud.freamework.common.core.BaseEntity;
import cn.mic.cloud.freamework.common.core.login.LoginInfoUtils;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * @Author simon
 * @Description DefaultMetaObjectHandler
 * @Date 2020/4/30
 */
@Slf4j
public class DefaultMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        if (log.isDebugEnabled()) {
            log.debug("start insert fill for time columns...");
        }
        this.setFieldValByName(BaseEntity.CREATED_TIME, new Date(), metaObject);
        this.setFieldValByName(BaseEntity.IS_DELETED, 0, metaObject);
        this.setFieldValByName(BaseEntity.LAST_UPDATED_TIME, new Date(), metaObject);
        if (LoginInfoUtils.getLoginName() != null && this.getFieldValByName(BaseEntity.CREATED_BY, metaObject) == null) {
            this.setFieldValByName(BaseEntity.CREATED_BY, String.valueOf(LoginInfoUtils.getLoginName()), metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (log.isDebugEnabled()) {
            log.debug("start updating fill for time columns...");
        }
        this.setFieldValByName(BaseEntity.LAST_UPDATED_TIME, new Date(), metaObject);
        if (LoginInfoUtils.getLoginName() != null && this.getFieldValByName(BaseEntity.MODIFIED_BY, metaObject) == null) {
            this.setFieldValByName(BaseEntity.MODIFIED_BY, String.valueOf(LoginInfoUtils.getLoginName()), metaObject);
        }
    }
}
