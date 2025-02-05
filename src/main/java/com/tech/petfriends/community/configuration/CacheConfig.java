package com.tech.petfriends.community.configuration;

import java.util.Arrays;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

	@Configuration
	@EnableCaching
	public class CacheConfig {

	    @Bean
	    public CacheManager cacheManager() {
	        SimpleCacheManager cacheManager = new SimpleCacheManager();
	        // 캐시 이름 'userStories'와 'postlist'를 설정
	        cacheManager.setCaches(Arrays.asList(new ConcurrentMapCache("userStories"), new ConcurrentMapCache("postlist")));
	        return cacheManager;
	    }
	}

