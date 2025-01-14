package com.tech.petfriends.community.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.tech.petfriends.community.dto.CDto;
import com.tech.petfriends.community.mapper.IDao;
import com.tech.petfriends.community.service.interfaces.CServiceMInterface;

@Service
public class CDeleteService implements CServiceMInterface {

	private IDao iDao;

	public CDeleteService(IDao iDao) {
		this.iDao = iDao;
	}

	@Override
	public void execute(Model model) {
		Map<String, Object> m = model.asMap();
		HttpServletRequest request = (HttpServletRequest) m.get("request");

		int board_no = Integer.parseInt(request.getParameter("board_no"));

		iDao.deleteReports(board_no);

		iDao.deleteLikes(board_no);

		iDao.deleteComments(board_no);

		deleteImages(board_no);

		iDao.delete(board_no);

	}

	private void deleteImages(int board_no) {
	    // CDto 객체 리스트 반환
	    List<CDto> imageRecords = iDao.selectImg(board_no); 
	    String root = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\images\\community_img";

	    for (CDto dto : imageRecords) {
	        // CDto에서 이미지 파일 경로를 추출
	        String imageFile = dto.getChrepfile(); // CDto 클래스의 corgfile 필드 사용
	        File file = new File(root + "\\" + imageFile);
	        if (file.exists() && file.delete()) {
	            System.out.println("파일 삭제 성공: " + file.getAbsolutePath());
	        } else {
	            System.out.println("파일 삭제 실패: " + file.getAbsolutePath());
	        }
	    }
	    // 데이터베이스에서 이미지 레코드 삭제
	    iDao.deleteImages(board_no);
	}
}