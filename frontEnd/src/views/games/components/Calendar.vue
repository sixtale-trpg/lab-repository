<template>
  <div class="calendar">
    <!-- 여기에 캘린더 라이브러리 컴포넌트를 추가하세요 -->
    <vue-datepicker @input="selectDate" />
  </div>
</template>

<script setup>
import { defineEmits } from 'vue';

const emit = defineEmits(['select']);

const selectDate = (date) => {
  emit('select', date);
};
</script>

<style scoped>
.calendar {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
  background-color: #e0e0e0;
  border-radius: 10px;
}
</style>
