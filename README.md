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


### 3. 여행 정보 조회 및 계획

* 여행지 조회

![Attraction1](https://github.com/SSAFYJJANG/TravelNoTrouble/assets/108526743/95a5193e-c623-4516-a768-5a8351eef00e)

시/도, 구/군, 검색 키워드, 관광지 유형 별로 조 가능합니다.


* 여행 계획 세우기

![Attraction2](https://github.com/SSAFYJJANG/TravelNoTrouble/assets/108526743/9c7c2d92-e6ee-4a13-a26b-283dd43d387b)

저장한 계획은 마이페이지 - 여행 계획 캘린더에서 조회 가능합니다.


### 4. 핫플레이스 리뷰

![Hotplace1](https://github.com/SSAFYJJANG/TravelNoTrouble/assets/108526743/b2ce842e-5356-4152-a00d-0e1a35d9aa6e)


* 리뷰 모달

![Hotplace3](https://github.com/SSAFYJJANG/TravelNoTrouble/assets/108526743/215a93e0-1082-4ae1-bf01-f78290398c8e)


* 리뷰 작성하기

![Hotplace-write](https://github.com/SSAFYJJANG/TravelNoTrouble/assets/108526743/4bf5d30a-881d-4a45-a7e2-1d4eaf299400)


### 5. 게시판

* 게시글 목록

![board1](https://github.com/SSAFYJJANG/TravelNoTrouble/assets/108526743/ab837d6f-2f5d-40e4-b3a5-89b19af43c2f)


* 게시글 조회

![board2](https://github.com/SSAFYJJANG/TravelNoTrouble/assets/108526743/50e1a965-509e-4977-babe-8d925dede23c)


* 게시글 작성 및 수정

![board3](https://github.com/SSAFYJJANG/TravelNoTrouble/assets/108526743/0ef919d3-aada-460e-aa7c-dd5fdfdfcbe6)


### 6. 마이페이지

![Mypage1](https://github.com/SSAFYJJANG/TravelNoTrouble/assets/108526743/8acd356d-3495-44d4-b7a1-db5097db33dc)


* 회원 정보 수정 페이지

![Mypage2](https://github.com/SSAFYJJANG/TravelNoTrouble/assets/108526743/588562da-1869-437f-942c-1b8bc4617e03)


* 여행 계획 캘린더

![Plan1](https://github.com/SSAFYJJANG/TravelNoTrouble/assets/108526743/99454827-a2d6-4c86-9d5d-54dab29589c4)


* 여행 계획 모달

![Plan2](https://github.com/SSAFYJJANG/TravelNoTrouble/assets/108526743/654e3f0f-2b1f-40d8-a84d-c577511196a6)

