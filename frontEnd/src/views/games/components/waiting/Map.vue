<template>
  <div class="map-view">
    <div class="map-controls">
      <img src="@/assets/images/room/previous.png" alt="Previous" @click="previousMap" />
      <img src="@/assets/images/room/next.png" alt="Next" @click="nextMap" />
    </div>
    <img :src="currentMap" alt="Map" class="map-image" />
  </div>
</template>

<script setup>
import { ref } from 'vue';

const currentMap = ref(require('@/assets/images/maps/map1.png'));
const maps = [
  require('@/assets/images/maps/map1.png'),
  require('@/assets/images/maps/map2.png'),
  require('@/assets/images/maps/map3.png'),
];
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
</script>

<style scoped>
.map-view {
  position: relative;
  height: 80%; 
  background-color: #f5f5f5;
  border: 1px solid #ccc;
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
  border-radius: 10px;
}
</style>
