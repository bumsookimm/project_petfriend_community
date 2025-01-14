package com.tech.petfriends.community.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.tech.petfriends.community.dto.CDto;
import com.tech.petfriends.community.mapper.IDao;

@Service
public class CMyActivityService  {

	private IDao iDao;

	public CMyActivityService(IDao iDao) {
		this.iDao = iDao;
	}

	public ArrayList<CDto> getMyActivityList(String userId) {
        // userId에 해당하는 활동 목록을 데이터베이스에서 가져옴
        return iDao.myActivityList(userId);
    }

}
