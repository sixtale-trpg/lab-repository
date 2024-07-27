<template>
  <div class="attributes-content">
    <div class="stats-description">
      <img src="@/assets/images/character_sheet/stats_description.png" alt="능력치 설명" class="stats-description-image">
      <div class="top-description">
        <img src="@/assets/images/character_sheet/nickname_light.png" alt="상단 설명 배경" class="top-description-image">
        <span class="top-description-text">16, 15, 13, 12, 9, 8의 여섯 수치를 원하는 능력치에 배정합니다.</span>
      </div>
      <div class="stats-content">
        <div class="attribute-rows">
          <div class="attribute-row" v-for="(row, rowIndex) in attributeRows" :key="rowIndex">
            <div class="attribute" v-for="(attribute, index) in row" :key="index">
              <span class="attribute-name">{{ attribute.name }}</span>
              <div class="attribute-value-container">
                <img src="@/assets/images/character_sheet/stats_select.png" alt="값 배경" class="attribute-value-background">
                <select class="attribute-select" v-model="attribute.value" @click="toggleDropdown(attribute)">
                  <option v-for="option in attribute.options" :value="option" :key="option">{{ option }}</option>
                </select>
                <img src="@/assets/images/character_sheet/stats_circle.png" alt="증가" class="attribute-circle" />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue';

const attributes = reactive([
  {
    name: '근력',
    value: '16',
    options: ['16', '15', '13', '10', '9', '8'],
    showDropdown: false
  },
  {
    name: '지능',
    value: '9',
    options: ['16', '15', '13', '10', '9', '8'],
    showDropdown: false
  },
  {
    name: '체력',
    value: '15',
    options: ['16', '15', '13', '10', '9', '8'],
    showDropdown: false
  },
  {
    name: '지혜',
    value: '13',
    options: ['16', '15', '13', '10', '9', '8'],
    showDropdown: false
  },
  {
    name: '민첩성',
    value: '12',
    options: ['16', '15', '13', '10', '9', '8'],
    showDropdown: false
  },
  {
    name: '매력',
    value: '8',
    options: ['16', '15', '13', '10', '9', '8'],
    showDropdown: false
  }
]);

const attributeRows = reactive([
  [attributes[0], attributes[1]],
  [attributes[2], attributes[3]],
  [attributes[4], attributes[5]]
]);

const toggleDropdown = (attribute) => {
  attribute.showDropdown = !attribute.showDropdown;
};
</script>

<style scoped>
.attributes-selection-container {
  padding: 20px;
  background-color: #2c2c2c;
  color: #fff;
  text-align: center;
  position: relative;
}

.description-box {
  width: 100%;
  margin-bottom: 20px;
}

.attributes-content {
  position: absolute;
  top: 80px; /* 필요한 위치에 맞게 조정 */
  left: 50%;
  transform: translateX(-50%);
  width: 90%;
}

.stats-description {
  position: relative;
}

.stats-description-image {
  width: 100%;
}

.top-description {
  position: absolute;
  top: 10%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 80%;
  text-align: center;
  margin-bottom: 20px;
}

.top-description-image {
  width: 100%;
}

.top-description-text {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 1rem; /* 폰트 크기 조정 */
  color: #fff;
}

.stats-content {
  position: absolute;
  top: 30%;
  left: 50%;
  transform: translate(-50%, 0);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 80%; /* 전체 너비 조정 */
}

.attribute-rows {
  width: 100%;
}

.attribute-row {
  display: flex;
  justify-content: space-between;
  width: 100%;
  margin-bottom: 20px; /* 간격 조정 */
}

.attribute {
  background-color: transparent;
  padding: 5px 10px;
  border-radius: 10px;
  display: flex;
  flex-direction: column;
  align-items: center;
  color: white;
  width: 45%; /* 각 항목 너비 조정 */
}

.attribute-name {
  font-size: 1rem; /* 크기 조정 */
  margin-bottom: 5px;
}

.attribute-value-container {
  position: relative;
  display: flex;
  align-items: center;
  cursor: pointer;
}

.attribute-value-background {
  width: 140px; /* 크기 조정 */
  height: 60px; /* 크기 조정 */
}

.attribute-select {
  width: 100%;
  font-size: 1.5rem; /* 크기 조정 */
  cursor: pointer;
  background-color: transparent;
  color: #fff;
  border: none;
  text-align: center;
  -webkit-appearance: none; /* 크롬 및 사파리 기본 스타일 제거 */
  -moz-appearance: none; /* 파이어폭스 기본 스타일 제거 */
  appearance: none; /* 기본 스타일 제거 */
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
}

.attribute-select option {
  background-color: #2c2c2c;
  color: #fff;
}

.attribute-circle {
  width: 30px; /* 크기 조정 */
  height: 30px; /* 크기 조정 */
  margin-left: 10px; /* 간격 조정 */
  cursor: pointer;
}

.footer-buttons {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-top: 10px;
}

.footer-buttons button {
  background: #555;
  border: none;
  color: white;
  padding: 5px 10px;
  cursor: pointer;
  border-radius: 5px;
}

.footer-buttons button:hover {
  background: #777;
}
</style>
