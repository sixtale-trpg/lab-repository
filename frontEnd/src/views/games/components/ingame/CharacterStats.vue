<template>
    <div class="character-stats">
      <img src="@/assets/images/ingame/Border6.png" alt="Stats Background" class="stats-background">
      <div class="stats-content" v-if="stats">
        <h3 class="character-name">{{ characterName }}</h3>
        <div class="level-job">
          <span class="level">Lv. {{ level }}</span>
          <span class="job">{{ job }}</span>
        </div>
        <div class="stats-grid">
          <div class="stat-item" v-for="(stat, index) in stats" :key="index">
            <span class="stat-name">{{ stat.name }}</span>
            <span class="stat-value">{{ stat.value }}</span>
          </div>
        </div>
      </div>
      <div v-else>
        Loading...
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue';
  // import axios from 'axios';  // 실제 API 호출 시 사용
  
  const characterName = ref('카를로스');
  const level = ref(2);
  const job = ref('드루이드');
  const stats = ref([
    { name: '경험치', value: 16 },
    { name: 'HP', value: 8 },
    { name: '근력', value: 12 },
    { name: '지능', value: 15 },
    { name: '체력', value: 10 },
    { name: '지혜', value: 17 },
    { name: '민첩성', value: 16 },
    { name: '매력', value: 8 },
  ]);
  

  // 나중 백엔드 추가 로직
  onMounted(async () => {
    try {
      // const response = await axios.get(`/rooms/${roomID}/sheets/${playMemberID}`);
      // const characterData = response.data;
      // characterName.value = characterData.name;
      // level.value = characterData.level;
      // job.value = characterData.job;
      // stats.value = characterData.stats;
    } catch (error) {
      console.error('Error loading character data:', error);
    }
  });
  </script>
  
  <style scoped>
  .character-stats {
    position: relative;
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    font-size: 0.5em; /* 텍스트 크기 조정 */
    padding: 10px;
    box-sizing: border-box;
    overflow: hidden;
  }
  
  .stats-background {
    position: absolute;
    width: 100%;
    height: 100%;
    top: 0;
    left: 0;
    z-index: -1;
    object-fit: contain;
  }
  
  .stats-content {
    z-index: 1;
    width: 100%;
    height: auto; /* 높이 자동 조정 */
    text-align: center;
  }
  
  .character-name {
    font-size: 1.1em;
    margin-bottom: 5px;
  }
  
  .level-job {
    display: flex;
    justify-content: center; /* 중앙에 배치 */
    width: 100%;
    padding: 2px 0;
  }
  
  .level, .job {
    margin: 0 10px; /* 양쪽 간격 조정 */
  }
  
  .stats-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 3px;
    width: 100%;
  }
  
  .stat-item {
    display: flex;
    justify-content: space-between;
    background-color: #3e2723;
    padding: 3px;
    border-radius: 3px;
  }
  
  .stat-name, .stat-value {
    flex: 1;
    text-align: center;
  }
  </style>
  