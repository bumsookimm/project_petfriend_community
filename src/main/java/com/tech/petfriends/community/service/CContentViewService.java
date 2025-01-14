package com.tech.petfriends.community.service;

import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.tech.petfriends.community.dto.CCommentDto;
import com.tech.petfriends.community.dto.CDto;
import com.tech.petfriends.community.mapper.IDao;
import com.tech.petfriends.community.service.interfaces.CServiceMInterface;
import com.tech.petfriends.login.dto.MemberLoginDto;

@Service
public class CContentViewService implements CServiceMInterface{

	private IDao iDao;
	
	public CContentViewService(IDao iDao) {
		this.iDao = iDao;
	}
	
	@Override
	public void execute(Model model) {
		Map<String, Object> m = model.asMap();	
		HttpServletRequest request = (HttpServletRequest) m.get("request");
		HttpSession session = (HttpSession) m.get("session");	
		
		String board_no = request.getParameter("board_no");
		
		
		iDao.incrementViews(board_no);
		
		
        MemberLoginDto loginUser = (MemberLoginDto) session.getAttribute("loginUser");

        int isliked = 0; // 기본값으로 0 (좋아요하지 않은 상태)
        if (loginUser != null) {
            String mem_code = loginUser.getMem_code();
            isliked = iDao.isLiked(board_no, mem_code); // 로그인한 경우 실제 좋아요 여부 조회
        }
        model.addAttribute("isliked", isliked);
		
        CDto dto = iDao.contentView(board_no);
		
		model.addAttribute("contentView", dto);

	      // 댓글 리스트 가져오기
		ArrayList<CCommentDto> commentList = iDao.commentList(board_no);
        model.addAttribute("commentList", commentList); // 댓글 리스트를 모델에 추가
		
        // 대 댓글 리스트 가져오기
    	ArrayList<CCommentDto> commentReplyList = iDao.commentReplyList(board_no);
        model.addAttribute("commentReplyList", commentReplyList);
	

   
	}

}
