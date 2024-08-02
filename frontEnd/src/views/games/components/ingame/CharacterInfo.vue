<template>
  <div class="character-info-container">
    <div class="info-section">
      <div class="left-section">
        <div class="image-container">
          <img :src="characterData.image || defaultImage" alt="캐릭터 이미지" class="character-image">
        </div>
      </div>
      <div class="right-section">
        <div class="info-item">
          <div class="title-container">
            <img src="@/assets/images/character_sheet/nickname_light.png" alt="이름 배경" class="title-image">
            <span class="title-text">이름</span>
          </div>
          <div class="info-box">
            <img src="@/assets/images/ingame/Button.png" alt="이름 배경" class="info-box-image">
            <span class="info-text">{{ characterData.name }}</span>
          </div>
        </div>
        <div class="info-item">
          <div class="title-container">
            <img src="@/assets/images/character_sheet/nickname_light.png" alt="종족 배경" class="title-image">
            <span class="title-text">종족</span>
          </div>
          <div class="info-box">
            <img src="@/assets/images/ingame/Button.png" alt="종족 배경" class="info-box-image">
            <span class="info-text">{{ characterData.race }}</span>
          </div>
        </div>
        <div class="info-item">
          <div class="title-container">
            <img src="@/assets/images/character_sheet/nickname_light.png" alt="직업 배경" class="title-image">
            <span class="title-text">직업</span>
          </div>
          <div class="info-box">
            <img src="@/assets/images/ingame/Button.png" alt="직업 배경" class="info-box-image">
            <span class="info-text">{{ characterData.job }}</span>
          </div>
        </div>
      </div>
    </div>
    <div class="history-section">
      <div class="history-title-container">
        <img src="@/assets/images/character_sheet/Vector.png" alt="히스토리 배경" class="history-title-image">
        <span class="history-title-text">히스토리</span>
      </div>
      <div class="history-content">
        <p>{{ characterData.background }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
// 주석 처리된 부분은 부모 컴포넌트에서 데이터를 받을 때 사용합니다.
// const props = defineProps(['characterData']);
// const characterData = computed(() => props.characterData);

import defaultImage from '@/assets/images/ingame/user1.png';

const characterData = ref({
  image: '', // 캐릭터 이미지 URL, 더미 데이터 사용
  name: '예시 이름',
  race: '인간',
  job: '전사',
  background: '이 캐릭터는 아주 특별한 배경을 가지고 있습니다...'
});
</script>


<style scoped>
.character-info-container {
  width: 100%;
  padding: 20px;
}

.info-section {
  display: flex;
  justify-content: space-between; 
  margin-bottom: 20px;
}

.left-section {
  flex: 0 0 250px; 
  margin-right: 20px;
}

.image-container {
  border-radius: 50%;
  overflow: hidden;
  width: 200px;
  height: 200px;
  border: 3px solid #fff;
  display: flex;
  justify-content: center;
  align-items: center;
}

.character-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.right-section {
  flex: 1; 
  display: flex;
  flex-direction: column;
  justify-content: center;
  margin-right: 20px; 
}

.info-item {
  margin-bottom: 20px; 
  display: flex;
  align-items: center;
}

.title-container {
  position: relative;
  display: inline-block;
  margin-right: 10px; /* 제목과 박스 사이 간격 */
}

.title-image {
  display: block;
}

.title-text {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: #fff;
  font-size: 1.4rem; 
  white-space: nowrap;
}

.info-box {
  position: relative;
  display: flex;
  align-items: center;
  height: 50px; 
}

.info-box-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.info-text {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: #fff;
  font-size: 1.4rem; 
  white-space: nowrap;
}

.history-section {
  width: 100%;
  margin-top: 20px;
}

.history-title-container {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
  position: relative;
}

.history-title-image {
  width: 200px;
  height: 50px;
}

.history-title-text {
  position: absolute;
  top: 50%;
  left: 20px;
  transform: translateY(-50%);
  color: #fff;
  font-size: 1.4rem; 
  white-space: nowrap;
}

.history-content {
  background: #333;
  color: #fff;
  padding: 10px;
  border-radius: 5px;
  width: 100%;
  box-sizing: border-box;
  text-align: left;
}
</style>
