package com.tech.petfriends.community.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.tech.petfriends.community.mapper.IDao;
@Service
public class CWriteService implements CServiceInterface {

	private IDao iDao;

	public CWriteService(IDao iDao) {
		this.iDao = iDao;
	}

	@Override
	public void execute(Model model) {
		Map<String, Object> m = model.asMap();
		MultipartHttpServletRequest mtfRequest = (MultipartHttpServletRequest) m.get("request");

		String mem_nick = mtfRequest.getParameter("mem_nick");
		String mem_code = mtfRequest.getParameter("mem_code");
		String board_title = mtfRequest.getParameter("board_title");
		String board_content = mtfRequest.getParameter("board_content");
		int b_cate_no = Integer.parseInt(mtfRequest.getParameter("b_cate_no"));
		String board_content_input = mtfRequest.getParameter("board_content_input");
		// 게시글 작성
		iDao.write(mem_nick, mem_code, board_title, board_content, b_cate_no, board_content_input);

		System.out.println("board_content_input: " +board_content_input);
		
		// 방금 작성한 게시글의 id 가져오기
		int board_no = iDao.selBid();
		// 피드로 전달
		iDao.addFeed(mem_code, board_no, mem_nick);
	
		String workPath = System.getProperty("user.dir");
		String root = workPath + "\\src\\main\\resources\\static\\images\\community_img";
		System.out.println(System.getProperty("user.dir"));

		// 파일 업로드 처리 (일반 이미지)
		String repImgOriginal = null; // 대표 이미지 원본 파일명
		String repImgChange = null; // 대표 이미지 변경 파일명

		// 대표 이미지 처리
		MultipartFile repFile = mtfRequest.getFile("repfile");
		if (repFile != null && !repFile.isEmpty()) {
		    String originalRepFile = repFile.getOriginalFilename();
		    long longtime = System.currentTimeMillis();
		    repImgChange = longtime + "_" + originalRepFile;
		    String pathRepFile = root + "\\" + repImgChange;

		    try {
		        repFile.transferTo(new File(pathRepFile));
		        repImgOriginal = originalRepFile; // 대표 이미지 원본 파일명 저장
		    } catch (Exception e) {
		        e.printStackTrace();
		        throw new RuntimeException("Representative image upload failed", e);
		    }
		}

		// 일반 이미지 업로드 처리
		
		String originalFile = null; //일반이미지
		String changeFile = null; 
		List<MultipartFile> fileList = mtfRequest.getFiles("file");
		boolean isFileUploaded = fileList.stream().anyMatch(file -> !file.isEmpty());

		if (isFileUploaded) {
		    for (MultipartFile mf : fileList) {
		        if (!mf.isEmpty()) { // 파일이 실제로 존재할 때만 처리
		            originalFile = mf.getOriginalFilename(); // 새로운 파일명
		            long longtime = System.currentTimeMillis();
		            changeFile = longtime + "_" + originalFile;
		            String pathFile = root + "\\" + changeFile;

		            try {
		                mf.transferTo(new File(pathFile));
		                System.out.println("다중업로드성공");
		                iDao.imgWrite(board_no, originalFile, changeFile, repImgOriginal, repImgChange);
		            } catch (Exception e) {
		                e.printStackTrace();
		                throw new RuntimeException("Image upload failed", e);
		            }
		        }
		    }
		} else {
		    // 새로운 파일이 없으면 기존 파일명(corgfile, cchgfile)을 그대로 사용하여 데이터베이스 업데이트
		    iDao.imgWrite(board_no, originalFile, changeFile, repImgOriginal, repImgChange);
		}
	}
}