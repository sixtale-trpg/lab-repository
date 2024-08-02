<template>
  <div class="gm-section-container" :style="backgroundStyle">
    <div class="gm-info-box">
      <img :src="gmImage" alt="Game Master" class="gm-image">
      <p class="gm-name">{{ gmNickname }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
// import axios from 'axios'; // 필요 시 axios 사용

const props = defineProps({
  gm: {
    type: Object,
    required: true
  },
  isGM: {
    type: Boolean,
    required: true
  },
  canStartGame: {
    type: Boolean,
    default: false
  }
});

const gmNickname = ref('미카엘');

// 배경 이미지 설정
const backgroundImage = require('@/assets/images/ingame/Border2.png');
const gmImage = require('@/assets/images/ingame/GM.png');

const backgroundStyle = {
  backgroundImage: `url(${backgroundImage})`,
  backgroundSize: 'cover',
  backgroundPosition: 'center',
  padding: '2px', 
  boxSizing: 'border-box',
  height: '5%', 
  display: 'flex',
  alignItems: 'center',
};

// GM 닉네임 가져오는 로직 (주석 처리)
/*
onMounted(() => {
  axios.get('/rooms/:roomID')
    .then(response => {
      gmNickname.value = response.data.data.gmNickname;
    })
    .catch(error => {
      console.error("Error fetching GM nickname:", error);
    });
});
*/
</script>

<style scoped>
.gm-section-container {
  display: flex;
  align-items: center;
  justify-content: center; 
  box-sizing: border-box;
  border: 1px solid #5a4d41;
}

.gm-info-box {
  width: 90%;
  height: 100%;
  text-align: center;
  padding: 5px;
  color: #ffffff;
  overflow: hidden; 
  display: flex;
  flex-direction: row; 
  justify-content: center;
  align-items: center; 
}

.gm-image {
  max-width: 50%;
  margin-right: 10px;
}

.gm-name {
  margin: 0;
  white-space: nowrap; 
  font-size: 1.1rem;
  overflow: hidden; 
  text-overflow: ellipsis; 
}
</style>
