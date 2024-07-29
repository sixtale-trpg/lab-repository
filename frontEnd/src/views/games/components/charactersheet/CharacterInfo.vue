<template>
  <div class="top-section">
    <div class="input-group">
      <div class="title-container">
        <img src="@/assets/images/character_sheet/nickname_light.png" alt="이름" class="title-image">
        <span class="title-text">이름</span>
      </div>
      <div class="name-input-container">
        <img src="@/assets/images/character_sheet/name_box.png" alt="이름 입력 배경" class="name-input-image">
        <input type="text" v-model="formData.name" id="name" class="name-input" />
      </div>
    </div>
    <div class="input-group">
      <div class="title-container">
        <img src="@/assets/images/character_sheet/nickname_light.png" alt="종족" class="title-image">
        <span class="title-text">종족</span>
      </div>
      <div class="race-buttons">
        <button 
          v-for="race in races" 
          :key="race" 
          :class="['race-button', { active: formData.selectedRace === race }]"
          @click="selectRace(race)">
          <img src="@/assets/images/character_sheet/tribebutton.png" :alt="race" class="race-button-image">
          <span class="race-button-text">{{ race }}</span>
        </button>
      </div>
    </div>
  </div>
  <div class="input-group history-input-group">
    <div class="history-title-container">
      <img src="@/assets/images/character_sheet/Vector.png" alt="히스토리" class="history-title-image">
      <span class="history-title-text">히스토리</span>
    </div>
    <div class="history-input-container">
      <img src="@/assets/images/character_sheet/history_box.png" alt="히스토리 입력 배경" class="history-input-image">
      <input type="text" v-model="formData.history" id="history" class="history-input" />
    </div>
  </div>
</template>

<script setup>
import { ref, toRefs } from 'vue';

const props = defineProps(['formData']);
const { formData } = toRefs(props);

const races = ref(['하플링', '인간', '엘프', '드워프']); // 예시 종족 목록

function selectRace(race) {
  formData.value.selectedRace = race;
}
</script>

<style scoped>
.top-section {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px; /* 간격 조정 */
  flex-wrap: wrap; /* 작은 화면에서도 줄 바꿈 허용 */
}

.input-group {
  margin-bottom: 15px; /* 간격 조정 */
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 48%; /* 너비 조정 */
}

.name-input-container {
  position: relative;
  width: 200px; /* 너비 조정 */
  height: 50px; /* 높이 조정 */
}

.name-input-image {
  width: 100%;
  height: 100%;
}

.name-input {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  padding: 10px;
  border: none;
  background: transparent;
  color: #fff;
  text-align: center;
  font-size: 1rem; /* 폰트 크기 조정 */
}

.title-container {
  position: relative;
  display: inline-block;
  margin-bottom: 10px; /* 간격 조정 */
}

.title-image {
  width: 200px; /* 너비 조정 */
  height: 50px; /* 높이 조정 */
  display: block;
}

.title-text {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: #fff;
  font-size: 1rem; /* 폰트 크기 조정 */
  white-space: nowrap;
}

.history-input-group {
  width: 100%;
}

.history-title-container {
  position: relative;
  display: inline-block;
  margin-bottom: 10px; /* 간격 조정 */
}

.history-title-image {
  width: 200px; /* 너비 조정 */
  height: 50px; /* 높이 조정 */
  display: block;
}

.history-title-text {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: #fff;
  font-size: 1rem; /* 폰트 크기 조정 */
  white-space: nowrap;
}

.history-input-container {
  position: relative;
  width: 100%;
  height: 300px; /* 높이 조정 */
}

.history-input-image {
  width: 100%;
  height: 100%;
}

.history-input {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%; /* 높이 조정 */
  padding: 20px;
  border: none;
  background: transparent;
  color: #fff;
  font-size: 1rem; /* 폰트 크기 조정 */
}

.race-buttons {
  display: flex;
  justify-content: center;
  gap: 8px;
  flex-wrap: wrap;
}

.race-button {
  position: relative;
  padding: 0;
  border: none;
  background: none;
  cursor: pointer;
  width: 150px; /* 너비 조정 */
  height: 50px; /* 높이 조정 */
  transition: transform 0.2s;
}

.race-button-image {
  width: 100%;
  height: 100%;
}

.race-button-text {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: #fff;
  font-size: 1rem; /* 폰트 크기 조정 */
}

.race-button.active {
  transform: scale(1.1);
  border: 2px solid #fff;
}
</style>
