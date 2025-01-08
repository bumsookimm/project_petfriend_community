package com.tech.petfriends.community.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.tech.petfriends.community.dto.CCategoryDto;
import com.tech.petfriends.community.dto.CChatDto;
import com.tech.petfriends.community.dto.CCommunityFriendDto;
import com.tech.petfriends.community.dto.CDto;
import com.tech.petfriends.community.dto.CReportDto;
import com.tech.petfriends.login.dto.MemberLoginDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CCommunityServiceGroup {

	private final CChatService cChatService;
	private final CCommentReplyService cCommentReplyService;
	private final CCommentService cCommentService;
	private final CContentViewService cContentViewService;
	private final CDeleteService cDeleteService;
	private final CDownloadService cDownloadService;
	private final CFriendService cFriendService;
	private final CModifyService cModifyService;
	private final CMyFeedService cMyFeedService;
	private final CMyNeighborListService cMyNeighborListService;
	private final CNeighborListService cNeighborListService;
	private final CPostListService cPostListService;
	private final CReportService cReportService;
	private final CStoryListService cStoryListService;
	private final CUpdateLikeService cUpdateLikeService;
	private final CWriteService cWriteService;
	private final CWriteViewService cWriteViewService;      

	
	
	public void loadCommunityMain(HttpSession session, HttpServletRequest request, Model model) {		
		model.addAttribute("session", session);
		model.addAttribute("request", request);
		
		cPostListService.execute(model);
		cStoryListService.execute(model);
	}
	
	
	
	
	public void loadwriteView(HttpSession session, HttpServletRequest request, Model model) {
		model.addAttribute("session", session);
		model.addAttribute("request", request);

		cWriteViewService.execute(model);
		
	
	}



	public void loadcommunityWrite(MultipartHttpServletRequest mtfRequest, Model model) {
		
		model.addAttribute("request", mtfRequest);
		model.addAttribute("msg", "게시글이 작성됐습니다.");
		model.addAttribute("url", "/community/main");
		
		cWriteService.execute(model);
		

	}

	
	public void loadDownload(HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {

		model.addAttribute("request", request);
		model.addAttribute("response", response);
	
		cDownloadService.execute(model);


	}


	public void loadContentView(HttpSession session, HttpServletRequest request, Model model) {
		
		model.addAttribute("request", request);
		model.addAttribute("session", session);
	
		cContentViewService.execute(model);

	
	}



	public void loadModify(MultipartHttpServletRequest mtfRequest, Model model) {
		model.addAttribute("request", mtfRequest);
	
		cModifyService.execute(model);	

	}

	
	@GetMapping("/modifyView")
	public String modifyView(@RequestParam("board_no") String board_no, Model model) {
		CDto content = iDao.contentView(board_no); // 게시글 정보를 가져옴
		model.addAttribute("contentView", content); // 게시글 정보를 모델에 담아서 JSP로 전달

		CWriteViewService categoryService = new CWriteViewService(iDao);
		List<CCategoryDto> categoryList = iDao.getCategoryList();
		model.addAttribute("categoryList", categoryList);

		return "/community/modifyView";

	}

	@PostMapping("/delete")
	public String delete(HttpServletRequest request, Model model) {
		System.out.println("community_delete");
		model.addAttribute("request", request);

		serviceInterface = new CDeleteService(iDao);
		serviceInterface.execute(model);

		return "redirect:/community/main";
	}

	@PostMapping("/comment")
	public String comment(HttpServletRequest request, Model model) {
		System.out.println("community_comment");
		model.addAttribute("request", request);

		serviceInterface = new CCommentService(iDao);
		serviceInterface.execute(model);

		return "redirect:/community/contentView?board_no=" + request.getParameter("board_no");
	}

	@PostMapping("/commentReply")
	public String commentReply(HttpServletRequest request, Model model) {
		System.out.println("commentReply");
		model.addAttribute("request", request);

		serviceInterface = new CCommentReplyService(iDao);
		serviceInterface.execute(model);

		return "redirect:/community/contentView?board_no=" + request.getParameter("board_no");
	}

	@PostMapping("/replyDelete")
	public String replyDelete(HttpServletRequest request, Model model) {
	
		model.addAttribute("request", request);
		
		String mem_nick = request.getParameter("mem_nick");	
		String mem_code = request.getParameter("mem_code");	
		String board_no = request.getParameter("board_no");
		String comment_no = request.getParameter("comment_no");
		String parent_comment_no = request.getParameter("parent_comment_no");
		String comment_level = request.getParameter("comment_level");
		String comment_order_no = request.getParameter("comment_order_no");
		
		System.out.println("mem_nick:"+ mem_nick);
		
		//만약 로그인 닉네임이 구트아카데미 라면 관리자 삭제로 진행 그게 아니라면 댓글삭제
		if(mem_nick.equals("구트아카데미")) {
			iDao.managerReplyUpdate(mem_nick, mem_code, comment_no);
			model.addAttribute("msg", "관리자 댓글 삭제 성공");
			model.addAttribute("url", "/community/contentView?board_no=" + board_no);
			return"/community/alert";
			
		} else {
		// 댓글 삭제 시도
		int rn = iDao.replyDelete(comment_no, parent_comment_no, comment_level, comment_order_no);
		if (rn == 0) {
			// 삭제 실패 (상위 댓글이 존재)
			System.out.println("댓글 삭제 실패");
			model.addAttribute("msg", "이 댓글은 상위 댓글을 가지고 있어 삭제할 수 없습니다.");
			model.addAttribute("url", "/community/contentView?board_no=" + board_no);
			return "/community/alert";

		} else {
			// 삭제 성공
			System.out.println("댓글 삭제 성공");
			iDao.stepInit(comment_no, parent_comment_no, comment_level);
			model.addAttribute("msg", "댓글이 삭제됐습니다.");
			model.addAttribute("url", "/community/contentView?board_no=" + board_no);
			return "/community/alert";
		}
		}
	}

	@PostMapping("/updateLike")
	public ResponseEntity<Map<String, Object>> updateLike(HttpServletRequest request, Model model) {
		System.out.println("updateLike");
		model.addAttribute("request", request);

		serviceInterface = new CUpdateLikeService(iDao);
		serviceInterface.execute(model);

		Map<String, Object> response = new HashMap<>();
		response.put("likes", model.getAttribute("likes"));
		response.put("likesCount", model.getAttribute("likesCount"));

		return ResponseEntity.ok(response); // JSON 형식으로 응답 반환
	}

	@GetMapping("/myfeed/{mem_code}")
	public String myfeed(@PathVariable String mem_code, HttpSession session, HttpServletRequest request, Model model) {
		CDto getMyfeedVisit = (CDto) iDao.getMyfeedVisit(mem_code);
		
		model.addAttribute("getMyfeedVisit", getMyfeedVisit);
		model.addAttribute("request", request);
		model.addAttribute("mem_code", mem_code);
		model.addAttribute("session", session);
		System.out.println(mem_code);
		iDao.totalVisits(mem_code);
		iDao.dailyVisits(mem_code);
	    
		
	    serviceInterface = new CMyFeedService(iDao);
		serviceInterface.execute(model);
			
		
		return "/community/myfeed";
	}

	
	@PostMapping("/upload/{mem_code}")
	public String uploadFeedImage(@PathVariable String mem_code, MultipartHttpServletRequest multipartRequest, Model model) {
	    // 파일 처리
		System.out.println("mem_code:");
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
		model.addAttribute("url", "/community/myfeed/" + mem_code);
	    
	    
	    return "/community/alert";  // 업로드 후 피드 페이지로 리다이렉트
	}
	
	
	
	
	@GetMapping("/neighborList/{mem_code}")
	@ResponseBody
	public ArrayList<CCommunityFriendDto> neighborList(@PathVariable String mem_code, HttpSession session, HttpServletRequest request, Model model) {
	    System.out.println("neighborList()");

	    model.addAttribute("session", session);
	    model.addAttribute("request", request);
	    serviceInterface = new CNeighborListService(iDao);
	    serviceInterface.execute(model);
	    
	    ArrayList<CCommunityFriendDto> neighborList = (ArrayList<CCommunityFriendDto>) model.getAttribute("neighborList");
	    System.out.println("neighborList" + neighborList.size());
	    return neighborList;  
	}
	
	
	@GetMapping("/myNeighborList/{mem_code}")
	@ResponseBody
	public ArrayList<CCommunityFriendDto> myNeighborList(@PathVariable String mem_code, HttpSession session, HttpServletRequest request, Model model) {
	    System.out.println("MyNeighborList()");

	    model.addAttribute("session", session);
	    model.addAttribute("request", request);
	    serviceInterface = new CMyNeighborListService(iDao);
	    serviceInterface.execute(model);
	    
	    ArrayList<CCommunityFriendDto> myNeighborList = (ArrayList<CCommunityFriendDto>) model.getAttribute("MyNeighborList");
	   
	    return myNeighborList;  
	}
	
	
	
	@GetMapping("/MainNeighborList")
	@ResponseBody
	public ArrayList<CCommunityFriendDto> MainNeighborList(HttpSession session, HttpServletRequest request, Model model) {
	    System.out.println("MyNeighborList()");

	    model.addAttribute("session", session);
	    model.addAttribute("request", request);
	    serviceInterface = new CMyNeighborListService(iDao);
	    serviceInterface.execute(model);
	    
	    ArrayList<CCommunityFriendDto> MainNeighborList = (ArrayList<CCommunityFriendDto>) model.getAttribute("MyNeighborList");
	   
	    return MainNeighborList;  
	}
	
	
	@PostMapping("/report")
	public  ResponseEntity<Map<String, String>> communityReport(@RequestBody CReportDto reportDto, Model model) {
		
	
		System.out.println("communityReport");
		
		model.addAttribute("board_no", reportDto.getBoard_no());
		model.addAttribute("mem_code", reportDto.getMem_code());
	    model.addAttribute("reporter_id", reportDto.getReporter_id());
	    model.addAttribute("reason", reportDto.getReason());
	    model.addAttribute("comment_no", reportDto.getComment_no());
	    model.addAttribute("report_type", reportDto.getReport_type());
	   
	
	    System.out.println("reportDto.getMem_code() " + reportDto.getMem_code());
	    System.out.println("reportDto.getBoard_no() " + reportDto.getBoard_no());
	    System.out.println("reportDto.reporter_id() " + reportDto.getReporter_id());
	    System.out.println("reportDto.reason() " + reportDto.getReason());
	    System.out.println("reportDto.getComment_no() " + reportDto.getComment_no());
	  
	   
	    serviceInterface = new CReportService(iDao);
	    serviceInterface.execute(model);
	   
	    Map<String, String> response = new HashMap<>();
	    response.put("message", "신고가 제출되었습니다.");
	    return ResponseEntity.ok(response);
	    
	}

	
	@GetMapping("/addFriend/{mem_code}")
	public String addFriend(@PathVariable String mem_code, HttpSession session, HttpServletRequest request, Model model) {
	    System.out.println("addFriend()");
	    model.addAttribute("session", session);
	    model.addAttribute("request", request);
	    serviceInterface = new CFriendService(iDao);
	    serviceInterface.execute(model);
	    System.out.println("mem_code: " + mem_code);
	  
	   
	    
	    System.out.println("isFriendBool: " + model.getAttribute("isFriendBool")); // 디버깅용
	    return "redirect:/community/myfeed/" + mem_code;
	}
	
	@GetMapping("/myActivity")
	@ResponseBody
	public ArrayList<CDto> activityList (Model model, HttpSession session) {
		MemberLoginDto loginUser = (MemberLoginDto) session.getAttribute("loginUser"); 
		String user_id = loginUser.getMem_nick();
		System.out.println("user_id: "+ user_id);
           
	    ArrayList<CDto> activityList = iDao.myActivityList(user_id);
	    System.out.println("Returned activityList: " + activityList);
	
	    return activityList;
     
	}
	
	@GetMapping("/userActivity")
	@ResponseBody
	public ArrayList<CDto> userActivityList (Model model, HttpSession session) {
		MemberLoginDto loginUser = (MemberLoginDto) session.getAttribute("loginUser"); 
		String user_id = loginUser.getMem_nick();
		System.out.println("user_id: "+ user_id);
           
	    ArrayList<CDto> activityList = iDao.userActivityList(user_id);
	    System.out.println("Returned activityList: " + activityList);
	
	    return activityList;
	}
	
	
    @GetMapping("/getChatHistory")
    @ResponseBody
    public List<CChatDto> getChatHistory(@RequestParam("roomId") String roomId) {
      System.out.println("roomId:"+roomId);
    	// 서비스에서 roomId로 메시지 리스트 조회
    	List<CChatDto> getChatHistory = iDao.getChatHistory(roomId);
    	
    	
    	return getChatHistory;
    }
	
    
    
    @GetMapping("/getChatRooms")
    @ResponseBody
    public List<CChatDto> getChatRooms(HttpSession session) {
        MemberLoginDto loginUser = (MemberLoginDto) session.getAttribute("loginUser");
        if (loginUser == null) {
            throw new RuntimeException("로그인 정보가 없습니다."); // 혹은 적절한 응답 반환
        }

        String sender = loginUser.getMem_nick();
        System.out.println("sender:" + sender);

        List<CChatDto> getChatRooms = iDao.getChatRooms(sender);
        return getChatRooms;
    }
	
	
	
}
