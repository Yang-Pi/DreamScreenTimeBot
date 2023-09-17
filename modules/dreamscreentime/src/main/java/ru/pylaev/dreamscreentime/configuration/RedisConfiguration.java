package ru.pylaev.dreamscreentime.configuration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

import java.time.Duration;
import java.util.Objects;

@AutoConfiguration
@PropertySource("classpath:redis.properties")
@EnableCaching
public class RedisConfiguration {

    @Bean
    RedisCacheConfiguration redisCacheConfiguration(CacheProperties cacheProperties) {

        final var redisProperties = cacheProperties.getRedis();
        var config = RedisCacheConfiguration.defaultCacheConfig();

        if (Objects.nonNull(redisProperties.getTimeToLive())) {
            config = config.entryTtl(redisProperties.getTimeToLive());
        }
        if (Objects.nonNull(redisProperties.getKeyPrefix())) {
            config = config.prefixCacheNameWith(redisProperties.getKeyPrefix());
        }
        if (redisProperties.isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }
        if (!redisProperties.isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }
        return config;

    }

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer(RedisCacheConfiguration redisCacheConfiguration) {
        return builder ->
                builder.withCacheConfiguration("Users", redisCacheConfiguration.entryTtl(Duration.ofDays(30)));
    }
}
