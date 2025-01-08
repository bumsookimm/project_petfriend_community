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
public class CModifyService implements CServiceInterface {

    private IDao iDao;

    public CModifyService(IDao iDao) {
        this.iDao = iDao;
    }

    @Override
    public void execute(Model model) {
        Map<String, Object> m = model.asMap();
        MultipartHttpServletRequest mtfRequest = (MultipartHttpServletRequest) m.get("request");

        // 파라미터 수집
        int board_no = Integer.parseInt(mtfRequest.getParameter("board_no"));
        String user_id = mtfRequest.getParameter("user_id");
        String board_title = mtfRequest.getParameter("board_title");
        String board_content = mtfRequest.getParameter("board_content");
        int b_cate_no = Integer.parseInt(mtfRequest.getParameter("b_cate_no"));

        // 기존 데이터 수정
        iDao.modify(board_no, board_title, board_content, b_cate_no);

        // 파일 경로 설정
        String workPath = System.getProperty("user.dir");
        String root = workPath + "\\src\\main\\resources\\static\\images\\community_img";
        System.out.println(System.getProperty("user.dir"));
        
     
		// 파일 업로드 처리 (일반 이미지)
        // 대표 이미지 처리
        MultipartFile repFile = mtfRequest.getFile("repfile");
        String repImgOriginal = mtfRequest.getParameter("orepfile"); // 기존 대표 이미지 파일명
        String repImgChange = mtfRequest.getParameter("chrepfile");  // 기존 대표 이미지 변경 파일명 
        
		
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
		List<MultipartFile> fileList = mtfRequest.getFiles("file");
		String originalFile = mtfRequest.getParameter("corgfile");   // 기존 파일명
		String changeFile = mtfRequest.getParameter("cchgfile");     // 기존 변경 파일명

		// 새로운 파일이 실제로 업로드되었는지 확인 (파일 크기가 0인 경우는 제외)
		boolean isFileUploaded = fileList.stream().anyMatch(file -> !file.isEmpty());

		if (isFileUploaded) {
		    // 새로운 파일이 있는 경우에만 처리
		    for (MultipartFile mf : fileList) {
		        if (!mf.isEmpty()) {  // 파일이 실제로 존재할 때만 처리
		            originalFile = mf.getOriginalFilename();
		            System.out.println("original: " + originalFile);

		            long longtime = System.currentTimeMillis();
		            changeFile = longtime + "_" + originalFile;
		            String pathFile = root + "\\" + changeFile;

		            try {
		                mf.transferTo(new File(pathFile));
		                System.out.println("다중업로드성공");
		                iDao.modifyImg(board_no, originalFile, changeFile, repImgOriginal, repImgChange);
		            } catch (Exception e) {
		                e.printStackTrace();
		                throw new RuntimeException("Image upload failed", e);
		            }
		        }
		    }
		} else {
		    // 새로운 파일이 없으면 기존 파일명(corgfile, cchgfile)을 그대로 사용하여 데이터베이스 업데이트
		    iDao.modifyImg(board_no, originalFile, changeFile, repImgOriginal, repImgChange);
		}
    }
}