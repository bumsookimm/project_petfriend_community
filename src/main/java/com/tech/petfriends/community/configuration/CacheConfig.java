package com.tech.petfriends.community.configuration;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
        EhCacheManagerFactoryBean factoryBean = new EhCacheManagerFactoryBean();
        factoryBean.setConfigLocation(new ClassPathResource("ehcache.xml")); // Ehcache 설정 파일 경로
        factoryBean.setShared(true); // 공유 캐시 설정
        return factoryBean;
    }

    @Bean
    public EhCacheCacheManager cacheManager(EhCacheManagerFactoryBean factoryBean) {
    	 
    	
    	return new EhCacheCacheManager(factoryBean.getObject());
    }
}

