package com.tech.petfriends.community.configuration;

import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.CacheManager; // Spring CacheManager 임포트

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean(name = "cacheManager")
    public CacheManager cacheManager() {
        // EhCache 3.x용 CacheManager 설정
        org.ehcache.CacheManager ehCacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("storyCache",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
                                ResourcePoolsBuilder.heap(1000))
                                .withExpiry(org.ehcache.expiry.Expirations.timeToLiveExpiration(
                                        Duration.of(10, java.util.concurrent.TimeUnit.MINUTES)))) // TimeUnit을 사용
                .build();

        ehCacheManager.init(); // CacheManager 초기화

        // Spring의 CacheManager로 래핑하여 반환
        return new EhCacheCacheManager(ehCacheManager); // Spring CacheManager 반환
    }
}