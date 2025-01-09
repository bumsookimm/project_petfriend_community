package com.tech.petfriends.community.service;

import java.io.File;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;

import com.tech.petfriends.community.mapper.IDao;
import com.tech.petfriends.community.service.interfaces.CServiceMInterface;
import com.tech.petfriends.login.dto.MemberLoginDto;

@Service
public class CUploadFeedImgService implements CServiceMInterface {

	private IDao iDao;

	public CUploadFeedImgService(IDao iDao) {
		this.iDao = iDao;
	}

	@Override
	public void execute(Model model) {
		// 파일 처리
		String mem_code = (String) model.getAttribute("mem_code");

		// HttpServletRequest에서 MultipartFile 추출
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) model
				.getAttribute("multipartRequest");
		MultipartFile feedImage = multipartRequest.getFile("feedImage");
		if (feedImage != null && !feedImage.isEmpty()) {
			// 파일 이름 생성 (고유 파일 이름 사용 가능)
			String originalFileName = feedImage.getOriginalFilename();
			String newFileName = System.currentTimeMillis() + "_" + originalFileName;

			String workPath = System.getProperty("user.dir");
			String root = workPath + "\\src\\main\\resources\\static\\images\\communityorign_img";

			File file = new File(root, newFileName);
			try {
				feedImage.transferTo(file); // 파일 저장
				// 이미지 파일 경로 저장
				iDao.myFeedImgWrite(mem_code, newFileName); // 파일 이름을 DB에 저장
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		model.addAttribute("msg", "이미지 수정이 완료됐습니다.");

	}
}