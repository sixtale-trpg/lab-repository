# SIXTALE
TRPG(Table-top Role Playing Game)를 온라인 상에서 즐길 수 있는 ORPG(Online Role Playing Game) 플랫폼 입니다. 룰 챗봇을 이용하여 GM은 번거롭지 않게 룰에 대한 정보를 습득하고 플레이어들에게 다이스, 스탯, 맵 등을 세팅할 수 있습니다. 플레이어들은 세팅된 값으로 게임을 진행할 수 있으며 플레이에만 집중할 수 있습니다.

## 개요
> 팀원 | 신건우, 강동완, 송근웅, 남경민, 안치숙, 정수현<br>
> 개발 | Front-End (Vue3), Back-End (Spring Boot, JPA, QueryDSL), Infra(Jenkins, Docker)<br>
> 이용 API | ChatPDF, DALL·E

## 유스케이스 다이어그램

### 1. 기본 관리
![image](https://github.com/SSAFYJJANG/TravelNoTrouble/assets/108526743/a09dcb27-fb96-45f4-b9d8-3ed429a3a196)

### 2. 여행 관리
![image](https://github.com/SSAFYJJANG/TravelNoTrouble/assets/108526743/0eb9a567-92e9-4660-8616-f10a4d057a77)

### 3. 관광지 검색 화면 관리
![image](https://github.com/SSAFYJJANG/TravelNoTrouble/assets/108526743/85f1f4ff-c4e6-45c5-bc3c-8e151caae3dd)

### 4. 핫플레이스 화면 관리
![image](https://github.com/SSAFYJJANG/TravelNoTrouble/assets/108526743/cf2e5387-d018-4c5a-8d27-33e59ce2f1da)

### 5. 메인 화면 관리
![image](https://github.com/SSAFYJJANG/TravelNoTrouble/assets/108526743/4dc8982a-1eb6-41f8-a0ca-24ea53dcb465)

## 테이블 구조도

![image](https://github.com/SSAFYJJANG/TravelNoTrouble/assets/108526743/51a0e120-d69d-4e84-b183-b7c499e4da27)

## 클래스 다이어그램

### 1. 회원
![image](https://github.com/SSAFYJJANG/TravelNoTrouble/assets/108526743/4867a81c-e852-4764-baa2-267c5bb0d4c4)

### 2. 게시판
![image](https://github.com/SSAFYJJANG/TravelNoTrouble/assets/108526743/971ae166-70e9-47fa-b9cc-c659964e562b)

### 3. 핫플레이스
![image](https://github.com/SSAFYJJANG/TravelNoTrouble/assets/108526743/2baa1ca8-4509-447c-9ce8-f476de14f324)

### 4. 여행 계획
![image](https://github.com/SSAFYJJANG/TravelNoTrouble/assets/108526743/578dd06a-7f44-44b5-a698-d5c1a9ce68de)

### 5. 여행지
![image](https://github.com/SSAFYJJANG/TravelNoTrouble/assets/108526743/3851a4cb-2fae-46a2-841c-042ecbfdd5e2)

## 구현 기능

### 1. 회원 인증

* 로그인
  
![Auth-Login](https://github.com/SSAFYJJANG/TravelNoTrouble/assets/108526743/8e270626-9946-4150-b507-421e49c29265)


* 회원가입
  
![Auth-Signup](https://github.com/SSAFYJJANG/TravelNoTrouble/assets/108526743/c5f874b2-788b-4168-83c4-db2cca8ff168)


* 비밀번호 찾기
  
![Auth-Password](https://github.com/SSAFYJJANG/TravelNoTrouble/assets/108526743/01babb18-bdac-4d28-a8d2-1c40f4e01b9b)


### 2. 홈 화면

![Main1](https://github.com/SSAFYJJANG/TravelNoTrouble/assets/108526743/1e5e4b64-8c9b-4cea-adbf-5ee4b6e21fbf)


* 날씨 정보

![Main2](https://github.com/SSAFYJJANG/TravelNoTrouble/assets/108526743/ddff32d5-e3f3-4505-a950-6d9e2584ae3a)


* 추천 여행지

![Main3](https://github.com/SSAFYJJANG/TravelNoTrouble/assets/108526743/54a2b0b3-4a42-4235-9660-7a5036d8d5af)


* 지역별 여행지

![Main4](https://github.com/SSAFYJJANG/TravelNoTrouble/assets/108526743/a9e950c1-32ea-4272-b2f4-3821ff289486)

클릭하면 지역별 관광지 조회 페이지로 이동합니다.


* 인기 리뷰

![Main5](https://github.com/SSAFYJJANG/TravelNoTrouble/assets/108526743/4ee08048-8ebd-4fb8-a836-cf85371fec0d)

좋아요 상위 15개의 피드를 보여줍니다.


### 3. 로비


![sixtale-lobby](/uploads/fa992c0a602cba08e41ee2a87e11dd98/sixtale-lobby.png)


* 방 생성 모달

![sixtale-lobby](/uploads/fa992c0a602cba08e41ee2a87e11dd98/sixtale-lobby.png)


### 4. 게임 대기 방

![sixtale-wait](/uploads/435f1aa5c13b7f7524aa73921dd8977e/sixtale-wait.png)


### 5. 캐릭터 시트

![sixtale-sheet](/uploads/8ee66a85380b9f7ebdff2b1acd66e02b/sixtale-sheet.png)


### 6. 인게임 플레이

![sixtale-ingame](/uploads/bfdd451862b7dbd8c28a06d710d15b29/sixtale-ingame.png)

### 7. 룰북 페이지

### 8. 시나리오 목록 페이지

### 9. 마이페이지
