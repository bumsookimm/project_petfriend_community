package com.tech.petfriends.community.service;

import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.tech.petfriends.community.dto.CDto;
import com.tech.petfriends.community.mapper.IDao;
import com.tech.petfriends.community.service.interfaces.CServiceMInterface;
import com.tech.petfriends.login.dto.MemberLoginDto;

@Service
public class CMyFeedService implements CServiceMInterface {

	private IDao iDao;
	private final RedisTemplate<String, String> redisTemplate;

	public CMyFeedService(IDao iDao, RedisTemplate<String, String> redisTemplate) {
		this.iDao = iDao;
		this.redisTemplate = redisTemplate;
	}

	@Override
	public void execute(Model model) {

		Map<String, Object> m = model.asMap();
		HttpServletRequest request = (HttpServletRequest) m.get("request");
		HttpSession session = (HttpSession) m.get("session");
		String mem_code = (String) model.getAttribute("mem_code");

		String clientIP = getClientIP(request);
		String todayVisitKey = "visited_" + clientIP + mem_code + "_today"; // 오늘 방문 체크
		System.out.println("todayVisitKey:" + todayVisitKey);

		// Redis에서 방문 여부 확인
		if (Boolean.FALSE.equals(redisTemplate.hasKey(todayVisitKey))) {
			iDao.totalVisits(mem_code);
			iDao.dailyVisits(mem_code);

			// 방문 기록 Redis에 저장
			redisTemplate.opsForValue().set(todayVisitKey, "true");
		}

		MemberLoginDto loginUser = (MemberLoginDto) session.getAttribute("loginUser");
		if (loginUser != null) {

			CDto logingetpetimg = (CDto) iDao.getPetIMG(mem_code);
			model.addAttribute("logingetpetimg", logingetpetimg);
		}

		;
		model.addAttribute("mem_code", mem_code);

		// mem_code를 기반으로 myFeedList 조회
		ArrayList<CDto> myFeedList = iDao.myFeedList(mem_code); // mem_code를 인자로 넘기기
		model.addAttribute("myFeedList", myFeedList);
		System.out.println(mem_code);

		CDto myFeedName = iDao.myFeedName(mem_code);
		model.addAttribute("myFeedName", myFeedName);

		CDto getpetimg = (CDto) iDao.getPetIMG(mem_code);
		model.addAttribute("getpetimg", getpetimg);
		System.out.println("getpetimg:" + getpetimg);

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

	}

	// 클라이언트 IP를 추출하는 메서드
	private String getClientIP(HttpServletRequest request) {
	    String clientIP = request.getHeader("X-Forwarded-For");

	    // X-Forwarded-For에서 실제 클라이언트 IP를 추출
	    if (clientIP == null || clientIP.isEmpty() || "unknown".equalsIgnoreCase(clientIP)) {
	        clientIP = request.getHeader("Proxy-Client-IP");
	    }
	    if (clientIP == null || clientIP.isEmpty() || "unknown".equalsIgnoreCase(clientIP)) {
	        clientIP = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if (clientIP == null || clientIP.isEmpty() || "unknown".equalsIgnoreCase(clientIP)) {
	        clientIP = request.getRemoteAddr();
	    }

	    // X-Forwarded-For 헤더에 여러 IP가 있을 경우 첫 번째 IP가 실제 클라이언트 IP입니다.
	    if (clientIP != null && clientIP.contains(",")) {
	        clientIP = clientIP.split(",")[0];
	    }

	    // IPv6 주소인 경우 이를 IPv4 주소로 변환하는 추가 로직을 삽입할 수 있습니다
	    if (clientIP.contains(":") && !clientIP.equals("0:0:0:0:0:0:0:1")) {
	        // 예시: IPv6 주소를 내부 네트워크 주소로 간주하고 다른 처리
	        System.out.println("IPv6 주소가 감지되었습니다: " + clientIP);
	    }

	    return clientIP;
	}
}
