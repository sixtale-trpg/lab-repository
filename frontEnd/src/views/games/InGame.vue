<template>
  <div class="character-sheet">
    <Header class="header" />
    <div class="main-content">
      <div class="left-section">
        <MainContent class="main-content-inner" />
        <VideoProfile class="video-profile" />
      </div>
      <div class="right-section">
        <GMSection class="gm-section" :gm="gm" :isGM="isGM" @start-game="startGame" />
        <div class="log-and-chat">
          <Log class="log-section" />
          <Chatting class="chatting-section" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import Header from '@/views/games/components/ingame/Header.vue';
import MainContent from '@/views/games/components/ingame/MainContent.vue';
import VideoProfile from '@/views/games/components/ingame/VideoProfiles.vue';
import GMSection from '@/views/games/components/ingame/GMSection.vue';
import Chatting from '@/views/games/components/ingame/Chatting.vue';
import Log from '@/views/games/components/ingame/Log.vue';

const router = useRouter();
const route = useRoute();

const gm = ref({
  name: '미카엘'
});

const isGM = ref(true);

</script>

<style scoped>
.character-sheet {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

.header {
  height: 70%;
  width: 100%;
}

.main-content {
  display: flex;
  height: 90%;
  padding: 10px;
  box-sizing: border-box;
}

.left-section {
  display: flex;
  flex-direction: column;
  width: 80%;
  gap: 10px;
  height: 100%;
}

.video-profile {
  height: 25%;
}

.main-content-inner {
  height: 75%;
  display: flex;
  box-sizing: border-box;
}

.right-section {
  width: 20%;
  display: flex;
  flex-direction: column;
  margin-left: 10px;
  padding: 3px;
  justify-content: flex-start;
}

.gm-section {
  height: 6%;
}

.log-and-chat {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.log-section {
  flex: 2; /* 2:3 비율 */
}

.chatting-section {
  flex: 3; /* 2:3 비율 */
}
</style>