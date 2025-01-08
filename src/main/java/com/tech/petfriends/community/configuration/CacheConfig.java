package com.tech.petfriends.community.configuration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

    // ConcurrentMapCacheManager를 사용하여 캐시 관리
	@Bean
	public CacheManager cacheManager() {
	    return new ConcurrentMapCacheManager("storyList"); // 캐시 이름
	}
}

