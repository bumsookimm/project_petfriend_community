package com.tech.petfriends.community.mapper;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tech.petfriends.community.entity.CommunityBoard;

	@Repository
	public interface CommunityBoardRepository extends CrudRepository<CommunityBoard, Integer> {
		
		@Modifying
		@Transactional
		@Query("DELETE FROM CommunityBoard ri WHERE ri.boardNo = :boardNo")
		void deleteBoard(int boardNo);
	
	    @Modifying
	    @Transactional
	    @Query("DELETE FROM CommunityLikes cl WHERE cl.board.boardNo = :boardNo")
	    void deleteLikes(int boardNo);
	   
	    @Modifying
	    @Transactional
	    @Query("DELETE FROM CommunityComment cc WHERE cc.board.boardNo = :boardNo")
	    void deleteComments(int boardNo);
	
	    @Modifying
	    @Transactional
	    @Query("DELETE FROM CommunityReport cr WHERE cr.board.boardNo = :boardNo")
	    void deleteReports(int boardNo);
	
	    @Modifying
	    @Transactional
	    @Query("DELETE FROM ReCboardImage ri WHERE ri.board.boardNo = :boardNo")
	    void deleteImages(int boardNo);
	
	}
	
