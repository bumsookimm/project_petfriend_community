package com.tech.petfriends.community.service;

import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.tech.petfriends.community.dto.CDto;
import com.tech.petfriends.community.mapper.IDao;
import com.tech.petfriends.community.service.interfaces.CServiceMInterface;
import com.tech.petfriends.login.dto.MemberLoginDto;
	
	@Service
	public class CStoryListService implements CServiceMInterface {
	
	    private IDao iDao;
	   
	   
	    public CStoryListService(IDao iDao) {
	        this.iDao = iDao;
	      
	    }
	    @Override
	    public void execute(Model model) {
	        Map<String, Object> m = model.asMap();
	        HttpSession session = (HttpSession) m.get("session");
	        MemberLoginDto loginUser = (MemberLoginDto) session.getAttribute("loginUser");
	
	        if (loginUser != null) {
	            String mem_nick = loginUser.getMem_nick();
	       
	            ArrayList<CDto> storyList = iDao.storyList(mem_nick);
	
	            model.addAttribute("storyList", storyList);
	        }
	    }
	}