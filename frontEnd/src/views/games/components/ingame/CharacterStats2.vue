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
      <div class="stat-item" v-for="(attribute, index) in attributes" :key="index">
        <div class="stat-name">{{ attribute.name }}</div>
        <div class="stat-controls">
          <img src="@/assets/images/ingame/Minus.png" alt="감소" class="control-button" @click="decreaseValue(attribute.key)">
          <div class="stat-value-container">
            <div class="stat-value">{{ statsData.attributes[attribute.key].value }}</div>
          </div>
          <img src="@/assets/images/ingame/Plus.png" alt="증가" class="control-button" @click="increaseValue(attribute.key)">
        </div>
        <div class="stat-weight-controls" v-if="statsData.attributes[attribute.key].weight !== undefined">
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
import { ref, defineProps, defineEmits } from 'vue';

const props = defineProps(['statsData']);
const emit = defineEmits(['update:statsData']);

// 임시 데이터 예시 (실제 데이터가 부모 컴포넌트에서 전달될 때 이 부분을 대체합니다)
const statsData = ref({
  level: 1,
  attributes: {
    experience: { value: 5 },
    hp: { value: 10 },
    armor: { value: 4 },
    exaltation: { value: 5 },
    strength: { value: 8, weight: 2 },
    intelligence: { value: 7, weight: 1 },
    constitution: { value: 6, weight: 1 },
    wisdom: { value: 9, weight: 0 },
    dexterity: { value: 8, weight: 2 },
    charisma: { value: 7, weight: 1 },
    
  }
});

const descriptionText = `
  캐릭터의 현재 능력치를 확인하고, 필요에 따라 수정할 수 있습니다.<br>
  변경 사항은 저장 버튼을 누를 때만 적용됩니다.
`;

const attributes = [
  { name: '경험치', key: 'experience' },
  { name: 'HP', key: 'hp' },
  { name: '장갑', key: 'armor' },
  { name: '고양감', key: 'exaltation' },
  { name: '근력', key: 'strength' },
  { name: '지능', key: 'intelligence' },
  { name: '체력', key: 'constitution' },
  { name: '지혜', key: 'wisdom' },
  { name: '민첩성', key: 'dexterity' },
  { name: '매력', key: 'charisma' },
  
];

const increaseValue = (key) => {
  statsData.value.attributes[key].value++;
  emit('update:statsData', statsData.value);
};

const decreaseValue = (key) => {
  if (statsData.value.attributes[key].value > 0) {
    statsData.value.attributes[key].value--;
    emit('update:statsData', statsData.value);
  }
};

const increaseWeight = (key) => {
  statsData.value.attributes[key].weight++;
  emit('update:statsData', statsData.value);
};

const decreaseWeight = (key) => {
  statsData.value.attributes[key].weight--;
  emit('update:statsData', statsData.value);
};

const increaseLevel = () => {
  statsData.value.level++;
  emit('update:statsData', statsData.value);
};

const decreaseLevel = () => {
  if (statsData.value.level > 1) {
    statsData.value.level--;
    emit('update:statsData', statsData.value);
  }
};
</script>

<style scoped>
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
