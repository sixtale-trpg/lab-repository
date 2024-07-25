<template>
  <div class="gm-section-container">
    <div class="gm-box">
      <p>Game Master</p>
    </div>
    <div class="gm-name-box">
      <p>{{ gm.name }}</p>
    </div>
  </div>
</template>

<script setup>
import { defineProps } from 'vue';

const props = defineProps({
  gm: {
    type: Object,
    required: true
  },
  isGM: {
    type: Boolean,
    required: true
  },
  canStartGame: {
    type: Boolean,
    default: false
  }
});

const startGame = () => {
  console.log('게임 시작');
  // 게임 시작 로직
};
</script>

<style scoped>
.gm-section-container {
  display: flex;
  align-items: center;
  height: 100%;
  background-color: #291707;
  border: 1px solid #5a4d41;
  border-radius: 10px;
  padding: 10px;
  box-sizing: border-box;
}

.gm-box {
  flex: 1;
  background-color: #564307;
  text-align: center;
  padding: 5px;
  border-radius: 5px;
  color: #ffffff;
  margin-right: 10px;
}

.gm-name-box {
  flex: 1;
  background-color: #564307;
  text-align: center;
  padding: 5px;
  border-radius: 5px;
  color: #ffffff;
}
</style>
