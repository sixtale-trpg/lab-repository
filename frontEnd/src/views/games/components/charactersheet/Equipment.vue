<template>
  <div class="equipment-selection-container">
    <p>선호하는 무기를 선택하십시오</p>
    <div class="title-bar">
      <img src="@/assets/images/character_sheet/description_box.png" alt="Title Bar" class="title-bar-image">
      <span class="title-bar-text">
        하주은 9. 근원입니다. 갖고 있는 물건은 다음과 같습니다.
        던전용 식량 (중1흔, 무게1), 가족 칼장 (중1흔, 무게1), 신탁력 특성3회분, 돈 10실.
      </span>
    </div>
    <div class="equipment-buttons">
      <div class="equipment-category">
        <div class="title-container">
          <img src="@/assets/images/character_sheet/nickname_light.png" alt="근거리 무기 선택" class="title-image">
          <span class="title-text">근거리 무기 선택</span>
        </div>
        <div 
          v-for="(button, index) in closeRangeWeapons" 
          :key="index" 
          class="equipment-button" 
          @click="selectCloseRangeWeapon(index)"
          :style="getButtonStyle(selectedCloseRangeWeapon === index)"
          :class="{ active: selectedCloseRangeWeapon === index }"
        >
          <span>{{ button.title }}</span>
        </div>
      </div>
      <div class="equipment-category">
        <div class="title-container">
          <img src="@/assets/images/character_sheet/nickname_light.png" alt="원거리 무기 선택" class="title-image">
          <span class="title-text">원거리 무기 선택</span>
        </div>
        <div 
          v-for="(button, index) in longRangeWeapons" 
          :key="index" 
          class="equipment-button" 
          @click="selectLongRangeWeapon(index)"
          :style="getButtonStyle(selectedLongRangeWeapon === index)"
          :class="{ active: selectedLongRangeWeapon === index }"
        >
          <span>{{ button.title }}</span>
        </div>
      </div>
      <div class="equipment-category">
        <div class="title-container">
          <img src="@/assets/images/character_sheet/nickname_light.png" alt="다음 중 하나 선택" class="title-image">
          <span class="title-text">다음 중 하나 선택</span>
        </div>
        <div 
          v-for="(button, index) in miscellaneous" 
          :key="index" 
          class="equipment-button" 
          @click="selectMiscellaneous(index)"
          :style="getButtonStyle(selectedMiscellaneous === index)"
          :class="{ active: selectedMiscellaneous === index }"
        >
          <span>{{ button.title }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';

// 버튼 정보 및 스타일 등록
const closeRangeWeapons = reactive([
  { title: '단도 (반검용, 무게 1)' },
  { title: '레이피어 (한검용, 장검, 무게 1)' },
]);

const longRangeWeapons = reactive([
  { title: '투척용 단검 세 자루 (투척, 중거리, 무게 0)' },
  { title: '낡은 활 (중거리, 무게 1), 화살 한 다발 (발사, 무게 1)' },
]);

const miscellaneous = reactive([
  { title: '모험 장비 (무게 1)' },
]);

const selectedCloseRangeWeapon = ref(null);
const selectedLongRangeWeapon = ref(null);
const selectedMiscellaneous = ref(null);

const selectCloseRangeWeapon = (index) => {
  selectedCloseRangeWeapon.value = index;
};

const selectLongRangeWeapon = (index) => {
  selectedLongRangeWeapon.value = index;
};

const selectMiscellaneous = (index) => {
  selectedMiscellaneous.value = index;
};

// 배경 이미지 경로 설정
const defaultBackgroundImage = require('@/assets/images/character_sheet/equip1.png');
const activeBackgroundImage = require('@/assets/images/character_sheet/equip2.png');

const getButtonStyle = (isActive) => {
  return {
    backgroundImage: `url(${isActive ? activeBackgroundImage : defaultBackgroundImage})`
  };
};
</script>

<style scoped>
.equipment-selection-container {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.title-bar {
  position: relative;
  width: 100%;
  height: 80px; /* 제목 바 높이 조정 */
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px; /* 제목 바와 내용 사이 간격 */
}

.title-bar-image {
  width: 100%;
  height: 100%;
}

.title-bar-text {
  position: absolute;
  color: white;
  font-size: 1rem; /* 텍스트 크기 조정 */
  text-align: center;
  padding: 0 20px; /* 텍스트 패딩 */
  white-space: pre-wrap; /* 여러 줄 텍스트 지원 */
}

.equipment-buttons {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px; /* 간격 조정 */
}

.equipment-category {
  margin-bottom: 20px; /* 간격 조정 */
  text-align: center; /* 카테고리 안의 내용 가운데 정렬 */
}

.title-container {
  position: relative;
  display: flex; /* flexbox로 설정 */
  align-items: center; /* 수직 가운데 정렬 */
  justify-content: center; /* 수평 가운데 정렬 */
  margin-bottom: 15px; /* 간격 조정 */
}

.title-image {
  width: 350px; /* 너비 조정 */
  height: 60px; /* 높이 조정 */
  display: block;
}

.title-text {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: #fff;
  font-size: 1.2rem; /* 폰트 크기 조정 */
  white-space: nowrap;
}

.equipment-button {
  background-size: cover;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  cursor: pointer;
  margin-bottom: 15px; /* 간격 조정 */
  padding: 10px; /* 패딩 조정 */
  font-size: 1rem; /* 폰트 크기 조정 */
  width: 400px; /* 너비 조정 */
  height: 60px; /* 높이 조정 */
  border-radius: 10px; /* 테두리 반경 조정 */
  position: relative;
  transition: transform 0.3s, box-shadow 0.3s, background-image 0.3s; /* 애니메이션 추가 */
}

.equipment-button.active {
  transform: scale(1.05); /* 크기 조정 */
  box-shadow: 0 0 10px rgba(255, 255, 255, 0.8); /* 그림자 추가 */
}
</style>
