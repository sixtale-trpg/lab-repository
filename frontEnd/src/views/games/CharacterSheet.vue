<template>
  <div class="character-sheet" :style="backgroundStyle">
    <Header class="header" />
    <div class="main-content">
      <div class="left-section">
        <JobBoard class="job-board" />
        <VideoProfile class="video-profile" />
      </div>
      <div class="right-section">
        <GMSection
          class="gm-section"
          :gm="gm"
          :isGM="isGM"
          @start-game="startGame"
        />
        <Chatting class="chatting" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref, watch, onBeforeUnmount } from "vue";
import { useRouter, useRoute } from "vue-router";
import Header from "@/views/games/components/charactersheet/Header.vue";
import JobCard from "@/views/games/components/charactersheet/JobCard.vue";
import VideoProfile from "@/views/games/components/charactersheet/VideoProfile.vue";
import GMSection from "@/views/games/components/charactersheet/GMSection.vue";
import Chatting from "@/views/games/components/charactersheet/Chatting.vue";
import JobBoard from "./components/charactersheet/JobBoard.vue";
import GameLogWebSocketService from "@/store/websocket/gameLog"; // GameLogWebSocket 서비스 가져오기

const router = useRouter();
const route = useRoute();

const gm = ref({
  name: "GM닉네임입니다",
  profileImage: require("@/assets/images/users/gm.png"),
});

const isGM = ref(true);
const canStartGame = ref(false);

const players = ref([
  { id: 1, jobSelected: true },
  { id: 2, jobSelected: true },
  { id: 3, jobSelected: true },
  { id: 4, jobSelected: true },
]);

onMounted(() => {
  GameLogWebSocketService.connect(route.params.roomId);

  GameLogWebSocketService.onMessageReceived("GAME_START", (message) => {
    console.log("Start Game message received:", message);

    router.push(`/game/${route.params.roomId}/in-game`);
  });
});

watch(players, (newPlayers) => {
  canStartGame.value = newPlayers.every((player) => player.jobSelected);
});

onBeforeUnmount(() => {
  // WebSocket 연결 해제
  GameLogWebSocketService.disconnect();
});

const startGame = () => {
  // GameLogWebSocketService.sendMessage({
  //   roomID: route.params.roomId,
  //   gameType: "GAME_START",
  // });
  // if (isGM.value) {
  //   router.push(`/game/${route.params.roomId}/in-game`);
  // }
};

const backgroundImage = require("@/assets/images/character_sheet/MainBackground.png");
const backgroundStyle = ref({
  backgroundImage: `url(${backgroundImage})`,
  backgroundSize: "cover",
  backgroundPosition: "center",
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
  height: 73%;
  margin-bottom: 1vh;
}

.video-profile {
  height: 27%;
}

.gm-section {
  height: 25%;
  margin-bottom: 1vh;
}

.chatting {
  height: 75%;
}
</style>
