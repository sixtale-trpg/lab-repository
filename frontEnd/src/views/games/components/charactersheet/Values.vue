<template>
  <div class="values-container">
    <div class="values-buttons">
      <div 
        v-for="(button, index) in buttons" 
        :key="index" 
        class="value-button" 
        @click="handleClick(index)"
        :class="{ active: selectedIndex === index }"
        :style="getButtonStyle(selectedIndex === index)"
      >
        <div class="button-content">
          <span class="button-title">{{ button.title }}</span>
          <img src="@/assets/images/character_sheet/Line.png" alt="line" class="button-line"/>
          <span class="button-description">{{ button.description }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';

// 버튼 정보 및 스타일 등록
const buttons = reactive([
  {
    title: '혼돈',
    description: '아무 계획 없이 위험한 상황에 뛰어듭니다.'
  },
  {
    title: '중립',
    description: '들키지 않고 움직이거나 어느 장소에 침투합니다.'
  },
  {
    title: '악',
    description: '자기에게 처한 위험이나 자기의 잘못을 남에게 전가합니다.'
  }
]);

const selectedIndex = ref(null);

const handleClick = (index) => {
  selectedIndex.value = index;
};

// 배경 이미지 경로 설정
const defaultBackgroundImage = require('@/assets/images/character_sheet/value2.png');
const activeBackgroundImage = require('@/assets/images/character_sheet/value1.png');

const getButtonStyle = (isActive) => {
  return {
    backgroundImage: `url(${isActive ? activeBackgroundImage : defaultBackgroundImage})`,
    width: '800px',  // 적절한 너비 설정
    height: '160px',  // 적절한 높이 설정
    borderRadius: '20px'  // 테두리 둥글게 설정
  };
};
</script>

<style scoped>
.values-buttons {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;  /* 간격 조정 */
  padding: 15px;  /* 패딩 추가 */
}

.value-button {
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  padding: 0 20px;  /* 패딩 조정 */
  color: white;
  border-radius: 20px;  /* 테두리 둥글게 설정 */
  width: 820px;  /* 너비 조정 */
  height: 160px;  /* 높이 조정 */
  transition: transform 0.3s, box-shadow 0.3s, background-image 0.3s; /* 애니메이션 추가 */
}

.value-button.active {
  transform: scale(1.05); /* 크기 조정 */
  box-shadow: 0 0 10px rgba(255, 255, 255, 0.8); /* 그림자 추가 */
}

.button-content {
  display: flex;
  width: 100%;
  align-items: center;
  position: relative;
}

.button-title {
  font-size: 1.5rem;  /* 폰트 크기 조정 */
  flex: 1;
  text-align: left;
}

.button-line {
  position: absolute;
  left: 20%;  /* 위치 조정 */
  height: 200%;  /* 버튼 높이에 맞게 설정 */
  margin: 0;
}

.button-description {
  font-size: 1.2rem;  /* 폰트 크기 조정 */
  flex: 1;
  text-align: center;
  padding-left: 20px;  /* 패딩 조정 */
}
</style>
