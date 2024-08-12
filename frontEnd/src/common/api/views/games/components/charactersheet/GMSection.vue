<template>
  <div class="gm-section-container">
    <div class="gm-info">
      <div class="gm-name-box">
        <img :src="gmBoxImage" alt="GM Box" class="gm-box-image" />
        <img :src="gmMaster" alt="GM Master" class="gm-master-image" />
        <div class="gm-details">
          <img :src="gmMarkImage" alt="GM Mark" class="gm-mark-image" />
          <p class="gm-name">{{ gm.name }}</p>
          <!-- GM 음성 채팅 버튼 추가 (닉네임 옆에) -->
          <VoiceChatButton :userId="gm.id" />
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
import VoiceChatButton from '../../VoiceChatButton.vue'; // VoiceChatButton 컴포넌트 임포트

const props = defineProps({
  gm: {
    type: Object,
    required: true,
  },
  isGM: {
    type: Boolean,
    required: true,
  },
});

const emit = defineEmits(['start-game']);

const startGame = () => {
  emit('start-game');
};

// 더미 데이터로 GM 정보 설정
const gmBoxImage = require('@/assets/images/character_sheet/gm_box.png');
const gmMarkImage = require('@/assets/images/character_sheet/gm_mark.png');
const startButtonImagePath = require('@/assets/images/room/start_button.png');
const gmMaster = require('@/assets/images/character_sheet/Game_Master.png');

// 테스트 용도의 더미 데이터
const gm = {
  id: 9, // GM의 고유 ID를 하드코딩으로 설정
  name: '미카엘', // GM의 닉네임
  profileImage: require('@/assets/images/users/gm.png'), // GM 프로필 이미지
};

console.log('GM ID:', gm.id); // GM ID 로그 출력
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

.gm-master-image {
  width: 60%;
  position: absolute;
  top: 10%;
  left: 10%;
  z-index: 1;
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
  gap: 10px; /* 닉네임과 음성 버튼 사이의 간격 설정 */
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
