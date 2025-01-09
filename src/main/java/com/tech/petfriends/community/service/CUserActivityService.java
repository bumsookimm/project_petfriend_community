package com.tech.petfriends.community.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.tech.petfriends.community.dto.CDto;
import com.tech.petfriends.community.mapper.IDao;

@Service
public class CUserActivityService  {

	private IDao iDao;

	public CUserActivityService(IDao iDao) {
		this.iDao = iDao;
	}

	public ArrayList<CDto> getUserActivityList(String userId) {
        // userId에 해당하는 활동 목록을 데이터베이스에서 가져옴
        return iDao.userActivityList(userId);
    }

}
