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
        <Chatting class="chatting" />
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

const router = useRouter();
const route = useRoute();

const gm = ref({
  name: 'Game Master',
  profileImage: require('@/assets/images/users/gm.png'),
});

const isGM = ref(true);
const canStartGame = ref(false);

const players = ref([
  { id: 1, jobSelected: true },
  { id: 2, jobSelected: true },
  { id: 3, jobSelected: true },
  { id: 4, jobSelected: true },
]);

watch(players, (newPlayers) => {
  canStartGame.value = newPlayers.every(player => player.jobSelected);
});

const startGame = () => {
  if (isGM.value) {
    router.push(`/game/${route.params.roomId}/in-game`);
  }
};
</script>

<style scoped>
.character-sheet {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

.header {
  height: 10%;
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

.map-container {
  flex: 2; /* 가로의 2/3를 명시적으로 차지하도록 설정 */
  height: 100%;
  display: flex;
}

.map {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.right-section {
  width: 20%; /* 가로의 1/5을 명시적으로 차지하도록 설정 */
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.gm-section {
  flex: 1;
}

.chatting {
  flex: 3;
}

.column-left,
.column-right {
  display: flex;
  flex-direction: column;
  gap: 10px;
  flex: 1;
}

.token,
.dice,
.action,
.inventory {
  flex: 1;
  width: 100%;
}
</style>
