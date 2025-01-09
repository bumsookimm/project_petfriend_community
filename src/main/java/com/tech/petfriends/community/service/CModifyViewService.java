package com.tech.petfriends.community.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.tech.petfriends.community.dto.CCategoryDto;
import com.tech.petfriends.community.dto.CDto;
import com.tech.petfriends.community.mapper.IDao;


	@Service
	public class CModifyViewService {
		
		private IDao iDao;
	
		public CModifyViewService(IDao iDao) {
			this.iDao = iDao;
		}
	
		
		public void loadModifyData(String board_no, Model model) {
	        // 게시글 정보 가져오기
	        CDto content = iDao.contentView(board_no);
	        model.addAttribute("contentView", content);

	        // 카테고리 리스트 가져오기
	        List<CCategoryDto> categoryList = iDao.getCategoryList();
	        model.addAttribute("categoryList", categoryList);
	    }
		
	
}
	

