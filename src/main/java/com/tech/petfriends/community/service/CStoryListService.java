package com.tech.petfriends.community.service;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.tech.petfriends.community.dto.CDto;
import com.tech.petfriends.community.mapper.IDao;
import com.tech.petfriends.login.dto.MemberLoginDto;
	
	@Service
	public class CStoryListService  {
	
	    private IDao iDao;
	   
	   
	    public CStoryListService(IDao iDao) {
	        this.iDao = iDao;
	      
	    }
	    @Cacheable(value = "userStories", key = "#loginUser.mem_nick")
	    public ArrayList<CDto> storyListExecute(MemberLoginDto loginUser) {
	        if (loginUser != null) {
	            String mem_nick = loginUser.getMem_nick();
	            System.out.println("[INFO] 캐시에 데이터가 없으므로 데이터베이스에서 가져옵니다: " + mem_nick);
	            
	            // 데이터베이스에서 사용자의 스토리 목록을 가져옴
	            return iDao.storyList(mem_nick);
	        }
	        return new ArrayList<>();
	    }
	}