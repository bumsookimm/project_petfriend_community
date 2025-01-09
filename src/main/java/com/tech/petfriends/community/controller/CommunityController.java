package com.tech.petfriends.community.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
	import org.springframework.web.bind.annotation.RequestBody;
	import org.springframework.web.bind.annotation.RequestMapping;
	import org.springframework.web.bind.annotation.RequestParam;
	import org.springframework.web.bind.annotation.ResponseBody;
	import org.springframework.web.multipart.MultipartHttpServletRequest;
	
	import com.tech.petfriends.community.dto.CChatDto;
	import com.tech.petfriends.community.dto.CCommunityFriendDto;
	import com.tech.petfriends.community.dto.CDto;
	import com.tech.petfriends.community.dto.CReportDto;
	import com.tech.petfriends.community.service.CCommunityServiceGroup;
	
	import lombok.RequiredArgsConstructor;
	
	@Controller
	@RequestMapping("/community")
	@RequiredArgsConstructor
	public class CommunityController {


	private final CCommunityServiceGroup cCommunityServiceGroup;

	// 커뮤니티 페이지로 이동
	@GetMapping("/main")
	public String communityMain(HttpSession session, HttpServletRequest request, Model model) {

		cCommunityServiceGroup.loadCommunityMain(session, request, model);
		return "/community/main";
	}

	@GetMapping("/writeView")
	public String writeView(HttpSession session, HttpServletRequest request, Model model) {

		cCommunityServiceGroup.loadwriteView(session, request, model);
		return "/community/writeView";
	}

	@PostMapping("/write")
	public String communityWrite(MultipartHttpServletRequest mtfRequest, Model model) {

		cCommunityServiceGroup.loadcommunityWrite(mtfRequest, model);
		return "/community/alert";

	}

	@GetMapping("/download")
	public String download(HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {

		cCommunityServiceGroup.loadDownload(request, model, response);
		String bid = request.getParameter("bid");
		return "contentView?bid=" + bid;
	}

	@GetMapping("/contentView")
	public String contentView(HttpSession session, HttpServletRequest request, Model model) {

		cCommunityServiceGroup.loadContentView(session, request, model);
		return "/community/contentView";

	}

	@GetMapping("/getPostsByCategory")
	public String getPostsByCategory(@RequestParam("b_cate_no") int bCateNo, Model model) {

		cCommunityServiceGroup.loadPostsByCatrgory(bCateNo, model);

		return "community/postList";
	}

	@PostMapping("/modify")
	public String modify(MultipartHttpServletRequest mtfRequest, Model model) {

		model.addAttribute("request", mtfRequest);
		cCommunityServiceGroup.loadModify(mtfRequest, model);

		return "redirect:/community/contentView?board_no=" + mtfRequest.getParameter("board_no");

	}

	@GetMapping("/modifyView")
	public String modifyView(@RequestParam String board_no, Model model) {

		cCommunityServiceGroup.loadModifyView(board_no, model);

		return "/community/modifyView";

	}

	@PostMapping("/delete")
	public String delete(HttpServletRequest request, Model model) {

		cCommunityServiceGroup.loadDelete(request, model);

		return "redirect:/community/main";
	}

	@PostMapping("/comment")
	public String comment(HttpServletRequest request, Model model) {

		cCommunityServiceGroup.loadComment(request, model);

		return "redirect:/community/contentView?board_no=" + request.getParameter("board_no");
	}

	@PostMapping("/commentReply")
	public String commentReply(HttpServletRequest request, Model model) {

		cCommunityServiceGroup.loadCommentReply(request, model);

		return "redirect:/community/contentView?board_no=" + request.getParameter("board_no");
	}

	@PostMapping("/replyDelete")
	public String replyDelete(HttpServletRequest request, Model model) {

		cCommunityServiceGroup.loadReplyDelete(request, model);

		return "community/alert";
	}

	@PostMapping("/updateLike")
	public ResponseEntity<Map<String, Object>> updateLike(HttpServletRequest request, Model model) {

		Map<String, Object> response = cCommunityServiceGroup.loadUpdateLike(request, model);

		return ResponseEntity.ok(response);

	}

	@GetMapping("/myfeed/{mem_code}")
	public String myfeed(@PathVariable String mem_code, HttpSession session, HttpServletRequest request, Model model) {

		cCommunityServiceGroup.loadMyfeed(mem_code, session, request, model);

		return "/community/myfeed";
	}

	@PostMapping("/upload/{mem_code}")
	public String uploadFeedImage(@PathVariable String mem_code, MultipartHttpServletRequest multipartRequest,
			Model model) {

		cCommunityServiceGroup.loadUploadFeedImage(mem_code, multipartRequest, model);

		return "/community/alert";
	}

	@GetMapping("/neighborList/{mem_code}")
	@ResponseBody
	public ArrayList<CCommunityFriendDto> neighborList(@PathVariable String mem_code, HttpSession session,
			HttpServletRequest request, Model model) {

		cCommunityServiceGroup.loadNeighborList(mem_code, session, request, model);

		@SuppressWarnings("unchecked")
		ArrayList<CCommunityFriendDto> neighborList = (ArrayList<CCommunityFriendDto>) model
				.getAttribute("neighborList");

		return neighborList;
	}

	@GetMapping("/myNeighborList/{mem_code}")
	@ResponseBody
	public ArrayList<CCommunityFriendDto> myNeighborList(@PathVariable String mem_code, HttpSession session,
			HttpServletRequest request, Model model) {

		cCommunityServiceGroup.loadMyNeighborList(mem_code, session, request, model);
		@SuppressWarnings("unchecked")
		ArrayList<CCommunityFriendDto> myNeighborList = (ArrayList<CCommunityFriendDto>) model
				.getAttribute("MyNeighborList");

		return myNeighborList;
	}

	@GetMapping("/MainNeighborList")
	@ResponseBody
	public ArrayList<CCommunityFriendDto> MainNeighborList(HttpSession session, HttpServletRequest request,
			Model model) {

		cCommunityServiceGroup.loadMainNeighborList(session, request, model);

		@SuppressWarnings("unchecked")
		ArrayList<CCommunityFriendDto> MainNeighborList = (ArrayList<CCommunityFriendDto>) model
				.getAttribute("MyNeighborList");

		return MainNeighborList;
	}

	@PostMapping("/report")
	public ResponseEntity<Map<String, String>> communityReport(@RequestBody CReportDto reportDto) {

		cCommunityServiceGroup.loadCommunityReport(reportDto);

		Map<String, String> response = new HashMap<>();
		response.put("message", "신고가 제출되었습니다.");

		return ResponseEntity.ok(response);

	}

	@GetMapping("/addFriend/{mem_code}")
	public String addFriend(@PathVariable String mem_code, HttpSession session, HttpServletRequest request,
			Model model) {
		cCommunityServiceGroup.loadAddFriend(mem_code, session, request, model);

		return "redirect:/community/myfeed/" + mem_code;
	}

	@GetMapping("/myActivity")
	@ResponseBody
	public ArrayList<CDto> activityList(HttpSession session) {

		ArrayList<CDto> activityList = cCommunityServiceGroup.loadMyActivityList(session);

		return activityList;

	}

	@GetMapping("/userActivity")
	@ResponseBody
	public ArrayList<CDto> userActivityList(HttpSession session) {

		ArrayList<CDto> activityList = cCommunityServiceGroup.loadUserActivityList(session);

		return activityList;
	}

	@GetMapping("/getChatHistory")
	@ResponseBody
	public List<CChatDto> getChatHistory(@RequestParam String roomId) {

		List<CChatDto> getChatHistory = cCommunityServiceGroup.loadGetChatHistory(roomId);

		return getChatHistory;
	}

	@GetMapping("/getChatRooms")
	@ResponseBody
	public List<CChatDto> getChatRooms(HttpSession session) {

		List<CChatDto> getChatRooms = cCommunityServiceGroup.loadGetChatRooms(session);

		return getChatRooms;
	}

}