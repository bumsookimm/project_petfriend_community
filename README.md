# 펫프렌즈 : 모바일 기반의 펫프렌즈를 PC 환경으로 확장
- **블로그**: https://bumdeveloper.tistory.com/

<br>

#### [프로젝트 목표]

- **모바일 전용 플랫폼인 "펫프렌즈"를 PC 환경에 최적화된 형태로 구현하는 것을 목표**
- **모바일에서 제공되던 서비스들이 PC에서도 원활히 동작하며 환경에 맞게 확장하고 개선**
- **사용자에게 더욱 편리하고 풍부한 경험을 제공**

   <br>

#### [프로젝트 개요]

- **인원**: 백엔드 5인
- **기간**: 24. 10. 14 - 24. 11. 21.
- **주요 기술 스택**: ```Java```, ```Spring MVC```, ```Spring Boot```, ```Oracle```, ```MyBatis```, ```WebSocket```, ```Node.js```, ```JavaScript```
- **나의 담당 역할**: COMMUNITY

<br>

### [게시글 작성 및 리스트] 

- **DB 데이터 활용**: 임시저장소 DB에 데이터 저장, 게스글 정보 DB에 저장 (악성코드 태그 제외)
- **백엔드 로직 처리** - 클라이언트에서 모든 데이터를 받아오고 자바스크립트로 페이징 처리하는 방식 -> 서버 측에서 오라클 12버전에서 OFFSET, FETCH NEXT를 활용해서 페이징을 처리하는 방식으로 개선
- **보안 강화 (XSS 방지)** - CKEditor로 입력된 HTML을 그대로 저장할 때 발생할 수 있는 XSS 공격 문제를 Jsoup 라이브러리를 사용하여 악성 <script> 태그 등을 제거하여 HTML을 안전하게 처리
- **임시 저장 기능 추가** - 임시 저장소 테이블 생성 및 서비스 클래스에서 임시 저장 데이터 처리, 5초마다 글 제목과 내용을 서버로 전송하여 임시 저장 하며 글 작성 완료 시 기존 임시 저장 데이터를 삭제

![Image](https://github.com/user-attachments/assets/2f0a0c2f-4613-4054-9bcf-42d2bb43fe30)

<br>

### [이웃 스토리, 인기게시글] 

- **쿼리 최적화**: MyBatis에서 ROWNUM, INTERVAL 을 활용하여 1일 이내의 이웃 스토리를 효율적으로 조회, MyBatis 매핑을 활용하여 ROWNUM을 사용, Oracle 데이터베이스 환경에서 쿼리 성능을 개선하여 상위 4개 인기 게시물 조회
- **캐시 기능**:사용자 닉네임을 키로 사용하여 스토리 데이터 캐시 기능을 추가
- **백엔드 로직 처리** - 프론트엔드에서 처리되던 기본 이미지 설정을 서비스 계층에서 처리하여, 프론트엔드에서 불필요한 조건문을 제거하고 데이터 전송량을 감소

![Image](https://github.com/user-attachments/assets/84af0d8f-d073-484d-b684-4cce70c5abbc)

<br>

### [방문자 관리]

- **Redis 활용**: 외부 캐시 저장소(Redis)를 활용하여 동일 IP 중복 카운트 방지
- **스케줄링 개선**: DB 스케줄러 -> 자바 스케줄링으로 매일 자정 방문자 데이터 자동 초기화, 캐시 키 초기화

![Image](https://github.com/user-attachments/assets/6902d0de-4db7-41e6-b421-4be042f519d1)


<br>

### [활동 로그, 내피드, 이웃]

- **DB 데이터 활용**: 활동 테이블에 활동 관련 데이터 저장 -> 해당 데이터 활동 로그에 노출, 회원가입 시 각자의 고유 내 피드 생성 -> 내 피드 테이블에 데이터 저장 및 활용

![Image](https://github.com/user-attachments/assets/57a39bac-bca8-4f69-86f5-9903bb6d1e1d)


<br>

### [실시간 채팅 1:1]

- **WebSocket 활용**: WebSocket 핸들러를 활용하여 연결 및 메시지 처리, WebSocket을 통해 클라이언트와 실시간 연결을 유지하고 메시지를 전달
- **DB 데이터 활용**: MyBatis + Oracle DB를 사용하여 채팅방, 채팅 메시지를 저장하고 대화 내역을 관리, 실시간으로 메시지를 주고받을 수 있도록 이벤트 기반 메시지 처리

![Image](https://github.com/user-attachments/assets/0bc6c412-6ae3-4baa-864c-128a78c83f45)

<br>

### [전체 채팅]

- **Node.js 활용**: Socket.IO를 활용하여 실시간 전체 채팅 기능 구축
- **DB 데이터 활용**: JavaScript + Node.js 환경에서 채팅 메시지를 DB에 저장하고 대화 내역을 관리

![Image](https://github.com/user-attachments/assets/b0681ed2-7787-4b06-b5a7-7da41b3dcc4b)
