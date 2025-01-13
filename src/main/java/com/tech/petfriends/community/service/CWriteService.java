package com.tech.petfriends.community.service;

	import java.io.File;
	import java.util.List;
	import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.stereotype.Service;
	import org.springframework.ui.Model;
	import org.springframework.web.multipart.MultipartFile;
	import org.springframework.web.multipart.MultipartHttpServletRequest;
	
	import com.tech.petfriends.community.mapper.IDao;
	import com.tech.petfriends.community.service.interfaces.CServiceMInterface;
	@Service
	public class CWriteService implements CServiceMInterface {
	
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
		   
		    String board_content =  "<script>alert('XSS 공격');</script>";
		    System.out.println("board content: " + board_content);
		  
		    String safeBoardContent = Jsoup.clean(board_content, Whitelist.basic()); // 기본 HTML 태그만 허용
		    System.out.println("Safe board content: " + safeBoardContent);
		   
		    int b_cate_no = Integer.parseInt(mtfRequest.getParameter("b_cate_no"));
		    String board_content_input = mtfRequest.getParameter("board_content_input");
		    
		    // 게시글 작성
		    iDao.write(mem_nick, mem_code, board_title, safeBoardContent, b_cate_no, board_content_input);
	
		    System.out.println("board_content_input: " + board_content_input);
		    
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
	
		    // 대표 이미지 정보만 데이터베이스에 저장
		    if (repImgOriginal != null && repImgChange != null) {
		        // 매퍼에서 사용하는 파라미터 이름에 맞게 수정
		        iDao.imgWrite(board_no, repImgOriginal, repImgChange);
		    }
		}
	}