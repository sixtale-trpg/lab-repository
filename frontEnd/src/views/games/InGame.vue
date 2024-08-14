<template>
  <div class="character-sheet" :style="backgroundStyle">
    <Header class="header" />
    <div class="main-content">
      <div class="left-section">
        <!-- MainContent에 selectedMap을 prop으로 전달 -->
        <MainContent class="main-content-inner" :selectedMap="selectedMap" />
        <VideoProfile class="video-profile" />
      </div>
      <div class="right-section">
        <GMSection class="gm-section" :gm="gm" :isGM="isGM"  />
        <div class="log-and-chat">
          <Log class="log-section" />
          <Chatting class="chatting-section" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, computed } from 'vue';
import { useMapStore } from '@/store/map/mapStore'; // 맵 상태 관리
import Header from '@/views/games/components/ingame/Header.vue';
import MainContent from '@/views/games/components/ingame/MainContent.vue';
import VideoProfile from '@/views/games/components/ingame/VideoProfiles.vue';
import GMSection from '@/views/games/components/ingame/GMSection.vue';
import Chatting from '@/views/games/components/ingame/Chatting.vue';
import Log from '@/views/games/components/ingame/Log.vue';

// Import the background image
import MainBackground from '@/assets/images/maps/background/MainBackground.png';

// Pinia 스토어 인스턴스
const mapStore = useMapStore();

// 선택된 맵을 관리하는 로컬 상태
const selectedMap = ref(mapStore.selectedMap);

// Pinia의 selectedMap이 변경되면 로컬 상태도 업데이트
watch(() => mapStore.selectedMap, (newMap) => {
  selectedMap.value = newMap;
});

const gm = ref({
  name: '미카엘'
});

const isGM = ref(true);

// Computed property for the background style
const backgroundStyle = computed(() => ({
  backgroundImage: `url(${MainBackground})`,
  backgroundSize: 'cover',
  backgroundPosition: 'center',
  backgroundRepeat: 'no-repeat',
}));
</script>

<style scoped>
.character-sheet {
  display: flex;
  flex-direction: column;
  overflow: hidden;
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
