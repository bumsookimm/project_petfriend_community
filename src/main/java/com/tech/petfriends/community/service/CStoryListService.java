package com.tech.petfriends.community.service;

import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.ui.Model;

import com.tech.petfriends.community.dto.CDto;
import com.tech.petfriends.community.mapper.IDao;
import com.tech.petfriends.login.dto.MemberLoginDto;

public class CStoryListService implements CServiceInterface {

    private IDao iDao;
    private CacheManager cacheManager;
    private static final Logger logger = LoggerFactory.getLogger(CStoryListService.class);

    public CStoryListService(IDao iDao, CacheManager cacheManager) {
        this.iDao = iDao;
        this.cacheManager = cacheManager;
    }

    @Override
    @Cacheable(value = "storyCache", key = "#mem_nick")
    public void execute(Model model) {
        Map<String, Object> m = model.asMap();
        HttpSession session = (HttpSession) m.get("session");
        MemberLoginDto loginUser = (MemberLoginDto) session.getAttribute("loginUser");

        if (loginUser != null) {
            String mem_nick = loginUser.getMem_nick();
            
            // 캐시 상태 확인
            Cache cache = cacheManager.getCache("storyCache");
            if (cache != null && cache.get(mem_nick) != null) {
                logger.info("Cache hit for user: " + mem_nick);
            } else {
                logger.info("Cache miss for user: " + mem_nick);
                // 캐시가 없을 경우 DB에서 데이터를 가져와서 캐시를 저장
                ArrayList<CDto> storyList = iDao.storyList(mem_nick);
                cache.put(mem_nick, storyList);  // 실제 데이터인 storyList를 캐시에 저장
                logger.info("Stored storyList in cache for user: " + mem_nick);
            }

            // 데이터 가져오기
            ArrayList<CDto> storyList = iDao.storyList(mem_nick);
            model.addAttribute("storyList", storyList);
            logger.info("Fetched storyList: " + storyList.size() + " items");
        } else {
            logger.warn("No login user found in session");
        }
    }
}