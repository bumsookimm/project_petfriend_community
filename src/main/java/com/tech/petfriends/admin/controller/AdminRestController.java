package com.tech.petfriends.admin.controller;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tech.petfriends.admin.dto.ACommunityDto;
import com.tech.petfriends.admin.mapper.AdminCommunityDao;
import com.tech.petfriends.admin.mapper.AdminPageDao;
import com.tech.petfriends.admin.service.AdminPethotelDataService;
import com.tech.petfriends.admin.service.AdminPethotelInfoData;
import com.tech.petfriends.admin.service.AdminPethotelInfoEditService;
import com.tech.petfriends.admin.service.AdminPethotelIntroData;
import com.tech.petfriends.admin.service.AdminPethotelIntroEditService;
import com.tech.petfriends.admin.service.AdminPethotelReserveDetailService;
import com.tech.petfriends.admin.service.AdminPethotelReserveUpdateService;
import com.tech.petfriends.admin.service.AdminPetteacherDataService;
import com.tech.petfriends.admin.service.AdminPetteacherDeleteService;
import com.tech.petfriends.admin.service.AdminPetteacherDetailService;
import com.tech.petfriends.admin.service.AdminPetteacherEditService;
import com.tech.petfriends.admin.service.AdminPetteacherWriteService;
import com.tech.petfriends.admin.service.AdminServiceInterface;
import com.tech.petfriends.helppetf.dto.PethotelInfoDto;
import com.tech.petfriends.helppetf.dto.PethotelIntroDto;
import com.tech.petfriends.helppetf.dto.PethotelMemDataDto;
import com.tech.petfriends.helppetf.dto.PetteacherDto;
import com.tech.petfriends.login.dto.MemberLoginDto;
import com.tech.petfriends.login.mapper.MemberMapper;
import com.tech.petfriends.mypage.dao.MypageDao;
import com.tech.petfriends.mypage.dto.MyPetDto;
import com.tech.petfriends.notice.dao.NoticeDao;
import com.tech.petfriends.notice.dto.EventDto;
import com.tech.petfriends.notice.dto.NoticeDto;

@RestController
@RequestMapping("/admin")
public class AdminRestController {

	@Autowired
	AdminPageDao adminDao;

	@Autowired
	NoticeDao noticeDao;
	
	@Autowired
	MemberMapper memberMapper;
	
	@Autowired
	MypageDao mypageDao;

	@Autowired
	AdminCommunityDao communtiyDao;

	AdminServiceInterface adminServiceInterface;

	@PostMapping("/pethotel_reserve_update")
	public String pethotelReserveUpdate(@RequestBody Map<String, String> statusMap, HttpServletRequest request,
			Model model) {
		model.addAttribute("statusMap", statusMap);
		model.addAttribute("request", request);

		adminServiceInterface = new AdminPethotelReserveUpdateService(adminDao);
		adminServiceInterface.execute(model);
		
		return "{\"status\": \"success\"}";
	}

