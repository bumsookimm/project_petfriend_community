package com.tech.petfriends.community.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.tech.petfriends.community.dto.CChatDto;
import com.tech.petfriends.community.mapper.IDao;
import com.tech.petfriends.login.dto.MemberLoginDto;

@Service
public class CChatRoomService {

	private IDao iDao;

	public CChatRoomService(IDao iDao) {
		this.iDao = iDao;
	}

	public List<CChatDto> getChatRooms(HttpSession session) {

        MemberLoginDto loginUser = (MemberLoginDto) session.getAttribute("loginUser");
        if (loginUser == null) {
            throw new RuntimeException("로그인 정보가 없습니다.");
        }

        String sender = loginUser.getMem_nick();

        
        return iDao.getChatRooms(sender);
    }
}
