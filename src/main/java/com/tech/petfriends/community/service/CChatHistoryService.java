package com.tech.petfriends.community.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tech.petfriends.community.dto.CChatDto;
import com.tech.petfriends.community.dto.CDto;
import com.tech.petfriends.community.mapper.IDao;

@Service
public class CChatHistoryService  {

	private IDao iDao;

	public CChatHistoryService(IDao iDao) {
		this.iDao = iDao;
	}

	public List<CChatDto> getChatHistoryList(String roomId) {
        // userId에 해당하는 활동 목록을 데이터베이스에서 가져옴
		List<CChatDto> getChatHistory = iDao.getChatHistory(roomId);
	
		return getChatHistory;
    }

}
