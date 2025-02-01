<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>myfeed.jsp</title>
<link rel="stylesheet" href="/static/css/community/community_myfeed.css">
<jsp:include page="/WEB-INF/views/include_jsp/include_css_js.jsp" />

<script src="/static/js/community/community_myfeed.js"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>


<script>

	var isLoggedIn = "${sessionScope.loginUser != null ? 'true' : 'false'}";
	var sender = "${sessionScope.loginUser.mem_nick}";
	var receiver = "${myFeedName.mem_nick}";
	var currentRoomId = null;
	var socket;

	

	
	// 페이지 로드 시 채팅방 목록을 불러오기
	   document.addEventListener('DOMContentLoaded', function() {
	       fetchChatRooms(sender);  // 로그인 여부와 상관없이 채팅방 목록을 불러옴
	       document.getElementById("chatContainer").style.display = "none"; // 채팅 창 숨기기
	   });

	   // 메시지 버튼 클릭 시 채팅창 열기
	   function openMessageBox() {
	      
	        if (isLoggedIn === "false") { // 'true' 또는 'false' 문자열로 비교
	            alert("로그인이 필요합니다.");
	            return;
		  }
		   
		   
		   document.getElementById("chatContainer").style.display = "flex"; // 채팅창 표시
	       var roomId = generateRoomId(sender, receiver);
	       openChat(roomId, receiver); // 새로운 채팅방 열기
	   }

	   // 채팅방 ID를 생성하는 함수
	   function generateRoomId(sender, receiver) {
	       return sender < receiver ? sender + ',' + receiver : receiver + ',' + sender;
	   }

	// 채팅 창을 여는 함수
	   function openChat(roomId, receiver) {
	       document.getElementById("chatBox").style.display = "block"; // 채팅창 본문 표시
	       if (socket) {
	           socket.close(); // 기존 소켓 연결을 닫음
	       }
	       currentRoomId = roomId;  // 현재 채팅방 ID 저장
	      	 socket = new WebSocket("ws://localhost:9002/ws/chat/" + roomId); // WebSocket 연결
	       //socket = new WebSocket("ws://25.0.125.242:9002/ws/chat/" + currentRoomId); // WebSocket 연결
	       //socket = new WebSocket("ws://25.2.226.173:9002/ws/chat/" + currentRoomId); // WebSocket 연결
			console.log("socket",socket);
	   
	       // 메시지를 WebSocket으로 받은 후
	       socket.onmessage = function(event) {
	           var chatMessages = document.getElementById("chatMessages");
	           var messageData = JSON.parse(event.data);
	           var newMessage = document.createElement("div");

	           // 메시지가 보낸 사람인지 받는 사람인지 확인하여 클래스 추가
	           if (messageData.sender === sender) {
	               newMessage.classList.add('message', 'sender'); // 내 메시지
	           } else {
	               newMessage.classList.add('message', 'receiver'); // 상대방 메시지
	           }

	           newMessage.innerText = messageData.sender + " : " + messageData.message_content;
	           chatMessages.appendChild(newMessage);

	           // 새로운 메시지가 추가된 후 스크롤을 맨 아래로
	           chatMessages.scrollTop = chatMessages.scrollHeight;
	       };

	       loadChatHistory(roomId);  // 채팅 히스토리 불러오기

	       // 채팅방 목록을 다시 로드하여 활성화된 채팅방을 표시
	       fetchChatRooms(sender);
	   }

	   // 메시지를 전송하는 함수
	   function sendMessage(event) {
	       if (event.key === "Enter" && socket && currentRoomId) {
	           var message = document.getElementById("chatInput").value;
	           socket.send(JSON.stringify({
	               sender: sender,
	               receiver: receiver,
	               message_content: message,
	               roomId: currentRoomId
	           }));
	         	console.log("currentRoomId",currentRoomId);
	           document.getElementById("chatInput").value = "";
	       }
	   }

	    // 전송 버튼 클릭 시 메시지 전송
	    function sendMessageButton() {
	        var message = document.getElementById("chatInput").value;
	        if (socket && currentRoomId && message) {
	            socket.send(JSON.stringify({
	                sender: sender,
	                receiver: receiver,
	                message_content: message,
	                roomId: currentRoomId
	            }));
	        	console.log("currentRoomId",currentRoomId);
	            document.getElementById("chatInput").value = ""; // 입력창 초기화
	        }
	    }
	   
	   
	   // 채팅 창을 닫는 함수
	   function closeChat() {
	       document.getElementById("chatContainer").style.display = "none";  // 채팅 창 숨기기
	       document.getElementById("chatBox").style.display = "none";  // 채팅창 본문 숨기기
	       if (socket) {
	           socket.close();
	       }
	   }

	   // 채팅 히스토리를 불러오는 함수
	   function loadChatHistory(roomId) {
    $.ajax({
        url: '/community/getChatHistory',
        type: 'GET',
        data: { roomId: roomId },
        success: function(messages) {
            var chatMessages = document.getElementById("chatMessages");
            chatMessages.innerHTML = ""; // 기존 메시지 초기화
        	console.log("roomId",roomId);
           
            messages.forEach(function(message) {
                var newMessage = document.createElement("div");

                // 메시지가 보낸 사람인지 받는 사람인지 확인하여 클래스 추가
                if (message.sender === sender) {
                    newMessage.classList.add('message', 'sender'); // 내 메시지
                } else {
                    newMessage.classList.add('message', 'receiver'); // 상대방 메시지
                }

                newMessage.innerText = message.sender + " : " + message.message_content;
                chatMessages.appendChild(newMessage);
            });
        
            // 히스토리가 로드된 후 스크롤을 맨 아래로
            chatMessages.scrollTop = chatMessages.scrollHeight;
        }
    });
}

	   // 채팅방 목록을 불러오는 함수
	function fetchChatRooms(sender) {
    fetch(`/community/getChatRooms?sender=\${sender}&receiver=\${receiver}`)
        .then(response => response.json())
        .then(data => {
            var chatRoomsList = document.getElementById("chatRoomsList");
            chatRoomsList.innerHTML = '';  // 기존 채팅방 목록 초기화

            console.log("sender", sender);
            console.log("receiver", receiver);
            
            data.forEach(chatRoom => {
                var roomLink = document.createElement("a");
                roomLink.innerText = chatRoom.room_id;

                // 현재 채팅방이 열려있는 채팅방과 일치하면 active 클래스를 추가
                if (chatRoom.room_id === currentRoomId) {
                    roomLink.classList.add('active');  // active 클래스를 추가하여 표시
                }

                roomLink.onclick = function() {
                    openChat(chatRoom.room_id, chatRoom.receiver);  // 클릭 시 채팅방 열기
                    document.getElementById("chatContainer").style.display = "flex"; // 채팅창 표시
              		console.log("chatRoom.room_id",chatRoom.room_id);
              	
                };

                var roomItem = document.createElement("div");
                roomItem.appendChild(roomLink);
                chatRoomsList.appendChild(roomItem);
            });
        })
        .catch(error => console.error('Error fetching chat rooms:', error));
}

	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
