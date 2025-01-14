package com.tech.petfriends.community.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.tech.petfriends.community.dto.CChatDto;
import com.tech.petfriends.community.dto.CDto;
import com.tech.petfriends.community.dto.CReportDto;
import com.tech.petfriends.login.dto.MemberLoginDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CCommunityServiceGroup {

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
	private final CModifyViewService cModifyViewService;
	private final CReplyDeleteService cReplyDeleteService;
	private final CUploadFeedImgService cUploadFeedImgService;
	private final CMyActivityService cMyActivityService;
	private final CUserActivityService cUserActivityService;
	private final CChatHistoryService cChatHistoryService;
	private final CChatRoomService cChatRoomService;
	private final CPostsByCatrgoryService cPostsByCatrgoryService;
	private final CDraftService cDraftService;

	public void loadCommunityMain(HttpSession session, HttpServletRequest request, Model model) {
		model.addAttribute("session", session);
		model.addAttribute("request", request);

		cPostListService.execute(model);
		cStoryListService.storyListExecute(model, session);
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
		cDraftService.deleteDraft(mtfRequest.getParameter("mem_code"));

	}

	public void loadsaveDraft(@RequestParam String mem_code, @RequestParam String board_title,
			@RequestParam String board_content) {

		cDraftService.saveOrUpdateDraft(mem_code, board_title, board_content);

	}

	public CDto loadgetDraft(@RequestParam String mem_code) {

		CDto draft = cDraftService.getDraft(mem_code);

		return draft;
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

	public void loadPostsByCatrgory(int bCateNo, Model model) {

		cPostsByCatrgoryService.GetCatergory(bCateNo, model);
	}

	public void loadModify(MultipartHttpServletRequest mtfRequest, Model model) {
		model.addAttribute("request", mtfRequest);

		cModifyService.execute(model);

	}

	public void loadModifyView(@RequestParam String board_no, Model model) {

		cModifyViewService.loadModifyData(board_no, model);

	}

	public void loadDelete(HttpServletRequest request, Model model) {
		model.addAttribute("request", request);

		cDeleteService.execute(model);

	}

	public void loadComment(HttpServletRequest request, Model model) {
		model.addAttribute("request", request);

		cCommentService.execute(model);

	}

	public void loadCommentReply(HttpServletRequest request, Model model) {
		model.addAttribute("request", request);

		cCommentReplyService.execute(model);
	}

	public void loadReplyDelete(HttpServletRequest request, Model model) {
		model.addAttribute("request", request);

		cReplyDeleteService.execute(model);
	}

	public Map<String, Object> loadUpdateLike(HttpServletRequest request, Model model) {

		return cUpdateLikeService.execute(request, model);
	}

	public void loadMyfeed(String mem_code, HttpSession session, HttpServletRequest request, Model model) {

		model.addAttribute("request", request);
		model.addAttribute("mem_code", mem_code);
		model.addAttribute("session", session);

		cMyFeedService.execute(model);

	}

	public void loadUploadFeedImage(String mem_code, MultipartHttpServletRequest multipartRequest, Model model) {

		model.addAttribute("multipartRequest", multipartRequest);
		model.addAttribute("mem_code", mem_code);
		model.addAttribute("url", "/community/myfeed/" + mem_code);

		cUploadFeedImgService.execute(model);

	}

	public void loadNeighborList(@PathVariable String mem_code, HttpSession session, HttpServletRequest request,
			Model model) {
		System.out.println("neighborList()");

		model.addAttribute("session", session);
		model.addAttribute("request", request);
		cNeighborListService.execute(model);

	}

	public void loadMyNeighborList(String mem_code, HttpSession session, HttpServletRequest request, Model model) {
		System.out.println("MyNeighborList()");

		model.addAttribute("session", session);
		model.addAttribute("request", request);

		cMyNeighborListService.execute(model);
	}

	public void loadMainNeighborList(HttpSession session, HttpServletRequest request, Model model) {
		System.out.println("MyNeighborList()");

		model.addAttribute("session", session);
		model.addAttribute("request", request);

		cMyNeighborListService.execute(model);

	}

	public void loadCommunityReport(CReportDto reportDto) {

		cReportService.Reportexecute(reportDto);

	}

	public void loadAddFriend(@PathVariable String mem_code, HttpSession session, HttpServletRequest request,
			Model model) {

		model.addAttribute("session", session);
		model.addAttribute("request", request);
		cFriendService.execute(model);

	}

	public ArrayList<CDto> loadMyActivityList(HttpSession session) {
		MemberLoginDto loginUser = (MemberLoginDto) session.getAttribute("loginUser");
		String user_id = loginUser.getMem_nick();

		return cMyActivityService.getMyActivityList(user_id);

	}

	public ArrayList<CDto> loadUserActivityList(HttpSession session) {
		MemberLoginDto loginUser = (MemberLoginDto) session.getAttribute("loginUser");
		String user_id = loginUser.getMem_nick();

		return cUserActivityService.getUserActivityList(user_id);
	}

	public List<CChatDto> loadGetChatHistory(String roomId) {

		return cChatHistoryService.getChatHistoryList(roomId);
	}

	public List<CChatDto> loadGetChatRooms(HttpSession session) {

		return cChatRoomService.getChatRooms(session);
	}

}
