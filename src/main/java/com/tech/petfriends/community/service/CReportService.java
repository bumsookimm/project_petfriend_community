package com.tech.petfriends.community.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.tech.petfriends.community.mapper.IDao;
@Service
public class CReportService implements CServiceInterface {
	
	private IDao iDao;
	
	public CReportService(IDao iDao) {
		this.iDao = iDao;
	}
	
	@Override
	public void execute(Model model) {
			
		Integer board_no = (Integer) model.getAttribute("board_no");		
		String reporter_id = (String) model.getAttribute("reporter_id");
		String mem_code = (String) model.getAttribute("mem_code");
		String reason = (String) model.getAttribute("reason");
		Integer comment_no = (Integer) model.getAttribute("comment_no");
		String report_type = (String) model.getAttribute("report_type");
		
		System.out.println("Received board_no: " + board_no);
		
		iDao.report(board_no, reporter_id, reason, comment_no, report_type, mem_code);
	
		
	}

	
	
	
}
