<template>
  <div class="attributes-content">
    <div class="description-bar">
      <img src="@/assets/images/character_sheet/stat_description.png" alt="설명 바" class="description-bar-image">
      <span class="description-bar-text" v-html="descriptionText"></span>
    </div>
    <div class="level-container">
      <div class="level-controls">
        <img src="@/assets/images/ingame/Minus.png" alt="감소" class="control-button" @click="decreaseLevel">
        <span class="level-text">Lv. {{ statsData.level }}</span>
        <img src="@/assets/images/ingame/Plus.png" alt="증가" class="control-button" @click="increaseLevel">
      </div>
    </div>
    <div class="stats-grid">
      <!-- 개별 속성들 렌더링 -->
      <div class="stat-item" v-for="(attr, index) in individualAttributes" :key="index">
        <div class="stat-name">{{ attr.name }}</div>
        <div class="stat-controls">
          <img src="@/assets/images/ingame/Minus.png" alt="감소" class="control-button" @click="decreaseIndividualValue(attr.key)">
          <div class="stat-value-container">
            <div class="stat-value">{{ statsData[attr.key] }}</div>
          </div>
          <img src="@/assets/images/ingame/Plus.png" alt="증가" class="control-button" @click="increaseIndividualValue(attr.key)">
        </div>
      </div>
      <!-- 스탯 속성들 렌더링 -->
      <div class="stat-item" v-for="(attribute, index) in attributes" :key="index">
        <div class="stat-name">{{ attribute.name }}</div>
        <div class="stat-controls">
          <img src="@/assets/images/ingame/Minus.png" alt="감소" class="control-button" @click="decreaseStatValue(attribute.key)">
          <div class="stat-value-container">
            <div class="stat-value">{{ statsData.attributes[attribute.key] ? statsData.attributes[attribute.key].value : 0 }}</div>
          </div>
          <img src="@/assets/images/ingame/Plus.png" alt="증가" class="control-button" @click="increaseStatValue(attribute.key)">
        </div>
        <div class="stat-weight-controls" v-if="statsData.attributes[attribute.key] && statsData.attributes[attribute.key].weight !== undefined">
          <img src="@/assets/images/ingame/Minus.png" alt="감소" class="control-button" @click="decreaseWeight(attribute.key)">
          <div class="stat-weight-container">
            <div class="stat-weight">{{ statsData.attributes[attribute.key].weight }}</div>
            <div class="weight-label">(가중치)</div>
          </div>
          <img src="@/assets/images/ingame/Plus.png" alt="증가" class="control-button" @click="increaseWeight(attribute.key)">
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue';

const props = defineProps({
  statsData: Object,
});

const emit = defineEmits(['update:statsData']);

const descriptionText = `
  캐릭터의 현재 능력치를 확인하고, 필요에 따라 수정할 수 있습니다.<br>
  변경 사항은 저장 버튼을 누를 때만 적용됩니다.
`;

const individualAttributes = [
  { name: '경험치', key: 'exp' },
  { name: 'HP', key: 'currentHp' },
  { name: '장갑', key: 'glove' },
  { name: '고양감', key: 'inspirationScore' },
];

const attributes = [
  { name: '근력', key: 1 },
  { name: '민첩성', key: 2 },
  { name: '체력', key: 3 },
  { name: '지능', key: 4 },
  { name: '지혜', key: 5 },
  { name: '매력', key: 6 },
];

const increaseIndividualValue = (key) => {
  props.statsData[key]++;
  emit('update:statsData', { ...props.statsData });
};

const decreaseIndividualValue = (key) => {
  if (props.statsData[key] > 0) {
    props.statsData[key]--;
    emit('update:statsData', { ...props.statsData });
  }
};

const increaseStatValue = (key) => {
  if (props.statsData.attributes[key]) {
    props.statsData.attributes[key].value++;
    emit('update:statsData', { ...props.statsData });
  }
};

const decreaseStatValue = (key) => {
  if (props.statsData.attributes[key] && props.statsData.attributes[key].value > 0) {
    props.statsData.attributes[key].value--;
    emit('update:statsData', { ...props.statsData });
  }
};

const increaseWeight = (key) => {
  if (props.statsData.attributes[key]) {
    props.statsData.attributes[key].weight++;
    emit('update:statsData', { ...props.statsData });
  }
};

const decreaseWeight = (key) => {
  if (props.statsData.attributes[key] && props.statsData.attributes[key].weight > 0) {
    props.statsData.attributes[key].weight--;
    emit('update:statsData', { ...props.statsData });
  }
};

const increaseLevel = () => {
  props.statsData.level++;
  emit('update:statsData', { ...props.statsData });
};

const decreaseLevel = () => {
  if (props.statsData.level > 1) {
    props.statsData.level--;
    emit('update:statsData', { ...props.statsData });
  }
};
</script>





<style scoped>
/* 스타일은 그대로 유지 */
.attributes-content {
  padding: 20px;
  color: #fff;
  text-align: center;
}

.description-bar {
  position: relative;
  width: 100%;
  height: auto;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px;
}

.description-bar-image {
  width: 100%;
  height: 120px;
}

.description-bar-text {
  position: absolute;
  color: white;
  justify-content: center;
  font-size: 1rem;
  text-align: center;
  white-space: pre-wrap;
  line-height: 1.1;
}

.level-container {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px;
  background-color: #1d1707;
  width: 30%;
  padding: 10px;
  border-radius: 5px;
  gap: 10px;
  margin: 0 auto 20px;
}

.level-controls {
  display: flex;
  align-items: center;
}

.control-button {
  width: 20px;
  height: 20px;
  cursor: pointer;
  margin: 0 5px;
}

.level-text {
  font-size: 1.5rem;
  color: #fff;
  margin: 0 10px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
  justify-items: center;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 85%;
  padding: 10px;
  border-radius: 5px;
  background-color: #1d1707;
  position: relative;
}

.stat-name {
  font-size: 1.2rem;
  color: #fff;
  margin-bottom: 10px;
}

.stat-controls, .stat-weight-controls {
  display: flex;
  align-items: center;
  background-color: #1d1707;
  padding: 5px;
  border-radius: 5px;
  gap: 5px;
}

.stat-value-container, .stat-weight-container {
  margin: 0 5px;
  font-size: 1.2rem;
  min-width: 40px;
  text-align: center;
  background-color: #1d1707;
  padding: 5px;
  border-radius: 5px;
}

.stat-value, .stat-weight {
  color: #fff;
}

.weight-label {
  font-size: 0.8rem;
  color: #fff;
  margin-top: 5px;
  text-align: center;
}
</style>
