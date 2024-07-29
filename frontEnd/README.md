# ssafy-vue

## Project setup
```
npm install
```

### Compiles and hot-reloads for development
```
서버 실행 명령어
npm run serve
```

### Compiles and minifies for production
```
npm run build
```

### Lints and fixes files
```
npm run lint
```

### Customize configuration
See [Configuration Reference](https://cli.vuejs.org/config/).


### 부트스트랩 설치
부트스트랩 cdn을 public폴더의 index.html에 삽입
npm install bootstrap bootstrap-vue-3

###
배포된 기본 스켈레톤 코드에서 틀을 가져옮

###
src의 images 폴더 안에 종류별 필요한 이미지 넣어놓음 (carousel, information, ingame, room 등)
필요한 이미지도 있고 더미데이터도 있어서 나중에 백엔드에서 받아오면 이미지 조정필요함

###
큰 페이지마다 component로 섹션 영역별로 쪼개서 관리하는 식
views 폴더안에 home, main, menu 이런식으로 / 다만 games는 큰 틀로 잡고 games의 components폴더 안에 waiting(대기방), ingame(인게임), charactersheet(캐릭터 시트), Modal(모달창 종류별)
로 나눠서 관리 중 

### 
경로를 뿌리는 router는 src폴더의 common 폴더의 lib 폴더의 vue-router.js 파일에 존재