<template>
  <div class="values-container">
    <div class="values-cards">
      <div 
        v-for="belief in currentOptions" 
        :key="belief.beliefID"
        :class="['action-card', { selected: localFormData.selectedValue === belief.beliefID }]"
        @click="selectBelief(belief.beliefID)"
      >
        <div class="card-content">
          <span class="card-title">{{ belief.beliefName }}</span>
          <span class="card-description">{{ belief.description }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, toRefs, watch } from 'vue';

const props = defineProps({
  formData: Object,
  currentOptions: Array
});

const { formData, currentOptions } = toRefs(props);

const emit = defineEmits(['update:selectedBelief']);

// 로컬 상태를 만들어 formData의 변경을 감지하고 부모에 반영
const localFormData = reactive({ ...formData.value });

// 부모 컴포넌트에서 넘어온 값이 변경될 경우 로컬 상태를 동기화
watch(() => formData.value, (newVal) => {
  Object.assign(localFormData, newVal);
});

function selectBelief(beliefID) {
  localFormData.selectedValue = beliefID;
  emit('update:selectedBelief', beliefID);
}
</script>

<style scoped>
.values-container {
  display: flex;
  justify-content: center;
  padding-top: 100px;
  height: 80%; 
  overflow-y: auto; 
}

.values-cards {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center; /* 중앙 정렬 */
  width: 100%;
}

.action-card {
  background: rgba(0, 0, 0, 0.5);
  border-radius: 5px;
  padding: 20px;
  cursor: pointer;
  transition: background 0.3s;
  width: 200px;
}

.action-card:hover {
  background: rgba(0, 0, 0, 0.7);
}

.action-card.selected {
  background: rgba(0, 0, 0, 0.7);
  border: 1px solid #fff;
}

.card-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
}

.card-title {
  font-size: 1.5rem;
  margin-bottom: 10px;
}

.card-description {
  font-size: 1.2rem;
  text-align: left; 
  width: 100%;
}
</style>
