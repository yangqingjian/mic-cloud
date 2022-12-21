package cn.mic.cloud.freamework.common.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.mic.cloud.freamework.common.constants.CommonEnum;
import cn.mic.cloud.freamework.common.vos.EnumVo;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * 枚举工具类
 *
 * @author yangqingjian
 * @date 2022/12/21
 */
public class EnumUtils {
    /**
     * 通过编码获取枚举描述
     *
     * @param clazz
     * @param code
     * @param <T>
     * @return
     */
    public static <T extends CommonEnum> String getDescByCode(Class<T> clazz, String code) {
        if (ObjectUtil.isEmpty(code)) {
            return null;
        }
        for (T _enum : clazz.getEnumConstants()) {
            if (code.equals(_enum.getCode())) {
                return _enum.getDesc();
            }
        }
        return null;
    }

    /**
     * 通过描述获取编码
     *
     * @param clazz
     * @param desc
     * @param <T>
     * @return
     */
    public static <T extends CommonEnum> String getCodeByDesc(Class<T> clazz, String desc) {
        for (T _enum : clazz.getEnumConstants()) {
            if (_enum.getDesc().equals(desc)) {
                return _enum.getCode();
            }
        }
        return null;
    }

    /**
     * 获取枚举的所有编码
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T extends CommonEnum> List<String> getListCode(Class<T> clazz) {
        List<String> resultList = Lists.newArrayList();
        for (T _enum : clazz.getEnumConstants()) {
            resultList.add(_enum.getCode());
        }
        return resultList;
    }

    /**
     * 获取枚举的所有描述
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T extends CommonEnum> List<String> getListDesc(Class<T> clazz) {
        List<String> resultList = Lists.newArrayList();
        for (T _enum : clazz.getEnumConstants()) {
            resultList.add(_enum.getDesc());
        }
        return resultList;
    }

    /**
     * 获取所有枚举值
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T extends CommonEnum> List<EnumVo> getListEnum(Class<T> clazz) {
        List<EnumVo> resultList = Lists.newArrayList();
        for (T _enum : clazz.getEnumConstants()) {
            EnumVo enumVo = new EnumVo();
            enumVo.setCode(_enum.getCode());
            enumVo.setDesc(_enum.getDesc());
            resultList.add(enumVo);
        }
        return resultList;
    }

    /**
     * 通过编码获取枚举值
     *
     * @param clazz
     * @param code
     * @param <T>
     * @return
     */
    public static <T extends CommonEnum> T getEnumByCode(Class<T> clazz, String code) {
        if (ObjectUtil.isEmpty(code)) {
            return null;
        }
        for (T _enum : clazz.getEnumConstants()) {
            if (code.equals(_enum.getCode())) {
                return _enum;
            }
        }
        return null;
    }

}
