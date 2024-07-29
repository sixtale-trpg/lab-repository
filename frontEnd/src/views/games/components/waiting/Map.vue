<template>
  <div :style="mapViewStyle" class="map-view">
    <div class="map-controls" v-if="isGM">
      <img :src="previousIcon" alt="Previous" @click="previousMap" />
      <img :src="nextIcon" alt="Next" @click="nextMap" />
    </div>
    <img :src="currentImage" alt="Map or Game" class="map-image" />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';

// 이미지 경로를 설정합니다.
const currentMap = ref(require('@/assets/images/maps/map1.png'));
const maps = [
  require('@/assets/images/maps/map1.png'),
  require('@/assets/images/maps/map2.png'),
  require('@/assets/images/maps/map3.png'),
];
const gameImage = require('@/assets/images/room/gameImage.png');
const previousIcon = require('@/assets/images/room/previous.png');
const nextIcon = require('@/assets/images/room/next.png');
const mapBackground = require('@/assets/images/room/map_background2.png');

let currentMapIndex = 0;

const previousMap = () => {
  if (currentMapIndex > 0) {
    currentMapIndex -= 1;
  } else {
    currentMapIndex = maps.length - 1;
  }
  currentMap.value = maps[currentMapIndex];
};

const nextMap = () => {
  if (currentMapIndex < maps.length - 1) {
    currentMapIndex += 1;
  } else {
    currentMapIndex = 0;
  }
  currentMap.value = maps[currentMapIndex];
};

// GM 여부 확인
const isGM = ref(true); // GM 여부를 확인할 수 있는 논리값

// 배경 이미지 스타일 설정
const mapViewStyle = {
  position: 'relative',
  height: '80%',
  backgroundImage: `url(${mapBackground})`,
  backgroundColor: 'transparent', // 배경색 투명으로 설정
  borderRadius: '10px',
  width: '70%',
  boxSizing: 'border-box',
  backgroundSize: 'cover',
  backgroundPosition: 'center',
  backgroundRepeat: 'no-repeat',
};

const currentImage = computed(() => {
  return isGM.value ? currentMap.value : gameImage;
});
</script>

<style scoped>
.map-view {
  position: relative;
  height: 80%; 
  border-radius: 10px;
  width: 70%; 
  box-sizing: border-box; 
}

.map-controls {
  display: flex;
  justify-content: space-between;
  position: absolute;
  top: 50%; 
  left: 5px;
  right: 5px;
  transform: translateY(-50%); 
}

.map-controls img {
  width: 20px;
  height: 20px;
  cursor: pointer;
}

.map-image {
  width: 100%;
  height: 100%;
  border-radius: 30px;
  padding: 15px;
}
</style>
