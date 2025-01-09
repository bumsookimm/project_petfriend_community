package com.tech.petfriends.community.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.tech.petfriends.community.mapper.IDao;
import com.tech.petfriends.community.service.interfaces.CServiceMInterface;
@Service
public class CReplyDeleteService implements CServiceMInterface {

    private IDao iDao;

    public CReplyDeleteService(IDao iDao) {
        this.iDao = iDao;
    }

    @Override
    public void execute(Model model) {
    	Map<String, Object> m = model.asMap();
    	HttpServletRequest request = (HttpServletRequest) m.get("request");
    	
		String mem_nick = request.getParameter("mem_nick");	
		String mem_code = request.getParameter("mem_code");	
		String board_no = request.getParameter("board_no");
		String comment_no = request.getParameter("comment_no");
		String parent_comment_no = request.getParameter("parent_comment_no");
		String comment_level = request.getParameter("comment_level");
		String comment_order_no = request.getParameter("comment_order_no");
		
		System.out.println("mem_nick:"+ mem_nick);
		
		//만약 로그인 닉네임이 구트아카데미 라면 관리자 삭제로 진행 그게 아니라면 댓글삭제
		if(mem_nick.equals("구트아카데미")) {
			iDao.managerReplyUpdate(mem_nick, mem_code, comment_no);
			model.addAttribute("msg", "관리자 댓글 삭제 성공");
			model.addAttribute("url", "/community/contentView?board_no=" + board_no);
		
			
		} else {
		// 댓글 삭제 시도
		int rn = iDao.replyDelete(comment_no, parent_comment_no, comment_level, comment_order_no);
		if (rn == 0) {
			// 삭제 실패 (상위 댓글이 존재)
			System.out.println("댓글 삭제 실패");
			model.addAttribute("msg", "이 댓글은 상위 댓글을 가지고 있어 삭제할 수 없습니다.");
			model.addAttribute("url", "/community/contentView?board_no=" + board_no);
		

		} else {
			// 삭제 성공
			System.out.println("댓글 삭제 성공");
			iDao.stepInit(comment_no, parent_comment_no, comment_level);
			model.addAttribute("msg", "댓글이 삭제됐습니다.");
			model.addAttribute("url", "/community/contentView?board_no=" + board_no);
		
		}
		}

    }
}
