package com.tech.petfriends.community.service;

import java.sql.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.tech.petfriends.community.mapper.IDao;
@Service
public class CCommentService implements CServiceInterface {

	private IDao iDao;
	
	public CCommentService(IDao iDao) {
		this.iDao = iDao;
	}

	@Override
	public void execute(Model model) {
		Map<String, Object> m = model.asMap();
		HttpServletRequest request = (HttpServletRequest) m.get("request");
		
		String board_no = request.getParameter("board_no");
		String comment_no = request.getParameter("comment_no");
		String mem_code = request.getParameter("mem_code");
		String mem_nick = request.getParameter("mem_nick");
		String comment_content = request.getParameter("comment_content");
		String parent_comment_no = request.getParameter("parent_comment_no");
		String comment_level = request.getParameter("comment_level");
		String comment_order_no = request.getParameter("comment_order_no");
		String related_user_id = request.getParameter("parent_user_nick");
	
		
		iDao.comment(board_no,comment_no,mem_nick,comment_content,parent_comment_no,comment_level,comment_order_no, mem_code);
		
		iDao.commentActivity(mem_nick, related_user_id, board_no, comment_content);
		
	}
	
	
}
