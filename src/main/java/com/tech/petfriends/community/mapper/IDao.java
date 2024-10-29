package com.tech.petfriends.community.mapper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.tech.petfriends.community.dto.CCategoryDto;
import com.tech.petfriends.community.dto.CCommentDto;
import com.tech.petfriends.community.dto.CDto;


@Mapper
public interface IDao {
	
	public ArrayList<CDto> getPostList();
	
	public void write (String user_id,String board_title,String board_content, int b_cate_no);
	
	public void imgWrite(int board_no, String originalFile, String changeFile,String repImgOriginal, String repImgChange);

	public int selBid();

	public CDto contentView(String board_no); 

	public ArrayList<CCategoryDto> getCategoryList();

	public ArrayList<CDto> getPostsByCategory(int b_cate_no);
	
	public void modify(int board_no, String board_title, String board_content, int b_cate_no);

	public void modifyImg(int board_no, String originalFile, String changeFile,String repImgOriginal, String repImgChange);

	public void delete(int board_no);

	public void comment(String board_no, String comment_no, String user_id, String comment_content,
			String parent_comment_no, String comment_level, String comment_order_no);

	public void commentReply(String board_no,String user_id, String comment_content,
			String parent_comment_no, String comment_level, String comment_order_no);
	
	public void commentShape(String parent_comment_no, String comment_level);
	
	public ArrayList<CCommentDto> commentList(String board_no);

	public ArrayList<CCommentDto> commentReplyList(String board_no);

	public int stepInit(String comment_no, String parent_comment_no, String comment_level);

	public int replyDelete(String comment_no, String parent_comment_no, String comment_level, String comment_order_no);
}

