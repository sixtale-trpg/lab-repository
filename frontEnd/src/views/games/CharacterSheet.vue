<template>
  <div class="character-sheet" :style="backgroundStyle">
    <Header class="header" />
    <div class="main-content">
      <div class="left-section">
        <JobBoard class="job-board" />
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

const backgroundImage = require('@/assets/images/character_sheet/MainBackground.png');
const backgroundStyle = ref({
  backgroundImage: `url(${backgroundImage})`,
  backgroundSize: 'cover',
  backgroundPosition: 'center'
});
</script>

<style scoped>
.character-sheet {
  display: flex;
  flex-direction: column;
  height: 100vh;
  width: 100vw;
  overflow: hidden;
}

.header {
  height: 10vh;
  width: 100%;
}

.main-content {
  display: flex;
  height: 90vh;
  width: 100%;
}

.left-section {
  display: flex;
  flex-direction: column;
  width: 80%;
  height: 100%;
  padding: 1vh;
  box-sizing: border-box;
}

.right-section {
  display: flex;
  flex-direction: column;
  width: 20%;
  height: 100%;
  padding: 1vh;
  box-sizing: border-box;
}

.job-board {
  height: 75%;
  margin-bottom: 1vh;
}

.video-profile {
  height: 25%;
}

.gm-section {
  height: 25%;
  margin-bottom: 1vh;
}

.chatting {
  height: 75%;
}
</style>
