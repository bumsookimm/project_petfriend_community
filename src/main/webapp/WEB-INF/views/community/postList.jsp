<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>postList</title>
<link rel="stylesheet" href="/static/css/community/community_main.css">
</head>

<script>

$(document).ready(function() {
    // 페이지 번호 클릭 시
    $(".pagination-link").click(function(e) {
        e.preventDefault();
        var page = $(this).data("page");

        $.ajax({
            url: "/community/main?page=" + page,
            method: "GET",
            success: function(response) {
                $("#postContainer").html($(response).find("#postContainer").html());
                updatePagination(response);
            }
        });
    });

    // 페이지네이션 업데이트
    function updatePagination(response) {
        var totalPages = $(response).find("#totalPages").val();
        var currentPage = $(response).find("#currentPage").val();
        var paginationHtml = '';

        for (var i = 1; i <= totalPages; i++) {
            paginationHtml += '<a href="#" class="pagination-link" data-page="' + i + '">' + i + '</a>';
        }

        $(".pagination").html(paginationHtml);
    }
});



</script>



<body>
	<input type="hidden" id="totalPages" value="${totalPages}">
	<input type="hidden" id="currentPage" value="${currentPage}">
	
	<div id="postContainer">

		<c:forEach items="${postList }" var="post">
			<article class="post-card">
				<div class="post-header">
					<input type="hidden" name="mem_code" value="${post.mem_code}">


					<a href="/community/myfeed/${post.mem_code}" class="profile-link">
				<div class="profile-info">
					<img src="/static/Images/pet/${post.pet_img}" alt="프로필 이미지 1"
					class="profile-image" /> 
					<span class="user-name">${post.user_id }</span></a>
					<span class="post-time">${post.board_created}</span>
				</div>

				<h2 class="post-title">
					<a href="/community/contentView?board_no=${post.board_no}">${post.board_title} </a>
				</h2>
				<div class="post-content">
					<a href="/community/contentView?board_no=${post.board_no}">${post.board_content_input}</a>
				</div>
	</div>
	
		<a href="/community/myfeed/${post.mem_code}" class="profile-link">
		    <c:choose>
		        <c:when test="${not empty post.chrepfile}">
		            <img
		                src="${pageContext.request.contextPath}/static/images/community_img/${post.chrepfile}"
		                alt="포스트 1 이미지" class="post-image" />
		        </c:when>
		        <c:otherwise>
		            <img
		                src="/static/Images/pet/noPetImg.jpg"
		                alt="기본 이미지" class="post-image" />
		        </c:otherwise>
		    </c:choose>
		</a>


<div class="post-footer">
    <span class="like-button">❤️ ${post.board_likes}</span>
    <span class="comment-button">💬 ${post.board_comment_count}</span>
    <div class="button-container">
        <c:if test="${sessionScope.loginUser ne null}">
            <c:if test="${sessionScope.loginUser.mem_code eq post.mem_code}">
                <!-- 수정 버튼 -->
                <a href="/community/modifyView?board_no=${post.board_no}" class="edit-button">수정</a>

                <!-- 삭제 버튼 -->
                <form action="/community/delete" method="post" class="delete-form" onsubmit="return confirm('정말 삭제하시겠습니까?')">
                    <input type="hidden" name="board_no" value="${post.board_no}">
                    <button type="submit" class="delete-button">삭제</button>
                </form>
            </c:if>
        </c:if>
    </div>
</div>
	
	
	
	</article>
	</c:forEach>
	
	<div class="pagination">

   
    <c:forEach var="i" begin="1" end="${totalPages}">
        <a href="#" class="pagination-link" data-page="${i}">${i}</a>
    </c:forEach>
	</div>


</body>
</html>