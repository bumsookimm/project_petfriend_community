<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="/static/js/community/community_main.js"></script>
<link rel="stylesheet" href="/static/css/community/community_main.css">
<jsp:include page="/WEB-INF/views/include_jsp/include_css_js.jsp" />
</head>

<body>


	<!-- 내 이웃 목록 모달 -->
	<div id="myNeighborListModal" class="modal">
		<div class="modal-content">
			<span class="close-btn" onclick="closeMyNeighborListModal()">&times;</span>
			<div id="MyneighborListContainer"></div>
		</div>
	</div>





	<main>
		<div class="container">
			<jsp:include page="/WEB-INF/views/include_jsp/header.jsp" />

			<!-- 핫토픽 섹션 -->
			<section class="hot-topics">
				<h3>펫프렌즈 핫이슈!</h3>
				<hr>
				<ul>
					<c:forEach var="hottopic" items="${getHotTopicList}">
					    <li><a href="/community/contentView?board_no=${hottopic.board_no}">
					        <div class="image-container">
					            <img src="/static/images/community_img/${hottopic.chrepfile}" alt="핫토픽 이미지" />
					            <div class="overlay">
					                <p>${hottopic.board_title}</p>
					            </div>
					        </div>
					    </a></li>
					</c:forEach>
				</ul>
			</section>






			<!-- 펫프렌즈는 지금 뭐할까? -->
			<div class="container-box">
				<div class="container-header">
					<span class="header-text">펫프렌즈는 지금 뭐할까?</span>
					<form class="search-form" action="/community/main" method="GET">
						<input type="text" name="query" placeholder="검색어를 입력하세요"
							class="search-input">
						<button type="submit" class="search-button">🔍</button>
					</form>
				</div>

				<div class="story-container">
					<c:choose>
						<c:when test="${sessionScope.loginUser != null}">
							<!-- 로그인 상태일 때: 게시글 리스트를 동적으로 출력 -->
							<c:forEach var="storyList" items="${storyList}">
								<div class="story-item">
									<a href="/community/myfeed/${storyList.mem_code}"> <img
										src="/static/Images/pet/${storyList.pet_img}" alt="스토리 이미지"
										class="story-image" />
										<p>${storyList.user_id}</p>
									</a>
								</div>
							</c:forEach>

							<!-- 게시글 리스트가 비어있을 경우 -->
							<c:if test="${empty storyList}">
								<div class="logout-message">
									<p>이웃의 새글이 없습니다.</p>
								</div>
							</c:if>


						</c:when>

						<c:otherwise>
							<!-- 로그아웃 상태일 때: 안내 메시지 출력 -->
							<div class="logout-message">
								<p>
									로그아웃 상태입니다.<br>로그인하여 이웃 새글을 확인해보세요.
								</p>
							</div>
						</c:otherwise>
					</c:choose>
				</div>
			</div>

			<!-- 카테고리 섹션 -->
			<section class="categories">

				<ul>

					<li><a href="#" class="category-button" data-cate-no="0">
							<img src="/static/Images/communityorign_img/category0.png" alt="" />
							<p>전체</p>
					</a></li>

					<li><a href="#" class="category-button" data-cate-no="1">
							<img src="/static/Images/communityorign_img/category1.png" alt="" />
							<p>육아꿀팁</p>
					</a></li>
					<li><a href="#" class="category-button" data-cate-no="2">
							<img src="/static/Images/communityorign_img/category2.png" alt="" />
							<p>내새꾸자랑</p>
					</a></li>
					<li><a href="#" class="category-button" data-cate-no="3">
							<img src="/static/Images/communityorign_img/category3.png" alt="" />
							<p>펫테리어</p>
					</a></li>
					<li><a href="#" class="category-button" data-cate-no="4">
							<img src="/static/Images/communityorign_img/category4.png" alt="" />
							<p>펫션쇼</p>
					</a></li>
					<li><a href="#" class="category-button" data-cate-no="5">
							<img src="/static/Images/communityorign_img/category5.png" alt="" />
							<p>집사일기</p>
					</a></li>
					<li><a href="#" class="category-button" data-cate-no="6">
							<img src="/static/Images/communityorign_img/category6.png" alt="" />
							<p>육아질문</p>
					</a></li>
					<li><a href="#" class="category-button" data-cate-no="7">
							<img src="/static/Images/communityorign_img/category7.png" alt="" />
							<p>수의사상담</p>
					</a></li>

				</ul>



				<div id="postContainer">
					<jsp:include page="postList.jsp" />
				</div>


			</section>
		</div>
		<!-- 사이드바 -->


		<div class="sidebar">
			<div class="ad-banner">
				<a href="http://localhost:9002/notice/eventView?id=49&active=N">
					<img src="/static/Images/thumbnail/페스룸포토리뷰썸네일.gif" alt="광고 배너" />
				</a>
			</div>

			<div class="post-header">
				<div class="profile-info">

					<c:if test="${sessionScope.loginUser ne null}">
						<c:choose>
							<c:when test="${empty getpetimg.pet_img}">
								<img src="/static/Images/pet/noPetImg.jpg" alt="프로필 이미지"
									class="profile-image">
							</c:when>
							<c:otherwise>
								<img src="/static/Images/pet/${getpetimg.pet_img}" alt="프로필 이미지"
									class="profile-image">
							</c:otherwise>
						</c:choose>
						<span class="user-name">${sessionScope.loginUser.mem_nick}</span>
						<a href="/mypage/logout" class="logout-button">로그아웃</a>
					</c:if>

					<c:if test="${sessionScope.loginUser eq null}">
						<a href="/login/loginPage" class="login-button">로그인</a>
					</c:if>

				</div>
			</div>




			<ul class="sidebar-menu">


				<c:if test="${sessionScope.loginUser ne null}">
					<li><a
						href="/community/myfeed/${sessionScope.loginUser.mem_code}">내
							피드</a></li>

					<li><a href="/community/writeView">글쓰기</a></li>

					<li><a href="javascript:void(0);"
						onclick="fetchUserActivity()">내 소식</a></li>
					<li><a href="javascript:void(0);" onclick="fetchMyActivity()">내
							활동</a></li>
					<a href="javascript:void(0);" onclick="fetchMyNeighborList()">내
						이웃 목록</a>
			</ul>
			<div class="sidebar-notice">
				<h3>소식상자</h3>
				<p class="notice-text">내 소식을 눌러보세요!</p>
			</div>



			</c:if>

			<div class="ad-banner">
				<a href="http://localhost:9002/notice/eventView?id=50&active=N">
					<img src="/static/Images/thumbnail/페스룸카카오친추썸네일.gif" alt="광고 배너" />
				</a>
			</div>
		</div>


	</main>






</body>
<jsp:include page="/WEB-INF/views/include_jsp/footer.jsp" />
</html>