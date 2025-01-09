package com.tech.petfriends.community.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.tech.petfriends.community.dto.CChatDto;
import com.tech.petfriends.community.dto.CDto;
import com.tech.petfriends.community.mapper.IDao;


@Service
public class CPostsByCatrgoryService {
   
	private IDao iDao;
	
	public CPostsByCatrgoryService(IDao iDao) {
		this.iDao = iDao;
	}
	
    public void GetCatergory(int bCateNo, Model model) {

		List<CDto> postList = iDao.getPostsByCategory(bCateNo);
		model.addAttribute("postList", postList);	
    	
    	
    }
}
