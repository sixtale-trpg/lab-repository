<template>
    <div class="header-container">
      <h1>게임 이름</h1>
      <div class="header-details">
        <div class="game-info">
          <p>게임 정보: </p>
          <p>라운드: {{ round }}</p>
        </div>
        <div class="timer">
          <p>타이머: {{ timer }}</p>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  
  const round = ref(1);
  const timer = ref('00:00:00');
  </script>
  
  <style scoped>
  .header-container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px;
    background-color: #333;
    color: white;
  }
  
  .header-details {
    display: flex;
    flex-direction: column;
    align-items: flex-end;
  }
  
  .game-info, .timer {
    margin: 0;
  }
  </style>
  