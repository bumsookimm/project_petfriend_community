<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>어드민 헬프펫프 펫호텔</title>
<jsp:include page="/WEB-INF/views/include_jsp/include_css_js.jsp" />
<link rel="stylesheet" href="/static/css/admin/pethotel_reserve.css" />
</head>
<body>
	<div class="title">
		<h3>펫호텔 예약 관리</h3>
	</div>
	<div id="tab1">
		<div id="pethotelRegister">
			<div class="filter-section" id="reserve-filter">
				<div class="radio-div">
					<div class="filter-title">예약 상태</div>
					<label><input class="radio-group" type="radio" name="reserve-type-filter" value="전체" checked> 전체</label>
					<label><input class="radio-group" type="radio" name="reserve-type-filter" value="신청중"> 신청중</label>
					<label><input class="radio-group" type="radio" name="reserve-type-filter" value="승인"> 승인</label>
					<label><input class="radio-group" type="radio" name="reserve-type-filter" value="거절"> 거절</label>
					<label><input class="radio-group" type="radio" name="reserve-type-filter" value="취소됨"> 취소됨</label>
					<button id="filterReset" class="btn-style">필터 초기화</button>
				</div>
			</div>
			<div class="filter-section">
				<div class="date-group">
					<div class="filter-title">조회기간</div>
					<label><input type="date" id="start-date">부터</label> 
					<label><input type="date" id="end-date">까지</label>
				</div>
			</div>
			<div class="filter-section">
				<div class="search-group">
					<div class="filter-title">검색</div>
					<label>회원코드<input type="text" id="search-mem-code"></label>
					<label>예약코드<input type="text" id="search-reserve-code"></label>
				</div>
			</div>
		</div>

		<!-- 리스트 영역 -->
		<div class="pethotel-reserve-list-container">
			<table class="pethotel-reserve-list">
				<thead class="thead">
					<tr>
						<th>예약코드</th>
						<th>닉네임</th>
						<th>마리 수</th>
						<th>예약 시작일</th>
						<th>예약 종료일</th>
						<th>예약 상태</th>
						<th>예약 승인일</th>
						<th>예약 거절 사유</th>
						<th>상세 예약정보 조회</th>
					</tr>
				</thead>
				<tbody id="pethotel-reserve-table-body">
					<!-- 예약 리스트 -->
				</tbody>
			</table>
			<br /><br />
			<br />
			<div id="pagination">
				<!-- 페이징 -->
			</div>
		</div>
	</div>

	<div id="reserveDetailDiv" style="display: none;">
		<div id="reserveDetailButtons" class="title">
			<button id="goBack" class="btn-style">목록으로</button>
			<label><input type="radio" name="reserve_status_set" value="신청중" /> 신청중</label>
			<label><input type="radio" name="reserve_status_set" value="승인" /> 승인</label>
			<label><input type="radio" name="reserve_status_set" value="거절" /> 거절</label>
			<label><input type="radio" name="reserve_status_set" value="취소됨" disabled /> 취소됨</label>
			<button id="reserveSubmit" class="btn-style">예약 상태 변경</button>
		</div>
		<div id="reserveDetailMemTable">
			<!-- 상세정보의 멤버 -->
			<table border="1" width="100%" style="text-align: center;">
				<thead class="thead">
					<tr>
						<th>예약 코드</th>
						<th>멤버 코드</th>
						<th>닉네임</th>
						<th>마리 수</th>
						<th>예약 시작일</th>
						<th>예약 종료일</th>
						<th>예약 상태</th>
						<th>예약 승인일</th>
						<th>예약 거절 사유</th>
					</tr>
				</thead>
				<tbody id="reserveDetailMem">
				
				</tbody>
			</table>
		</div>
		<div id="reserveDetailListTable">
			<!-- 상세정보의 펫 목록 -->
			<table width="100%" id="reserveDetailList">
				
			</table>
		</div>
	</div>
	
	<div id="reasonModal" class="modal" style="display: none;">
		 <div class="modal-content">
		 <span class="close-btn"><i class="fa-solid fa-xmark"></i></span>
		 	<div class="input-group">
	            <label for="refusal_reason">거절 사유</label>
	            <textarea name="refusal_reason" id="refusal_reason" cols="30" rows="10" placeholder="거절 사유를 적어주세요."></textarea>
	            <button id="reserveSubmit_refusal" class="btn-style">예약 상태 변경</button>
	        </div>
		 </div>
	</div>
	
	<script src="/static/js/admin/pethotel_reserve.js"></script>
</body>
</html>