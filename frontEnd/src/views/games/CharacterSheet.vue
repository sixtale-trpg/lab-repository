<template>
  <div class="character-sheet">
    <Header class="header" />
    <div class="main-content">
      <div class="left-section">
        <JobBoard class="job-card" />
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
import { ref, watch, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import Header from '@/views/games/components/charactersheet/Header.vue';
import JobCard from '@/views/games/components/charactersheet/JobCard.vue';
import VideoProfile from '@/views/games/components/charactersheet/VideoProfile.vue';
import GMSection from '@/views/games/components/charactersheet/GMSection.vue';
import Chatting from '@/views/games/components/charactersheet/Chatting.vue';
import JobBoard from './components/charactersheet/JobBoard.vue';

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
  display: grid;
  grid-template-columns: 1fr 20%;
  gap: 10px;
  height: 90%;
  padding: 10px;
  box-sizing: border-box;
}

.left-section {
  display: flex;
  flex-direction: column;
  gap: 10px;
  flex: 1;
}

.right-section {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  flex-basis: 20%;
}

.job-card {
  flex: 3;
}

.video-profile {
  flex: 1;
}

.gm-section {
  flex: 1;
}

.chatting {
  flex: 3;
}
</style>
