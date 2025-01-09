package com.tech.petfriends.community.service;

import org.springframework.stereotype.Service;

import com.tech.petfriends.community.dto.CReportDto;
import com.tech.petfriends.community.mapper.IDao;

@Service
public class CReportService {

	private IDao iDao;

	public CReportService(IDao iDao) {
		this.iDao = iDao;
	}

	public void Reportexecute(CReportDto reportDto) {

		Integer board_no = reportDto.getBoard_no();
		String reporter_id = reportDto.getReporter_id();
		String reason = reportDto.getReason();
		Integer comment_no = reportDto.getComment_no();
		String report_type = reportDto.getReport_type();
		String mem_code = reportDto.getMem_code();

		System.out.println("Received board_no: " + board_no);

		iDao.report(board_no, reporter_id, reason, comment_no, report_type, mem_code);

	}

}
