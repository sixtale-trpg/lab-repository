<template>
  <div class="gm-section-container">
    <div class="gm-info">
      <div class="gm-name-box">
        <img :src="gmBoxImage" alt="GM Box" class="gm-box-image" />
        <div class="gm-details">
          <img :src="gmMarkImage" alt="GM Mark" class="gm-mark-image" />
          <p class="gm-name">{{ gm.name }}</p>
        </div>
      </div>
      <button class="start-game-button" :disabled="!isGM" @click="startGame">
        <img :src="startButtonImagePath" alt="Start Game" class="start-button-image" />
        <span class="start-button-text">게임 시작</span>
      </button>
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

const gmBoxImage = require('@/assets/images/character_sheet/gm_box.png');
const gmMarkImage = require('@/assets/images/character_sheet/gm_mark.png');
const startButtonImagePath = require('@/assets/images/room/start_button.png');
</script>

<style scoped>
.gm-section-container {
  display: flex;
  height: 100%;
}

.gm-profile {
  flex: 3;
  display: flex;
  justify-content: center;
  align-items: center;
}

.profile-image {
  width: 80%;
  height: auto;
  border-radius: 10px;
}

.gm-info {
  flex: 3;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.gm-name-box {
  position: relative;
  width: 100%;
  text-align: center;
  margin-bottom: 10px;
}

.gm-box-image {
  width: 100%;
}

.gm-details {
  position: absolute;
  width: 100%;
  top: 70%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
}

.gm-mark-image {
  width: 13%;
  height: 13%;
  margin: 3%;
}

.gm-name {
  font-size: 0.8rem;
  margin: 0;
}

.start-game-button {
  width: 100%;
  padding: 0;
  background: none;
  border: none;
  cursor: pointer;
  position: relative;
  margin-top: 2%;
}

.start-game-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.start-button-image {
  width: 100%;
  height: auto;
}

.start-button-text {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: white;
  font-size: 0.8rem;
  font-weight: bold;
}
</style>