	@GetMapping("/pethotel_admin_reserve_detail")
	public String pethotelReserveDetail(HttpServletRequest request, Model model) throws JsonProcessingException {
		AdminPethotelReserveDetailService adminService = new AdminPethotelReserveDetailService(adminDao);
		
		return adminService.execute(model, request);
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/pethotel_admin_reserve")
	public ArrayList<PethotelMemDataDto> pethotelReserveData(HttpServletRequest request, Model model) {
		model.addAttribute("request", request);
		adminServiceInterface = new AdminPethotelDataService(adminDao);
		adminServiceInterface.execute(model);
		
		return (ArrayList<PethotelMemDataDto>) model.getAttribute("memSelectDto");
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/petteacher_admin_data")
	public List<PetteacherDto> getPetteacherData(HttpServletRequest request, Model model) {
		model.addAttribute("request", request);
		adminServiceInterface = new AdminPetteacherDataService(adminDao);
		adminServiceInterface.execute(model);
		
		return (List<PetteacherDto>) model.getAttribute("petteacherList");
	}

	@GetMapping("/petteacher_admin_data_forEdit")
	public PetteacherDto petteacherDataForEdit(HttpServletRequest request, Model model) {
		model.addAttribute("request", request);
		adminServiceInterface = new AdminPetteacherDetailService(adminDao);
		adminServiceInterface.execute(model);

		return (PetteacherDto) model.getAttribute("dto");
	}

	@DeleteMapping("/petteacher_admin_data_forDelete")
	public String petteacherDataForDelete(HttpServletRequest request, Model model) {
		model.addAttribute("request", request);
		adminServiceInterface = new AdminPetteacherDeleteService(adminDao);
		adminServiceInterface.execute(model);

		return "{\"status\": \"success\"}";
	}

	@PostMapping("/petteacher_admin_data_forWrite")
	public String petteacherDataForWrite(@RequestBody PetteacherDto dto, Model model) {
		model.addAttribute("dto", dto);
		adminServiceInterface = new AdminPetteacherWriteService(adminDao);
		adminServiceInterface.execute(model);
		
		return "{\"status\": \"success\"}";
	}

	@PutMapping("/petteacher_admin_data_forEdit")
	public String petteacherDataForEdit(@RequestBody PetteacherDto dto, HttpServletRequest request, Model model) {
		model.addAttribute("request", request);
		model.addAttribute("dto", dto);
		adminServiceInterface = new AdminPetteacherEditService(adminDao);
		adminServiceInterface.execute(model);

		return "{\"status\": \"success\"}";
	}

	@GetMapping("/pethotel_intro_data")
	public PethotelIntroDto pethotelIntroData(Model model) {
		adminServiceInterface = new AdminPethotelIntroData(adminDao);
		adminServiceInterface.execute(model);
		return (PethotelIntroDto) model.getAttribute("dto");
	}

	@GetMapping("/pethotel_info_data")
	public PethotelInfoDto pethotelInfoData(Model model) {
		adminServiceInterface = new AdminPethotelInfoData(adminDao);
		adminServiceInterface.execute(model);
		return (PethotelInfoDto) model.getAttribute("dto");
	}

	@PutMapping("/pethotel_admin_intro_dataForEdit")
	public String pethotelIntroForEdit(@RequestBody PethotelIntroDto dto, HttpServletRequest request, Model model) {
		model.addAttribute("request", request);
		model.addAttribute("dto", dto);
		adminServiceInterface = new AdminPethotelIntroEditService(adminDao);
		adminServiceInterface.execute(model);

		return "{\"status\": \"success\"}";
	}

	@PutMapping("/pethotel_admin_info_dataForEdit")
	public String pethotelInfoForEdit(@RequestBody PethotelInfoDto dto, HttpServletRequest request, Model model) {
		model.addAttribute("request", request);
		model.addAttribute("dto", dto);
		adminServiceInterface = new AdminPethotelInfoEditService(adminDao);
		adminServiceInterface.execute(model);

		return "{\"status\": \"success\"}";
	}

	@GetMapping("/notice_notice_list")
	public ArrayList<NoticeDto> noticeNoticeList() {
		ArrayList<NoticeDto> noticeList = noticeDao.noticeAdminList();
		return noticeList;
	}

	@GetMapping("/notice_event_list")
	public ArrayList<EventDto> noticeEventList() {
		ArrayList<EventDto> eventList = noticeDao.eventAdminList();
		return eventList;
	}

	// 공지사항 삭제 메서드
	@DeleteMapping("/deleteNotice")
	public ResponseEntity<String> deleteNotice(@RequestParam("id") Long noticeNo) {
		try {
			int isDeleted = noticeDao.deleteNotice(noticeNo);
			if (isDeleted > 0) {
				return ResponseEntity.ok("Notice deleted successfully.");
			} else {
				return ResponseEntity.status(404).body("Notice not found.");
			}
		} catch (Exception e) {
			return ResponseEntity.status(500).body("An error occurred while deleting the notice.");
		}
	}


	@PostMapping("/community")
	@ResponseBody
	public Map<String, Object> searchCommunityBoard(@RequestBody Map<String, String> requestData,Model model) {
	    System.out.println("searchCommunityBoard");
	    String searchKeyword = requestData.get("searchKeyword");
	    String searchFilterType = requestData.get("searchFilterType");
	    String searchCategory = requestData.get("searchCategory");
	    String searchStartDate = requestData.get("searchStartDate");
	    String searchEndDate = requestData.get("searchEndDate");     
	    
	    
	    // 날짜 형식 변환 (문자열 -> LocalDate)
	    LocalDate startDate = null;
	    LocalDate endDate = null;
	        
	    // 시작 날짜가 있을 경우 LocalDate로 변환
	    if (searchStartDate != null && !searchStartDate.isEmpty()) {
	        startDate = LocalDate.parse(searchStartDate, DateTimeFormatter.ISO_LOCAL_DATE);
	    }

	    // 종료 날짜가 있을 경우 LocalDate로 변환
	    if (searchEndDate != null && !searchEndDate.isEmpty()) {
	        endDate = LocalDate.parse(searchEndDate, DateTimeFormatter.ISO_LOCAL_DATE);
	    }   
	   
	    // 해당 페이지에 맞는 게시물 리스트 조회
	    List<ACommunityDto> communityList = communtiyDao.communityList(
	        searchKeyword, searchFilterType, searchCategory, searchStartDate, searchEndDate );
	 
	    int totalItems = communtiyDao.totalItems(
	            searchKeyword, searchFilterType, searchCategory, searchStartDate, searchEndDate);
	    
	    System.out.println("조회된 게시물 수: " + communityList.size());  // 게시물 수 확인
	    Map<String, Object> response = new HashMap<>();
	    response.put("communityList", communityList);
	    response.put("totalItems", totalItems);
	        
	    return response;
	}

	@PostMapping("/report")
	@ResponseBody
	public Map<String, Object> searchReport(@RequestBody Map<String, String> requestData, Model model) {
	    System.out.println("searchReport");
	    String reportSearchKeyword = requestData.get("searchKeyword");
	    String reportSearchFilterType = requestData.get("searchFilterType");
	    String reportCategory = requestData.get("searchCategory");
	    String reportStartDate = requestData.get("searchStartDate");
	    String reportEndDate = requestData.get("searchEndDate");

	    
	    
	    // 날짜 형식 변환 (문자열 -> LocalDate)
	    LocalDate startDate = null;
	    LocalDate endDate = null;

	    // 시작 날짜가 있을 경우 LocalDate로 변환
	    if (reportStartDate != null && !reportStartDate.isEmpty()) {
	        startDate = LocalDate.parse(reportStartDate, DateTimeFormatter.ISO_LOCAL_DATE);
	    }

	    // 종료 날짜가 있을 경우 LocalDate로 변환
	    if (reportEndDate != null && !reportEndDate.isEmpty()) {
	        endDate = LocalDate.parse(reportEndDate, DateTimeFormatter.ISO_LOCAL_DATE);
	    }

	    // 신고 리스트 조회
	    List<ACommunityDto> reportList = communtiyDao.reportList(
	        reportSearchKeyword, reportSearchFilterType, reportCategory, reportStartDate, reportEndDate);

	    int totalReportItems = communtiyDao.totalItems(
	        reportSearchKeyword, reportSearchFilterType, reportCategory, reportStartDate, reportEndDate);

	    System.out.println("조회된 신고 내역 수: " + reportList.size()); // 신고 내역 수 확인
	    
	    Map<String, Object> response = new HashMap<>();
	    response.put("reportList", reportList);
	    response.put("totalItems", totalReportItems);
	    
	    return response;
	}
	// 이벤트 삭제 메서드
	@DeleteMapping("/deleteEvent")
	public ResponseEntity<String> deleteEvent(@RequestParam("id") Long eventNo) {
		try {
			int isDeleted = noticeDao.deleteEvent(eventNo);
			if (isDeleted > 0) {
				return ResponseEntity.ok("Event deleted successfully.");
			} else {
				return ResponseEntity.status(404).body("Event not found.");
			}
		} catch (Exception e) {
			return ResponseEntity.status(500).body("An error occurred while deleting the Event.");
		}
	}
	
	@PostMapping("/setVisibilityForNotices")
    public ResponseEntity<?> setVisibilityForNotices(@RequestBody Map<String, Object> request) {
        List<Long> ids = (List<Long>) request.get("ids");
        String visibility = (String) request.get("visibility");

        if (ids != null && !ids.isEmpty()) {
            boolean isVisible = "show".equals(visibility);
            noticeDao.updateVisibilityNotice(ids, isVisible); // MyBatis 매퍼에서 공개 여부 업데이트
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body("유효하지 않은 요청입니다.");
        }
    }
	
	@PostMapping("/setVisibilityForEvents")
    public ResponseEntity<?> setVisibilityForEvents(@RequestBody Map<String, Object> request) {
        List<Long> ids = (List<Long>) request.get("ids");
        String visibility = (String) request.get("visibility");

        if (ids != null && !ids.isEmpty()) {
            boolean isVisible = "show".equals(visibility);
            noticeDao.updateVisibilityEvent(ids, isVisible); // MyBatis 매퍼에서 공개 여부 업데이트
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body("유효하지 않은 요청입니다.");
        }
    }
	
	@GetMapping("/customer_list")
	public ArrayList<MemberLoginDto> customerList() {
		ArrayList<MemberLoginDto> memberlist = memberMapper.memberList();	
		return memberlist;
	}
	
	@GetMapping("/pet_list")
	public ArrayList<MyPetDto> petList() {
		ArrayList<MyPetDto> petlist = mypageDao.getPetList();
		return petlist;
	}

	@PostMapping("/updateCustomerType")
	public ResponseEntity<Map<String, String>> updateCustomerType(@RequestBody Map<String, Object> request) {
	    List<Long> ids = (List<Long>) request.get("ids");
	    String newType = (String) request.get("newType");

	    if (ids == null || ids.isEmpty() || newType == null || newType.isEmpty()) {
	        return ResponseEntity.badRequest().body(Map.of("message", "유효하지 않은 요청입니다."));
	    }

	    try {
	        memberMapper.updateCustomerType(ids, newType); // MyBatis 매퍼 호출
	        return ResponseEntity.ok(Map.of("message", "회원 유형이 성공적으로 변경되었습니다."));
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "회원 유형 변경 중 오류가 발생했습니다."));
	    }
	}
	
