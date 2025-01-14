package com.tech.petfriends.community.service;

import org.springframework.stereotype.Service;

import com.tech.petfriends.community.dto.CDto;
import com.tech.petfriends.community.mapper.IDao;

	@Service
	public class CDraftService {
		private final IDao iDao;
	
		public CDraftService(IDao iDao) {
			this.iDao = iDao;
		}
	
		public void saveOrUpdateDraft(String mem_code, String board_title, String board_content) {
			CDto draft = iDao.getDraft(mem_code);
			if (draft == null ) {
				iDao.saveDraft(mem_code, board_title, board_content);
			} else {
				iDao.updateDraft(mem_code, board_title, board_content);
			}
		}
	
		public CDto getDraft(String mem_code) {
			
			return iDao.getDraft(mem_code);
		}
	
		public void deleteDraft(String mem_code) {
			System.out.println(mem_code+" 임시저장소 삭제");
			iDao.deleteDraft(mem_code);
		}
	}
