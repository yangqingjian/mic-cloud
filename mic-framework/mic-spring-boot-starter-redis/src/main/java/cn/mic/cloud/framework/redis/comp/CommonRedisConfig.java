package cn.mic.cloud.framework.redis.comp;

import cn.mic.cloud.framework.redis.common.CacheManagerNames;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * @author yangqingjian
 */
public interface CommonRedisConfig {
    /**
     * 使用方法名及参数作为redis缓存的key值
     *
     * @return
     */
    @Bean
    @Primary
    default RedisCacheManager redisCacheManager(LettuceConnectionFactory lettuceConnectionFactory) {
        return defaultCacheManager(lettuceConnectionFactory, Duration.ofHours(1));
    }

    @Bean(name = CacheManagerNames.REDIS_1D_CACHE_MANAGER)
    default RedisCacheManager redis48hCacheManager(LettuceConnectionFactory lettuceConnectionFactory) {
        return defaultCacheManager(lettuceConnectionFactory, Duration.ofDays(1));
    }

    @Bean(name = CacheManagerNames.REDIS_300S_CACHE_MANAGER)
    default RedisCacheManager redis300sCacheManager(LettuceConnectionFactory lettuceConnectionFactory) {
        return defaultCacheManager(lettuceConnectionFactory, Duration.ofSeconds(300));
    }

    @Bean(name = CacheManagerNames.REDIS_2D_CACHE_MANAGER)
    default RedisCacheManager redis2dCacheManager(LettuceConnectionFactory lettuceConnectionFactory) {
        return defaultCacheManager(lettuceConnectionFactory, Duration.ofDays(2));
    }

    @Bean(name = CacheManagerNames.REDIS_7D_CACHE_MANAGER)
    default RedisCacheManager redis7dCacheManager(LettuceConnectionFactory lettuceConnectionFactory) {
        return defaultCacheManager(lettuceConnectionFactory, Duration.ofDays(7));
    }

    @Bean(name = CacheManagerNames.REDIS_1M_CACHE_MANAGER)
    default RedisCacheManager redis1mCacheManager(LettuceConnectionFactory lettuceConnectionFactory) {
        return defaultCacheManager(lettuceConnectionFactory, Duration.ofDays(30));
    }

    default RedisCacheManager defaultCacheManager(LettuceConnectionFactory lettuceConnectionFactory, Duration duration) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(duration)
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));
        redisCacheConfiguration.usePrefix();

        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(lettuceConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .build();
    }

    @Bean
    default RedisTemplate<String, String> redisTemplate(LettuceConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate(factory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.activateDefaultTyping(om.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL);
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

}
