package cn.mic.cloud.web.controller;

import cn.hutool.core.util.StrUtil;
import cn.mic.cloud.freamework.common.constants.CommonEnum;
import cn.mic.cloud.freamework.common.exception.InvalidParameterException;
import cn.mic.cloud.freamework.common.utils.EnumUtils;
import cn.mic.cloud.freamework.common.vos.EnumVo;
import cn.mic.cloud.freamework.common.vos.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @Author zw
 * @Description EnumController
 * @Date 2020/7/16
 */
@Slf4j
@RestController
@RequestMapping("/enum")
@Api(tags = "枚举接口")
public class EnumController {

    /**
     * 查询指定的枚举类
     *
     * @param path
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "查询指定的枚举类")
    @PostMapping("/all")
    public Result<List<EnumVo>> all(@RequestParam("path") String path) throws Exception {
        return Result.ok(EnumUtils.getListEnum(getClazzByPath(path)));
    }

    /**
     * 查询指定的枚举类的所有key
     *
     * @param path
     * @return
     */
    @ApiOperation(value = "查询指定的枚举类的所有key")
    @PostMapping("/keys")
    public Result<List<String>> keys(@RequestParam("path") String path) {
        return Result.ok(EnumUtils.getListCode(getClazzByPath(path)));
    }

    /**
     * 查询指定的枚举类的所有value
     *
     * @param path
     * @return
     */
    @ApiOperation(value = "查询指定的枚举类的所有value")
    @PostMapping("/values")
    public Result<List<String>> values(@RequestParam("path") String path) {
        return Result.ok(EnumUtils.getListDesc(getClazzByPath(path)));
    }

    private Class<CommonEnum> getClazzByPath(String path) {
        if (StrUtil.isBlank(path)) {
            throw new InvalidParameterException("路径不能为空");
        }
        Class<CommonEnum> clazz = null;
        try {
            clazz = (Class<CommonEnum>) Class.forName(path);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new InvalidParameterException("枚举【%s】不存在", path);
        }
        return clazz;
    }


}
