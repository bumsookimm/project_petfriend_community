package com.tech.petfriends.community.service;

import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.tech.petfriends.community.dto.CCommunityFriendDto;
import com.tech.petfriends.community.mapper.IDao;
import com.tech.petfriends.community.service.interfaces.CServiceMInterface;
@Service
public class CNeighborListService implements CServiceMInterface {

	private IDao iDao;

	public CNeighborListService(IDao iDao) {
		this.iDao = iDao;
	}

	@Override
	public void execute(Model model) {
		Map<String, Object> m = model.asMap();
		HttpServletRequest request = (HttpServletRequest) m.get("request");
		
		
		// friend_mem_nick = 현재 게시글 nick
		String friend_mem_nick = request.getParameter("mem_nick");
		System.out.println("friend_mem_nick: " + friend_mem_nick);
		
		ArrayList<CCommunityFriendDto> neighborList = iDao.getNeighborList(friend_mem_nick);
		model.addAttribute("neighborList", neighborList);

	
			
	}
}