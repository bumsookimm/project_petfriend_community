<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>로그인</title>
	<jsp:include page="/WEB-INF/views/include_jsp/include_css_js.jsp" />
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script src="/static/js/loginPage.js"></script>
    <link rel="stylesheet" href="/static/css/login/login.css" />
</head>
<body>

	<!-- 회원복구 -->
	<c:if test="${not empty withdraw}">
	    <script>
	        if (confirm("${withdraw}")) {
	            // 폼을 동적으로 생성하여 POST 방식으로 전송
	            const form = document.createElement('form');
	            form.method = 'POST';
	            form.action = '/login/restoration';
	            // 코드 파라미터를 폼에 추가
	            const input = document.createElement('input');
	            input.type = 'hidden';
	            input.name = 'code';
	            input.value = '${member.mem_code}';
	            form.appendChild(input);
	            document.body.appendChild(form);
	            form.submit(); // 폼 전송
	        }
	    </script>
	</c:if>

	<!-- 에러 전달받는게 있을때 표시 -->
	<c:if test="${not empty error}">
        <script>
            alert("${error}");
        </script>
    </c:if>
    
    <!-- 알림 전달받는게 있을때 표시 -->
    <c:if test="${not empty message}">
        <script>
            alert("${message}");
        </script>
    </c:if>
    <!-- 헤더 인클루드 -->
    <jsp:include page="/WEB-INF/views/include_jsp/header.jsp" />
    <!--  -->
    <div id="login">
        <div id="login1">
            <img src="<c:url value='/static/Images/LoginImg/login_text.PNG'/>" id="login_text" alt=""> <br>
            <form action="loginService" method="post">
			    <label for="email">EMAIL</label> <br>
			    <input type="email" id="email" name="email" placeholder="이메일" value="${cookie.email != null ? cookie.email.value : ''}"> <br>
			    
			    <label for="password">PASSWORD</label> <br>
			    <input type="password" id="password" name="password" placeholder="비밀번호"> <br>
			
			    <input type="checkbox" id="remember" name="remember" ${cookie.email != null ? 'checked' : ''}> Remember Me
			    
			    <!-- 이전에 있던 주소 -->
			    <input type="hidden" name="previousUrl" value="${previousUrl }" />
			    
			    <a href="findId">Forgot Password</a>
			    <input type="submit" id="signin" value="Sign In"> <br>
			</form>
            <div id="login1_bottom">
                <h4>아직 펫프렌즈 회원이 아니신가요?
                <a href="/join/joinPage">여기를 눌러 회원가입</a></h4> 
            </div>
        </div>
        <div class="login_slider">
            <div class="login_slides">
                <!-- 각 슬라이드 -->
                <div class="login_slide"><img src="<c:url value='/static/Images/LoginImg/login_slide1.png'/>" alt=""></div>
                <div class="login_slide"><img src="<c:url value='/static/Images/LoginImg/login_slide2.png'/>" alt=""></div>
                <div class="login_slide"><img src="<c:url value='/static/Images/LoginImg/login_slide3.png'/>" alt=""></div>      
            </div>
        </div>
    </div>
</body>
</html>