</script>

<body>
	<jsp:include page="/WEB-INF/views/include_jsp/header.jsp" />

	<!-- 이웃 목록 모달 -->
	<div id="neighborListModal" class="modal">
		<div class="modal-content">
			<span class="close-btn" onclick="closeNeighborListModal()">&times;</span>
			<div id="neighborListContainer"></div>
		</div>
	</div>


	<!-- 내 이웃 목록 모달 -->
	<div id="myNeighborListModal" class="modal">
		<div class="modal-content">
			<span class="close-btn" onclick="closeMyNeighborListModal()">&times;</span>
			<div id="MyneighborListContainer"></div>
		</div>
	</div>




	<!-- 마이피드 이미지 -->
	<div class="feed-image">
		<c:choose>
			<c:when test="${empty getMyfeedVisit.myfeed_img}">
				<img
					src="<c:url value='/static/Images/communityorign_img/myfeed.jpg'/>"
					alt="Event 1" class="feed-main-image">
			</c:when>
			<c:otherwise>
				<img
					src="<c:url value='/static/Images/communityorign_img/${getMyfeedVisit.myfeed_img}'/>"
					alt="Event 1" class="feed-main-image">
			</c:otherwise>
		</c:choose>
	</div>




	<!-- 상단 프로필 영역 -->
	<div class="profile-section">

		<div class="visit-stats">
			<span>전체: ${getMyfeedVisit.total_visits}</span> <span>오늘:
				${getMyfeedVisit.daily_visits}</span>

			<!-- 수정 버튼을 표시할지 여부 결정 -->

			<!-- 이미지 수정 폼 -->
			<c:if
				test="${getMyfeedVisit.mem_code == sessionScope.loginUser.mem_code}">
				<form action="/community/upload/${getMyfeedVisit.mem_code}"
					method="post" enctype="multipart/form-data" class="upload-form">
					<label for="feedImage" class="custom-file-button">파일 선택</label> <input
						type="file" id="feedImage" name="feedImage" class="file-input">
					<button type="submit" class="upload-button">업로드</button>
				</form>
			</c:if>



		</div>


		<div class="profile-info">
			<c:choose>
				<c:when test="${empty getpetimg.pet_img}">
					<img src="/static/Images/pet/noPetImg.jpg" alt="프로필 이미지"
						class="main-profile-image">
				</c:when>
				<c:otherwise>
					<img src="/static/Images/pet/${getpetimg.pet_img}" alt="프로필 이미지"
						class="main-profile-image">
				</c:otherwise>
			</c:choose>
			<h2>${myFeedName.mem_nick}</h2>
		</div>


		<div class="profile-menu">

			<c:if
				test="${myFeedName.mem_nick eq sessionScope.loginUser.mem_nick}">
				<a href="">내 피드</a>
			</c:if>

			<c:if
				test="${isFriendBool == 1 && myFeedName.mem_nick ne sessionScope.loginUser.mem_nick}">
				<a
					href="<c:url value='/community/addFriend/${mem_code}'/>?mem_nick=${myFeedName.mem_nick}"
					onclick="return checkDelFriend('${sessionScope.loginUser}');">친구
					삭제</a>
			</c:if>

			<c:if
				test="${(isFriendBool == 0 || isFriendBool == null) && myFeedName.mem_nick ne sessionScope.loginUser.mem_nick}">
				<a
					href="<c:url value='/community/addFriend/${mem_code}'/>?mem_nick=${myFeedName.mem_nick}"
					onclick="return checkAddFriend('${sessionScope.loginUser}');">+
					친구 추가</a>
				<!-- 친구가 아닐 때 -->
			</c:if>

			<a href="javascript:void(0);"
				onclick="openMessageBox(); return false;">메시지</a>

			<!-- 이웃 목록 버튼 -->
			<a href="javascript:void(0);"
				onclick="fetchNeighborList('${mem_code}', '${myFeedName.mem_nick}'); return false;">이웃
				목록</a>
		</div>
	</div>





	<!-- 채팅창 전체 컨테이너 (기본적으로 숨겨짐) -->
	<div id="chatContainer" class="chat-container"
		style="position: fixed; top: 50%; left: 50%; transform: translate(-50%, -50%); display: flex; width: 60%; height: 70%; border: 1px solid #ccc; border-radius: 10px; background-color: white; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); z-index: 9999;">

		<!-- 왼쪽: 채팅방 목록 -->
		<div id="chatRooms" class="chat-rooms"
			style="flex: 1; padding: 10px; border-right: 1px solid #ccc; overflow-y: auto; height: 100%; box-sizing: border-box;">
			<h3 class="chat-title" style="text-align: center;">채팅방 목록</h3>

			<!-- 스크롤 추가된 채팅방 목록 표시 영역 -->
			<div id="chatRoomsList"
				style="max-height: calc(100% - 40px); overflow-y: auto;"></div>
		</div>

		<!-- 오른쪽: 채팅창 -->
		<div id="chatBox" class="chat-box"
			style="flex: 2; display: flex; flex-direction: column; padding: 10px; height: 100%; box-sizing: border-box; position: relative;">
			<h3 class="chat-title" style="text-align: center;">채팅창</h3>

			<!-- 채팅 메시지 영역에 스크롤 추가 -->
			<div id="chatMessages" class="chat-messages"
				style="flex: 1; overflow-y: auto; margin-bottom: 0; max-height: calc(100% - 90px);"></div>

			<!-- 채팅 입력창과 전송 버튼을 동일 선상에 배치 -->
			<div
				style="display: flex; align-items: center; position: absolute; bottom: 5px; width: calc(100% - 20px); left: 10px;">
				<!-- 메시지 입력창 -->
				<input type="text" id="chatInput" class="chat-input"
					placeholder="메시지 입력..." onkeypress="sendMessage(event)"
					style="padding: 10px; width: 85%; border: 1px solid #ccc; border-radius: 5px; box-sizing: border-box;">

				<!-- 전송 버튼 -->
				<button onclick="sendMessageButton()" class="send-btn"
					style="padding: 10px 15px; margin-left: 10px; border: none; background-color: #ff4081; color: white; font-size: 14px; border-radius: 5px; cursor: pointer;">
					전송</button>
			</div>

			<!-- 오른쪽 상단에 닫기 버튼 (X 표시) -->
			<button onclick="closeChat()" class="chat-close-btn"
				style="position: absolute; top: 10px; right: 10px; padding: 5px 10px; font-size: 18px; background-color: transparent; border: none; color: #f44336; cursor: pointer; font-weight: bold;">&times;</button>

		</div>

	</div>






	<!-- 컨테이너 시작 -->
	<div class="container">
		<!-- 메인 영역 -->
		<div class="my_feed">
			<div class="profile"></div>
			<br>

			<div class="feed-images">
				<c:forEach items="${myFeedList}" var="myFeedList">
					<div class="feed-item">
						<!-- 이미지와 정보를 감싸는 div 추가 -->
						<a
							href="<c:url value='/community/contentView/?board_no=${myFeedList.board_no}'/>">
							<!-- 이미지 클릭 시 이동할 링크 추가 --> <c:choose>
								<c:when test="${not empty myFeedList.chrepfile}">
									<img src="/static/images/community_img/${myFeedList.chrepfile}"
										alt="피드 이미지" style="width: 200px; height: 200px;">
								</c:when>
								<c:otherwise>
									<img src="/static/Images/pet/noPetImg.jpg" alt="기본 이미지"
										style="width: 200px; height: 200px;">
								</c:otherwise>
							</c:choose>
						</a>
						<div class="feed-info">
							<!-- 제목과 날짜를 감싸는 div 추가 -->
							<a
								href="<c:url value='/community/contentView/?board_no=${myFeedList.board_no}'/>"
								class="post-title">${myFeedList.board_title}</a>
							<!-- 제목 클릭 시 이동할 링크 추가 -->
							<span class="post-time">${fn:substring(myFeedList.board_created, 0, 10)}</span>
						</div>
					</div>
				</c:forEach>
			</div>
		</div>

		<!-- 메인 구성 끝 -->

		<!-- 사이드바 -->
		<div class="sidebar">
			<div class="ad-banner">
				<a href="http://localhost:9002/notice/eventView?id=49"> <img
					src="/static/Images/thumbnail/페스룸포토리뷰썸네일.gif" alt="광고 배너" />
				</a>
			</div>

			<div class="post-header">
				<div class="profile-info">

					<c:if test="${sessionScope.loginUser ne null}">
						<img
							src="<c:choose>
				                <c:when test="${empty logingetpetimg.pet_img}">
				                    /static/Images/pet/noPetImg.jpg
				                </c:when>
				                <c:otherwise>
				                    /static/Images/pet/${logingetpetimg.pet_img}
				                </c:otherwise>
				              </c:choose>"
							alt="Profile Image" class="profile-image">
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
					<a href="#"
						onclick="fetchMyNeighborList('${mem_code}'); return false;">내
						이웃 목록</a>
				</c:if>



			</ul>
			<div class="sidebar-notice">
				<h3>소식상자</h3>
				<p class="notice-text">내 활동을 눌러보세요!</p>
			</div>


			<div class="ad-banner">
				<a href="http://localhost:9002/notice/eventView?id=50"> <img
					src="/static/Images/thumbnail/페스룸카카오친추썸네일.gif" alt="광고 배너" />
				</a>
			</div>
		</div>
	</div>
	<!-- 컨테이너 끝 -->
</body>
<jsp:include page="/WEB-INF/views/include_jsp/footer.jsp" />
</html>