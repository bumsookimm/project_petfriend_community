package com.tech.petfriends.community.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.tech.petfriends.community.mapper.IDao;
import com.tech.petfriends.login.dto.MemberLoginDto;
@Service
public class CFriendService implements CServiceInterface {

	private IDao iDao;

	public CFriendService(IDao iDao) {
		this.iDao = iDao;
	}

	@Override
	public void execute(Model model) {
		Map<String, Object> m = model.asMap();
		HttpSession session = (HttpSession) m.get("session");
		HttpServletRequest request = (HttpServletRequest) m.get("request");
		
		MemberLoginDto loginUser = (MemberLoginDto) session.getAttribute("loginUser");
		String mem_nick = loginUser.getMem_nick();
		System.out.println("mem_nick: " + mem_nick);
		
		String friend_mem_nick = request.getParameter("mem_nick");
		System.out.println("friend_mem_nick: " + friend_mem_nick);
		

		   // 친구 관계 여부 확인 (1이면 친구, 0이면 친구 아님)
	    Integer isFriendBool = iDao.isFriend(mem_nick, friend_mem_nick); 
	    
	    // isFriendBool을 모델에 추가
	    model.addAttribute("isFriendBool", isFriendBool);

	    // 친구 추가 또는 삭제 처리
	    if (isFriendBool == 0 || isFriendBool == null) {
	        iDao.addFriend(mem_nick, friend_mem_nick);
	        iDao.friendActivity(mem_nick, friend_mem_nick);
	    } else if (isFriendBool == 1) {
	        iDao.deleteFriend(mem_nick, friend_mem_nick);
	    }
	
		
	}
}