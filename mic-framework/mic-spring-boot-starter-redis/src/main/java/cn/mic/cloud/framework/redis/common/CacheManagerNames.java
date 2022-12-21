package cn.mic.cloud.framework.redis.common;

/**
 * @author yangqingjian
 */
public interface CacheManagerNames {
    /**
     * 缓存时长 5分钟
     */
    String REDIS_300S_CACHE_MANAGER = "redis5mCacheManager";
    /**
     * 缓存时长 1天
     */
    String REDIS_1D_CACHE_MANAGER = "redis1dCacheManager";
    /**
     * 缓存时长 2天
     */
    String REDIS_2D_CACHE_MANAGER = "redis2dCacheManager";
    /**
     * 缓存时长 7天
     */
    String REDIS_7D_CACHE_MANAGER = "redis7dCacheManager";
    /**
     * 缓存时长 1月
     */
    String REDIS_1M_CACHE_MANAGER = "redis1MCacheManager";
}
