package cn.mic.cloud.mybatis.plus.core;

/**
 * @author : YangQingJian
 * @date : 2022/12/22
 */

import cn.mic.cloud.freamework.common.core.BaseEntity;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 注意参考：cn.mic.cloud.web.core.BaseEntityFeign
 * @param <T>
 */
public interface BaseEntityService<T extends BaseEntity>  {

    /**
     * 新增或者更新
     *
     * @param entity
     */
    @ApiOperation("新增或者更新")
    @PostMapping("/saveOrUpdate")
    T saveOrUpdate(@RequestBody T entity);

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    @ApiOperation("根据ID查询")
    @GetMapping("/selectById/{id}")
    T selectById(@PathVariable("id") Serializable id);

    /**
     * 根据ID删除
     *
     * @param id
     * @param operator
     */
    @ApiOperation("根据ID删除")
    @DeleteMapping("/deleteById/{id}/{operator}")
    void deleteById(@PathVariable("id") Serializable id, @PathVariable("operator") String operator);

    /**
     * 根据对象实体查询列表
     *
     * @param entity
     * @return
     */
    @ApiOperation("查询列表(不分页)")
    @PostMapping(value = "/list")
    List<T> list(@RequestBody T entity);

    /**
     * 分页查询数据
     *
     * @param condition
     * @return
     */
    @ApiOperation("分页查询")
    @PostMapping(value = "/search")
    Page<T> search(@RequestBody T condition, @RequestParam("current") int current, @RequestParam("pageSize") int pageSize);

    /**
     * 批量删除
     *
     * @param ids
     * @param operator
     */
    @ApiOperation("批量删除")
    @DeleteMapping("/batchDel/{operator}")
    void deleteBatch(@RequestBody List<Serializable> ids, @PathVariable("operator") String operator);

    /**
     * 批量更新
     *
     * @param entityList
     * @param operator
     */
    @ApiOperation("批量保存")
    @PostMapping(value = "/saveOrUpdateBatch/{operator}")
    List<T> saveOrUpdateBatch(@RequestBody List<T> entityList, @PathVariable("operator") String operator);


    /**
     * 批量查询
     *
     * @param idList
     * @return
     */
    @PostMapping("/selectBatchIds")
    @ApiOperation("批量查询")
    List<T> selectBatchIds(@RequestBody List<? extends Serializable> idList);

}
