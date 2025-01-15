package com.tech.petfriends.community.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class CommunityComment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int commentNo;

	@ManyToOne
	@JoinColumn(name = "board_no", nullable = false)
	private CommunityBoard board;

	private String userId;
	private String commentContent;
	private String createdAt;

}
