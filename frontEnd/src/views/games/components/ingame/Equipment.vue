<template>
  <div class="equipment-display-container">
    <div class="equipment-grid">
      <div 
        class="equipment-item" 
        v-for="(item, index) in equipment" 
        :key="index"
        @click="selectItem(item)"
        :class="{ active: selectedItem === item }"
      >
        <img src="@/assets/images/character_sheet/tribebutton.png" alt="아이템 배경" class="item-background">
        <div class="item-name">{{ item.name }}</div>
      </div>
    </div>
    <div class="equipment-details-container">
      <div class="equipment-details">
        <div class="details-header">장비 설명</div>
        <div class="details-content">
          <div class="details-text">{{ selectedItem ? selectedItem.details : '장비를 선택하세요.' }}</div>
        </div>
      </div>
    </div>
    <div class="gold-display">
        <div class="gold-amount">소지 골드: {{ gold }}G</div>
      </div>
    <div class="weight-info">현재 장비 하중: {{ currentWeight }} / 한계 장비 하중: {{ maxWeight }}</div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
// 주석 처리된 부분은 부모 컴포넌트에서 데이터를 받을 때 사용합니다.
// const props = defineProps(['equipmentData', 'gold', 'currentWeight', 'maxWeight']);
// const equipment = computed(() => props.equipmentData);
// const gold = computed(() => props.gold);
// const currentWeight = computed(() => props.currentWeight);
// const maxWeight = computed(() => props.maxWeight);

const equipment = ref([
  { name: '레이피어', details: '한검용, 장검, 무게 1' },
  { name: '투척용 단검 세 자루', details: '투척, 중거리, 무게 0' },
  { name: '낡은 활', details: '중거리, 무게 1, 화살 한 다발 포함' },
  { name: '모험 장비', details: '무게 1' }
]);

const gold = ref(100); // 임시 데이터, 부모 컴포넌트로부터 실제 데이터를 받을 예정
const currentWeight = ref(5); // 임시 데이터
const maxWeight = ref(10); // 임시 데이터
const selectedItem = ref(null);

const selectItem = (item) => {
  selectedItem.value = item;
};
</script>

<style scoped>
.equipment-display-container {
  display: flex;
  flex-direction: column;
  padding: 20px;
}

.equipment-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr); /* 그리드 형태로 표시 */
  gap: 10px;
  margin-bottom: 20px;
}

.equipment-item {
  position: relative;
  padding: 10px;
  cursor: pointer;
  transition: transform 0.3s;
  display: flex;
  align-items: center;
  justify-content: center;
}

.item-background {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  object-fit: cover;
  z-index: -1; 
}

.item-name {
  font-weight: bold;
  color: #fff;
  z-index: 1;
  text-align: center;
}

.equipment-item.active, .equipment-item:hover {
  transform: scale(1.05); 
}

.equipment-details-container {
  display: flex;
  justify-content: space-between; 
  align-items: flex-start;
}

.equipment-details {
  flex: 2;
  display: flex;
  flex-direction: column;
  margin-right: 20px; 
  padding: 20px;
}

.details-header {
  font-weight: bold;
  color: #fff;
  margin-bottom: 10px;
}

.details-content {
  background-color: #222;
  padding: 10px;
  border-radius: 5px;
  color: #fff;
  flex: 1;
}

.gold-display {
  flex: 1;
  text-align: right; 
}

.gold-amount {
  font-size: 1.2rem;
  color: gold;
  margin-top: 5px; 
  padding: 20px;
}

.weight-info {
  font-size: 1.8rem; 
  text-align: center; 
}
</style>