	@PostMapping("/deletePetImages")
	public ResponseEntity<String> deletePetImages(@RequestBody Map<String, Object> request) {
	    List<String> petCodes = (List<String>) request.get("petCodes");
	    if (petCodes == null || petCodes.isEmpty()) {
	        return ResponseEntity.badRequest().body("유효하지 않은 요청입니다.");
	    }

	    try {
	        petCodes.forEach(petCode -> {
	            String petImg = mypageDao.getPetImgForPetCode(petCode); // petCode로 이미지 파일명 조회
	            if (petImg != null && !petImg.isEmpty()) {
	                // 파일 경로 설정 (서버의 실제 경로를 절대 경로로 사용)
	            	String imagesDir = new File("src/main/resources/static/Images/pet/").getAbsolutePath();
	                File file = new File(imagesDir, petImg);
	                
	                // 파일 존재 여부 확인 및 삭제 처리
	                if (file.exists()) {
	                    boolean deleted = file.delete();
	                    if (!deleted) {
	                        System.err.println("이미지 삭제에 실패했습니다: " + file.getAbsolutePath());
	                    } else {
	                        System.out.println("이미지 삭제 성공: " + file.getAbsolutePath());
	                    }
	                } else {
	                    System.out.println("이미지가 존재하지 않습니다: " + file.getAbsolutePath());
	                }
	            }
	            mypageDao.deletePetImgForPetCode(petCode);
	        });
	        return ResponseEntity.ok("이미지 삭제가 완료되었습니다.");
	    } catch (Exception e) {
	        e.printStackTrace(); // 디버깅을 위한 예외 출력
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 삭제 중 오류가 발생했습니다.");
	    }
	}

}
