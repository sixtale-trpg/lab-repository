<template>
  <div class="attributes-content">
    <div class="description-bar">
      <img src="@/assets/images/character_sheet/stat_description.png" alt="설명 바" class="description-bar-image">
      <span class="description-bar-text">
        많은 룰에서 플레이어 캐릭터의 능력치와 능력수정치를 사용합니다. 능력치는 근력, 체력, 민첩성, 지능, 지혜, 매력의 여섯 가지이고, 3에서 18까지로 정의됩니다. 15은 사람으로서 최고 수준임을 가리킵니다. 판정을 할 때는 능력치에서 유래한 능력수정치가 사용됩니다. 근력, 체력, 민첩성, 지혜, 매력의 약자를 씁니다. 능력수정치는 -3에서 +3까지이고, 능력치를 따라서 정해집니다.
      </span>
    </div>
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
                <select class="attribute-select" v-model="formData.attributes[attribute.key]" @change="updateOptions">
                  <option value="" selected>전체</option>
                  <option v-for="option in availableOptions(attribute)" :value="option" :key="option">{{ option }}</option>
                </select>
                <div class="attribute-circle">
                  <img src="@/assets/images/character_sheet/stats_circle.png" alt="보정치 배경" class="circle-background">
                  <span class="modifier-value">{{ getModifier(formData.attributes[attribute.key]) }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, computed, toRefs } from 'vue';

const props = defineProps(['formData']);
const { formData } = toRefs(props);

const initialOptions = ['16', '15', '13', '12', '9', '8'];

const attributes = reactive([
  {
    name: '근력',
    key: 'strength',
  },
  {
    name: '지능',
    key: 'intelligence',
  },
  {
    name: '체력',
    key: 'constitution',
  },
  {
    name: '지혜',
    key: 'wisdom',
  },
  {
    name: '민첩성',
    key: 'dexterity',
  },
  {
    name: '매력',
    key: 'charisma',
  }
]);

const attributeRows = reactive([
  [attributes[0], attributes[1]],
  [attributes[2], attributes[3]],
  [attributes[4], attributes[5]]
]);

const selectedValues = computed(() => Object.values(formData.value.attributes).filter(val => val));

const availableOptions = (attribute) => {
  return initialOptions.filter(option => !selectedValues.value.includes(option) || formData.value.attributes[attribute.key] === option);
};

const updateOptions = () => {
  // 강제로 갱신을 유도하여 select 옵션 업데이트
  attributes.forEach(attribute => {
    formData.value.attributes[attribute.key].options = availableOptions(attribute);
  });
};

const getModifier = (value) => {
  if (!value) return '';
  const num = parseInt(value, 10);
  const modifier = Math.floor((num - 10) / 2);
  return modifier > 0 ? `+${modifier}` : modifier;
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

.description-bar {
  position: relative;
  width: 100%;
  height: 80px; /* 제목 바 높이 조정 */
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px; /* 제목 바와 내용 사이 간격 */
}

.description-bar-image {
  width: 100%;
  height: 100%;
}

.description-bar-text {
  position: absolute;
  color: white;
  font-size: 1rem; /* 텍스트 크기 조정 */
  text-align: center;
  padding: 0 20px; /* 텍스트 패딩 */
  white-space: pre-wrap; /* 여러 줄 텍스트 지원 */
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
  font-size: 1.5rem; /* 크기 조정 */
  margin-bottom: 5px;
}

.attribute-value-container {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: space-between;
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
  position: relative;
  width: 60px; /* 크기 조정 */
  height: 60px; /* 크기 조정 */
  display: flex;
  align-items: center;
  justify-content: center;
  margin-left: 10px; /* 간격 조정 */
  margin-top: 20px; /* 아래로 이동 */
}

.circle-background {
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
}

.modifier-value {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 1.2rem; /* 폰트 크기 조정 */
  color: #fff;
  text-align: center;
}
</style>
