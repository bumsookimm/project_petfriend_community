package com.tech.petfriends.community.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;

import com.tech.petfriends.community.dto.CDto;
import com.tech.petfriends.community.mapper.IDao;
import com.tech.petfriends.login.dto.MemberLoginDto;



public class CPostListService implements CServiceInterface{
	
	private IDao iDao;

	public CPostListService(IDao iDao) {
		this.iDao = iDao;
	}

	@Override
	public void execute(Model model) {
		Map<String, Object> m = model.asMap();
		HttpServletRequest request = (HttpServletRequest) m.get("request");
		HttpSession session = (HttpSession) m.get("session");
	
		
		MemberLoginDto loginUser = (MemberLoginDto) session.getAttribute("loginUser");
		if(loginUser != null) {
		String mem_code = loginUser.getMem_code();
		System.out.println("mem_code" + mem_code);
		
        CDto getpetimg = (CDto) iDao.getPetIMG(mem_code);
        model.addAttribute("getpetimg",getpetimg);
		}

	    int page = Integer.parseInt(request.getParameter("page") != null ? request.getParameter("page") : "1");
	    int limit = 5;  // 페이지당 게시글 수
	    int offset = (page - 1) * limit;
	    System.out.println("page: "+ page);
	    
		// 검색어 처리
        String query = request.getParameter("query");
		if (query != null && !query.isEmpty()) {
	        // 검색어가 있을 경우, 제목, 콘텐츠, 작성자에서 검색
			ArrayList<CDto> postList = iDao.searchPosts(query);
	        model.addAttribute("postList", postList); // 검색된 게시글 리스트
	  
	
		
		} else {
	        // 검색어가 없으면 모든 게시글 목록 조회
	    	
			ArrayList<CDto> allPosts = iDao.getPostList();
	        
			// 서버에서 페이징 처리
		    int totalPosts = allPosts.size();
		    int totalPages = (int) Math.ceil((double) totalPosts / limit);
		    List<CDto> postList = allPosts.subList(offset, Math.min(offset + limit, totalPosts));

		    // 모델에 필요한 데이터를 추가
		   
		    model.addAttribute("postList", postList); // 모델에 게시글 리스트 추가
		    model.addAttribute("totalPages", totalPages); // 전체 페이지 수
		    model.addAttribute("currentPage", page); // 현재 페이지
		    model.addAttribute("totalPosts", totalPosts); // 전체 게시글 수
		
		}
	  

        
		    ArrayList<CDto> getHotTopicList = iDao.getHotTopicList();
		    
		    for (CDto hottopic : getHotTopicList) {
		        if (hottopic.getChrepfile() == null || hottopic.getChrepfile().isEmpty()) {
		            hottopic.setChrepfile("noPetImg.jpg");
		        }
		    }
	
		    model.addAttribute("getHotTopicList", getHotTopicList);
		

  
	
	} 
	
}
	

