package com.tech.petfriends.community.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.tech.petfriends.community.dto.CCategoryDto;
import com.tech.petfriends.community.dto.CDto;
import com.tech.petfriends.community.mapper.IDao;
import com.tech.petfriends.login.dto.MemberLoginDto;

@Service
public class CWriteViewService implements CServiceInterface {
	private IDao iDao;
	
	public CWriteViewService(IDao idao) {
		this.iDao = idao;
	}
		
	@Override
	public void execute(Model model) {
		Map<String, Object> m = model.asMap();
		HttpSession session = (HttpSession) m.get("session");
		
		MemberLoginDto loginUser = (MemberLoginDto) session.getAttribute("loginUser");
		String mem_code = loginUser.getMem_code();
		CDto getpetimg = (CDto) iDao.getPetIMG(mem_code);
	    model.addAttribute("getpetimg",getpetimg);
		
		ArrayList<CCategoryDto> category = iDao.getCategoryList();
		model.addAttribute("category",category);

			
	}


	
}
