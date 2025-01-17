package com.tech.petfriends.community.service;

import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.tech.petfriends.community.dto.CDto;
import com.tech.petfriends.community.mapper.IDao;
import com.tech.petfriends.community.service.interfaces.CServiceMInterface;
import com.tech.petfriends.login.dto.MemberLoginDto;

@Service
public class CMyFeedService implements CServiceMInterface {

	private IDao iDao;

	public CMyFeedService(IDao iDao) {
		this.iDao = iDao;
	}

	@Override
	public void execute(Model model) {
		// Model에서 request와 session을 가져옵니다.
		Map<String, Object> m = model.asMap();
		HttpServletRequest request = (HttpServletRequest) m.get("request");
		HttpSession session = (HttpSession) m.get("session");

		MemberLoginDto loginUser = (MemberLoginDto) session.getAttribute("loginUser");
		if (loginUser != null) {
			String mem_code = loginUser.getMem_code();

			CDto logingetpetimg = (CDto) iDao.getPetIMG(mem_code);
			model.addAttribute("logingetpetimg", logingetpetimg);
		}

		
		String mem_code = (String) model.getAttribute("mem_code");
		model.addAttribute("mem_code", mem_code);

		// mem_code를 기반으로 myFeedList 조회
		ArrayList<CDto> myFeedList = iDao.myFeedList(mem_code); // mem_code를 인자로 넘기기
		model.addAttribute("myFeedList", myFeedList);
		System.out.println(mem_code);

		CDto myFeedName = iDao.myFeedName(mem_code);
		model.addAttribute("myFeedName", myFeedName);

		CDto getpetimg = (CDto) iDao.getPetIMG(mem_code);
		model.addAttribute("getpetimg", getpetimg);
		System.out.println("getpetimg:"+ getpetimg);
		
		String friend_mem_nick = myFeedName.getMem_nick();
		System.out.println("friend_mem_nick:" + friend_mem_nick);
			    												
		
		  if (loginUser != null) {
			String mem_nick = loginUser.getMem_nick();
			Integer count = iDao.isFriend(mem_nick, friend_mem_nick); // Integer로 받아옴
		
			System.out.println("count:" + count);

			model.addAttribute("isFriendBool", count);
		}

		
		CDto getMyfeedVisit = (CDto) iDao.getMyfeedVisit(mem_code);
		model.addAttribute("getMyfeedVisit", getMyfeedVisit);
		
		
		
		iDao.totalVisits(mem_code);
		iDao.dailyVisits(mem_code);
	}

}
