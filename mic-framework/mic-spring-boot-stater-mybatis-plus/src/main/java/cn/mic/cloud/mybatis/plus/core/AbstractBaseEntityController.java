package cn.mic.cloud.mybatis.plus.core;

import cn.mic.cloud.freamework.common.core.LoginInfoUtils;
import cn.mic.cloud.freamework.common.vos.Result;
import cn.mic.cloud.mybatis.plus.entity.BaseEntity;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

/**
 * @author : YangQingJian
 * @date : 2022/12/22
 */
public abstract class AbstractBaseEntityController<S extends BaseEntityService<T>, T extends BaseEntity> {

    private BaseEntityFeign<T> baseEntityFeign;
    public AbstractBaseEntityController(BaseEntityFeign<T> baseEntityFeign){
        this.baseEntityFeign = baseEntityFeign;
    }

    /**
     * 根据ID删除
     *
     * @param id 主键ID
     * @return
     */
    @ApiOperation("根据ID删除")
    @DeleteMapping("/deleteById/{id}")
    public Result<String> deleteById(@PathVariable("id") Serializable id) {
        String workNumber = LoginInfoUtils.getUserId();
        baseEntityFeign.deleteById(id, workNumber);
        return Result.ok();
    }

    /**
     * 保存
     *
     * @param entity
     * @return
     */
    @ApiOperation("保存")
    @PostMapping("/saveOrUpdate")
    public Result<T> saveOrUpdate(@RequestBody T entity) {
        String workNumber = LoginInfoUtils.getUserId();
        entity.fillOperationInfo(workNumber);
        return (Result<T>) Result.ok(baseEntityFeign.saveOrUpdate(entity));
    }

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     * @throws Exception
     */
    @ApiOperation("根据ID查询")
    @GetMapping("/selectById/{id}")
    public Result<T> selectById(@PathVariable("id") Serializable id) {
        return (Result<T>) Result.ok(baseEntityFeign.selectById(id));
    }

    /**
     * 分页查询
     *
     * @param condition
     * @param currentPage
     * @param pageSize
     * @return
     */
    @ApiOperation("分页查询")
    @PostMapping("/search/{currentPage}/{pageSize}")
    public Result<Page<T>> search(@RequestBody T condition, @PathVariable("currentPage") int currentPage, @PathVariable("pageSize") int pageSize) {
        return Result.ok(baseEntityFeign.search(condition, currentPage, pageSize));
    }

}
