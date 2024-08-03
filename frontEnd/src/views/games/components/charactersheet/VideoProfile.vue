<template>
  <div class="video-profile">
    <div class="profile-cards">
      <div class="profile-card" v-for="n in 8" :key="n">
        <div class="profile-image">
          <img :src="getUserImage(n)" :alt="'User ' + n + ' profile picture'" />
          <video :id="'video-' + n" autoplay playsinline></video>
        </div>
        <div class="profile-info">
          <h3>User {{ n }}</h3>
          <button class="voice-chat-button" @click="toggleVoiceChat(n)">
            <img :src="getVoiceIcon(n)" alt="Voice chat" />
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useSessionStore } from '@/store/session';

const sessionStore = useSessionStore();

const getUserImage = (n) => {
  return require(`@/assets/images/ingame/user${n}.png`);
};

const getVoiceIcon = (userId) => {
  return sessionStore.isVoiceOn(userId)
    ? require('@/assets/images/ingame/voice.png')
    : require('@/assets/images/ingame/voicex.png');
};

const toggleVoiceChat = (userId) => {
  sessionStore.toggleVoiceChat(userId);
};
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
