package com.tech.petfriends.community.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.tech.petfriends.community.dto.CChatDto;
import com.tech.petfriends.community.mapper.IDao;
import com.tech.petfriends.login.dto.MemberLoginDto;

@Service
public class CChatRoomService {

	private static final Logger logger = LoggerFactory.getLogger(CChatRoomService.class);
	private IDao iDao;

	public CChatRoomService(IDao iDao) {
		this.iDao = iDao;
	}

	public List<CChatDto> getChatRooms(HttpSession session) {

        MemberLoginDto loginUser = (MemberLoginDto) session.getAttribute("loginUser");
  
        if (loginUser == null) {
            logger.warn ("로그인 정보가 없습니다."); 
            return null;  //
        }

        String sender = loginUser.getMem_nick();

        
        return iDao.getChatRooms(sender);
    }
}
