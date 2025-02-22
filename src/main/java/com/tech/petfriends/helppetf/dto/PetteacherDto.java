package com.tech.petfriends.helppetf.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PetteacherDto {
	private int hpt_seq;
	private String hpt_yt_videoid;
	private String hpt_exp;
	private String hpt_title;
	private String hpt_channal;
	private String hpt_content;
	private String hpt_pettype;
	private String hpt_category;
	private int hpt_hit;
	private Date hpt_rgedate;
}

