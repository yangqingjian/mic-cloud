package cn.mic.cloud.mybatis.plus.core;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.mic.cloud.freamework.common.exception.BusinessException;
import cn.mic.cloud.freamework.common.exception.InvalidParameterException;
import cn.mic.cloud.mybatis.plus.constants.MybatisPlusConsts;
import cn.mic.cloud.mybatis.plus.entity.BaseEntity;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.List;

import static cn.mic.cloud.mybatis.plus.constants.MybatisPlusConsts.ENTITY_ID_COLUMN;

/**
 * <B>系统名称：CAAS系统</B><BR>
 * <B>模块名称：vms</B><BR>
 * <B>中文类名：实现基础SERVICE</B><BR>
 * <B>概要说明：</B><BR>
 * <B>@version：v1.0</B><BR>
 * <B>版本		修改人		备注</B><BR>
 *
 * @author : YangQingjian
 * @date : 2018年4月6日
 */
@Slf4j
public class BaseEntityServiceImpl<T extends BaseEntity> implements BaseEntityService<T> {

    @Autowired
    private BaseEntityRepository<T> repository;


    /**
     * 新增或者更新
     *
     * @param entity
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public T saveOrUpdate(T entity) {
        beforeCheckSaveOrUpdate(entity);
        repository.saveOrUpdate(entity);
        postSaveOrUpdate(entity);
        return entity;
    }

    protected void postSaveOrUpdate(T entity) {
    }

    protected void beforeCheckSaveOrUpdate(T entity) {
    }

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    @Override
    public T selectById(Serializable id) {
        T t = repository.getById(id);
        postSelectById(t);
        return t;
    }

    protected void postSelectById(T t) {
    }

    /**
     * 根据ID删除
     *
     * @param id
     * @param operator
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(Serializable id, String operator) {
        beforeDeleteById(id, operator);
        T t = repository.getById(id);
        if (null == t) {
            throw new BusinessException("数据已不存在，无须删除");
        }
        t.fillOperationInfo(operator);
        repository.removeById(t);
    }

    protected void beforeDeleteById(Serializable id, String operator) {
    }

    /**
     * 根据对象实体查询列表
     *
     * @param entity
     * @return
     */
    @Override
    public List<T> list(T entity) {
        beforeList(entity);
        LambdaQueryWrapper<T> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        beforeListWrapper(lambdaQueryWrapper, entity);
        lambdaQueryWrapper.setEntity(entity);
        List<T> list = repository.list(lambdaQueryWrapper);
        postList(list, entity);
        return list;
    }

    protected void postList(List<T> list, T entity) {
    }

    protected void beforeListWrapper(LambdaQueryWrapper<T> lambdaQueryWrapper, T entity) {
    }

    protected void beforeList(T entity) {
    }

    /**
     * 分页查询数据
     *
     * @param condition
     * @param current
     * @param pageSize
     * @return
     */
    @Override
    public Page<T> search(T condition, int current, int pageSize) {
        beforeSearch(condition);
        Page<T> page = new Page<>(current, pageSize);
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<>();
        beforeSearchWrapper(wrapper, condition);
        wrapper.setEntity(condition);
        OrderItem orderItem = beforeSearchSort(condition);
        page.addOrder(orderItem);
        Page<T> resultPage = repository.page(page, wrapper);
        postSearch(resultPage, condition);
        return resultPage;
    }

    /**
     * 组装排序【默认ID倒序】
     *
     * @param condition
     * @return
     */
    protected OrderItem beforeSearchSort(T condition) {
        /**
         * 默认排序问题
         */
        OrderItem orderItem = new OrderItem();
        if (null == condition) {
            orderItem.setColumn(ENTITY_ID_COLUMN);
            orderItem.setAsc(false);
            return orderItem;
        }
        orderItem.setColumn(StrUtil.isBlank(condition.getSortFieldName()) ? ENTITY_ID_COLUMN : condition.getSortFieldName());
        orderItem.setAsc(ObjectUtil.isNull(condition.getSortFieldAscFlag()) ? false : condition.getSortFieldAscFlag());
        return orderItem;
    }

    protected void postSearch(Page<T> resultPage, T condition) {
    }

    protected void beforeSearchWrapper(LambdaQueryWrapper<T> wrapper, T condition) {
    }

    protected void beforeSearch(T condition) {
    }

    /**
     * 批量删除
     *
     * @param ids
     * @param operator
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteBatch(List<Serializable> ids, String operator) {
        Assert.notEmpty(ids, "ids不能为空");
        Assert.hasText(operator, "操作人不能为空");
        beforeCheckDeleteBatch(ids, operator);
        List<T> srcList = this.repository.listByIds(ids);
        if (srcList == null) {
            throw new InvalidParameterException("根据沒有查到对应数据");
        }
        if (srcList.size() != ids.size()) {
            throw new InvalidParameterException("id集合数量为[%s]数据库中的集合条数为[%s]，数量一致不能更新", ids.size(), srcList.size());
        }
        srcList.forEach(temp -> {
            temp.setIsDeleted(1);
            temp.fillOperationInfo(operator);
        });
        this.repository.updateBatchById(srcList, MybatisPlusConsts.MAX_UPDATE_SIZE);
        postDeleteBatch(srcList);
    }

    protected void postDeleteBatch(List<T> srcList) {
    }

    protected void beforeCheckDeleteBatch(List<Serializable> ids, String operator) {
    }

    /**
     * 批量更新
     *
     * @param entityList
     * @param operator
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<T> saveOrUpdateBatch(List<T> entityList, String operator) {
        Assert.notEmpty(entityList, "entityList不能为空");
        Assert.hasText(operator, "操作人不能为空");
        beforeSaveOrUpdateBatch(entityList, operator);
        entityList.forEach(temp -> {
            temp.fillOperationInfo(operator);
        });
        this.repository.saveOrUpdateBatch(entityList, MybatisPlusConsts.MAX_UPDATE_SIZE);
        postSaveOrUpdateBatch(entityList, operator);
        return entityList;
    }

    protected void postSaveOrUpdateBatch(List<T> entityList, String operator) {
    }

    protected void beforeSaveOrUpdateBatch(List<T> entityList, String operator) {
    }

    /**
     * 批量查询
     *
     * @param idList
     * @return
     */
    @Override
    public List<T> selectBatchIds(List<? extends Serializable> idList) {
        Assert.notEmpty(idList, "idList不能为空");
        List<T> list = this.repository.listByIds(idList);
        postSelectBatchIds(list);
        return list;
    }

    protected void postSelectBatchIds(List<T> list) {
    }

}
