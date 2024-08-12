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
          <!-- 여기서 VoiceChatButton 컴포넌트를 사용합니다 -->
          <VoiceChatButton :userId="n" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import VoiceChatButton from '../../VoiceChatButton.vue';

// 사용자 ID에 따라 사용자 이미지를 가져오는 함수
const getUserImage = (n) => {
  return require(`@/assets/images/ingame/user${n}.png`);
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
  /* padding: 5px; */
  background-color: rgba(83, 78, 78, 0.5);
  width: 100%;
}

.profile-info h3 {
  margin: 0;
  font-size: 12px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
