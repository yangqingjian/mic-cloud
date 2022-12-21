package cn.mic.cloud.framework.redis.comp;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author yangqingjian
 */
@Component
public class RedisKit {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, String value, long ms, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, ms, timeUnit);
    }

    public String getStr(String key) {
        return redisTemplate.opsForValue().get(key);
    }


    public <T> T getObj(String key, Class<T> clzz) {
        String val = redisTemplate.opsForValue().get(key);
        if (StrUtil.isNotBlank(val)) {
            return JSONObject.parseObject(val, clzz);
        }
        return null;
    }

    public <T> List<T> getList(String key, Class<T> clzz) {
        String val = redisTemplate.opsForValue().get(key);
        if (StrUtil.isNotBlank(val)) {
            return JSON.parseArray(val, clzz);
        }
        return null;
    }

    public boolean remove(String key) {
        Boolean delete = redisTemplate.delete(key);
        return delete != null ? delete : false;
    }

    /**
     * 判断 健 是否存在
     *
     * @param key
     * @return true键已存在；否则不存在
     */
    public boolean has(String key) {
        Boolean hasKey = redisTemplate.hasKey(key);
        return hasKey != null ? hasKey : true;
    }
}
