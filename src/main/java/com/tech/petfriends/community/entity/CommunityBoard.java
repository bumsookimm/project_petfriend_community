package com.tech.petfriends.community.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Entity
public class CommunityBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int boardNo;

    private String title;
    private String content;
    private String userId;
    private int likes;
    private int commentCount;

    @OneToMany(mappedBy = "board")
    private List<CommunityLikes> likesList;

    @OneToMany(mappedBy = "board")
    private List<CommunityComment> commentList;

    @OneToMany(mappedBy = "board")
    private List<CommunityReport> reportList;

    @OneToMany(mappedBy = "board")
    private List<ReCboardImage> imageList;

   
}
