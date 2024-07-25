<template>
  <div class="gm-section-container">
    <div class="gm-info">
      <div class="gm-header">
        <h3>Game Master</h3>
      </div>
      <div class="gm-name-box">
        <p>{{ gm.name }}</p>
      </div>
      <button class="start-game-button" :disabled="!isGM" @click="startGame">게임 시작</button>
    </div>
    <div class="gm-profile">
      <img :src="gm.profileImage" alt="GM Profile" class="profile-image" />
    </div>
  </div>
</template>

<script setup>

const props = defineProps({
  gm: {
    type: Object,
    required: true
  },
  isGM: {
    type: Boolean,
    required: true
  }
});

const emit = defineEmits(['start-game']);

const startGame = () => {
  emit('start-game');
};
</script>

<style scoped>
.gm-section-container {
  display: flex;
  height: 100%;
}

.gm-profile {
  flex: 7;
  display: flex;
  justify-content: center;
  align-items: center;
}

.profile-image {
  width: 100%;
  height: auto;
  border-radius: 10px;
}

.gm-info {
  flex: 5;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-around;
  background-color: #291707;
  border: 1px solid #5a4d41;
  border-radius: 10px;
  padding: 10px;
  box-sizing: border-box;
}

.gm-header {
  background-color: #564307;
  width: 100%;
  text-align: center;
  padding: 5px;
  border-radius: 5px;
  color: #ffffff;
}

.gm-name-box {
  background-color: #564307;
  width: 100%;
  text-align: center;
  padding: 10px;
  border-radius: 5px;
  color: #ffffff;
  border: 1px solid #5a4d41;
}

.start-game-button {
  width: 100%;
  padding: 10px;
  background-color: #073467;
  color: white;
  border: 1px solid white;
  border-radius: 5px;
  cursor: pointer;
}

.start-game-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style>
