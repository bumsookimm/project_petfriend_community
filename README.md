# 펫프렌즈 : 모바일 기반의 펫프렌즈를 PC 환경으로 확장

#### [프로젝트 목표]

- **모바일 전용 플랫폼인 "펫프렌즈"를 PC 환경에 최적화된 형태로 구현하는 것을 목표**
- **모바일에서 제공되던 서비스들이 PC에서도 원활히 동작하며 환경에 맞게 확장하고 개선**
- **사용자에게 더욱 편리하고 풍부한 경험을 제공**

   <br>

#### [프로젝트 개요]

- **인원**: 백엔드 5인
- **기간**: 24. 10. 14 - 24. 11. 21.
- **주요 기술 스택**: ```Java```, ```Spring MVC```, ```Spring Boot```, ```Oracle```, ```MyBatis```, ```WebSocket```, ```Node.js```,```JavaScript```
- **나의 담당 역할**: COMMUNITY

   <br>

### [버스 정류장 주변 시설] 

- **DB 데이터 활용**: 서울 시 버스정류소 위치 DB에 저장 -> 저장된 위치 기반으로 주변 시설 탐색
- **APi 활용**: Google Places API를 활용하여 장소의 이미지, 리뷰 수, 평점을 조회하여 사용자에게 제공,
                Kakao Api를 활용하여 카카오 맵 장소 url을 사용자에게 제공


![Image](https://github.com/user-attachments/assets/5b502a0c-a1f2-4fd4-b839-e98a44511d45)

<br>

### [주변 시설 블로그 검색] 

- **APi 활용**: 네이버 블로그 API에서 블로그 포스트를 검색, 검색어를 URL 인코딩하여 API에 전달하고, JSON 응답을 파싱하여 블로그 포스트 목록 반환


![Image](https://github.com/user-attachments/assets/bfe14014-45a5-44a0-89b1-3268401f9055)

<br>

### [실시간 버스 위치 추적]

- **APi 활용**: 버스 위치 정보 Open API와 연동하여 실시간 버스 정보를 JSON 형식으로 파싱, 버스 정보를 객체로 변환 하여 실시간 버스 위치, 혼잡도, 위치 정보 등을 제공

![Image](https://github.com/user-attachments/assets/bdf6007b-348e-40fc-bf32-91e902f29577)


<br>

### [보행 거리 경로]

- **APi 활용**: 출발지, 도착지 좌표 변환 및 Tmap API 요청, 응답 데이터를 기반으로 Kakao Maps Polyline을 생성하여 경로 시각화 및 시간 및 거리 표시

![Image](https://github.com/user-attachments/assets/49d43d0d-96da-4471-abe7-2e7f49545adf)


<br>

### [시간별 승차 시각화]

- **DB 데이터 활용**: 시간별 승하차 데이터 DB에 저장 -> 저장된 데이터를 기반으로 정류장 승하차 데이터 전달
- **차트 생성 및 업데이트**: DB에서 받은 데이터를 Chart.js를 활용히여 시각화

![Image](https://github.com/user-attachments/assets/7820eaf1-d652-4a3d-b558-904bf616ff6b)
