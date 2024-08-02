<template>
  <div class="video-profile">
    <div class="profile-cards">
      <div class="profile-card" v-for="n in 8" :key="n">
        <div class="profile-image">
          <img :src="getUserImage(n)" :alt="'User ' + n + '의 프로필 사진'" />
          <video :id="'video-' + n" autoplay playsinline></video>
        </div>
        <div class="profile-info">
          <h3>사용자 {{ n }}</h3>
          <button class="voice-chat-button" @click="toggleVoice(n)">
            <img :src="getVoiceIcon(n)" alt="음성 채팅" />
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, onBeforeUnmount } from 'vue';
import { useSessionStore } from '@/store/session';

const sessionStore = useSessionStore();

const toggleVoice = (userId) => {
  sessionStore.toggleVoiceChat(userId);
};

const getUserImage = (n) => {
  return require(`@/assets/images/ingame/user${n}.png`);
};

const getVoiceIcon = (userId) => {
  return sessionStore.voiceStates[userId - 1]
    ? require('@/assets/images/ingame/voice.png')
    : require('@/assets/images/ingame/voicex.png');
};

onMounted(() => {
  // 추가 초기화 코드가 필요한 경우 여기에 작성합니다.
});

onBeforeUnmount(() => {
  sessionStore.disconnect();
});
</script>

<style scoped>
.video-profile {
  color: white;
  border-radius: 10px;
  display: flex;
  height: 100%;
}

.profile-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(110px, 1fr));
  gap: 10px;
  width: 100%;
}

.profile-card {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.profile-image {
  width: 100%;
  padding-top: 100%;
  position: relative;
  overflow: hidden;
}

.profile-image img,
.profile-image video {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.profile-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: rgba(0, 0, 0, 0.5);
  width: 100%;
}

.profile-info h3 {
  margin: 0;
  font-size: 12px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.voice-chat-button {
  background: none;
  border: none;
  cursor: pointer;
  padding: 0;
}

.voice-chat-button img {
  width: 15px;
  height: 15px;
}
</style>
