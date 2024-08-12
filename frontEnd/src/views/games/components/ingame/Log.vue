<template>
    <div class="log-section-container" :style="backgroundStyle">
      <div class="tabs">
        <button class="tab" :class="{ active: activeTab === 'allLogs' }" @click="selectTab('allLogs')">
          전체 로그 기록
        </button>
      </div>
      <div class="log-content">
        <div v-if="activeTab === 'allLogs'">
          전체 로그 기록
          <!-- 이곳에 로그 내용이 표시됨 -->
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  // 로그로직 추가해야한다 ! //


  import { ref } from 'vue';
  
  const activeTab = ref('allLogs');
  
  // 이미지 경로 설정
  const backgroundImage = require('@/assets/images/ingame/Border7.png');
  
  // 배경 스타일 설정
  const backgroundStyle = {
    backgroundImage: `url(${backgroundImage})`,
    backgroundSize: 'cover',
    backgroundPosition: 'center',
    padding: '10px',
    boxSizing: 'border-box',
    display: 'flex',
    flexDirection: 'column',
    height: '100%'
  };
  
  const selectTab = (key) => {
    activeTab.value = key;
  };
  </script>
  
  <style scoped>
  .log-section-container {
    display: flex;
    flex-direction: column;
    height: 100%;
  }
  
  .tabs {
    display: flex;
    justify-content: center;
    margin-bottom: 0;
  }
  
  .tab {
    flex: 1;
    padding: 5px; /* 높이를 줄임 */
    cursor: pointer;
    border: 1px solid #333;
    color: white;
    background: linear-gradient(to bottom, #1A4E23, #102F12); 
    text-align: center; /* 텍스트 중앙 배치 */
  }
  
  .tab.active {
    background-color: #555;
    color: #fff;
  }
  
  .log-content {
    flex: 1;
    padding: 10px;
    overflow-y: auto;
    border: 1px solid #444;
    color: white;
  }
  
  /* 스크롤바 스타일링 */
  .log-content::-webkit-scrollbar {
    width: 8px;
  }
  
  .log-content::-webkit-scrollbar-track {
    background: #a56722;
    border-radius: 5px;
  }
  
  .log-content::-webkit-scrollbar-thumb {
    background-color: #274e13;
    border-radius: 5px;
    border: 1px solid #a56722;
  }
  </style>
  