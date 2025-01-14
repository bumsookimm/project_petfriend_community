package com.tech.petfriends.community.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CDto {
	

    private int board_no;
    private int b_cate_no;
    private String mem_code;
    private String user_id;
    private String board_title;
    private int board_password;
    private String board_content;
    private Date board_created;
    private Date board_modified;
    private int board_views;
    private String board_tag;
    private int board_likes;
    private int board_comment_count;
    private String board_content_input;
	
//    RE_CBOARD_IMAGE 테이블
	private int rebno;
	private String corgfile; // 일반 이미지 원본 파일 이름
	private String cchgfile; // 일반 이미지 변경 파일 이름
	private String orepfile; // 대표 이미지 원본 파일 이름
	private String chrepfile; // 대표 이미지변경 파일 이름

	// member_pet  테이블
	private String pet_img;
	private String pet_main;

	private int feed_no;
	private String mem_nick;

	//친구 테이블
	private String friend_mem_code;

	//활동 테이블

	private int activity_no;
	private String activity_type;
	private String related_user_id;
	private String content;
	private Date created_at;
	private String related_user_mem_code ;

    private int offset;
    private int limit;
	
    //myfeed 테이블
    
    private int  total_visits;
    private int daily_visits;
    private Date create_feed;
    private String myfeed_img;
}





