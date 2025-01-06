package com.tech.petfriends.community.service;

import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.ui.Model;

import com.tech.petfriends.community.dto.CDto;
import com.tech.petfriends.community.mapper.IDao;
import com.tech.petfriends.login.dto.MemberLoginDto;



	public class CStoryListService implements CServiceInterface{
		
		private IDao iDao;
	
		public CStoryListService(IDao iDao) {
			this.iDao = iDao;
		}
	
		
		@Override
		@Cacheable(value = "storyCache", key = "#mem_nick") //캐시 설정
		public void execute(Model model) {
			Map<String, Object> m = model.asMap();
			HttpSession session = (HttpSession) m.get("session");	
			MemberLoginDto loginUser = (MemberLoginDto) session.getAttribute("loginUser");
	 
	        if(loginUser != null) {
	        	 String mem_nick = loginUser.getMem_nick();
	    	
	    		
	             ArrayList<CDto> storyList = iDao.storyList(mem_nick);
	             model.addAttribute("storyList",storyList);
	          
	        }
	        
	  	
		} 
		
	}
	

