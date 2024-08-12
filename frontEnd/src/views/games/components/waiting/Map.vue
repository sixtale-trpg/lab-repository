<template>
  <div :style="mapViewStyle" class="map-view">
    <div class="map-controls" v-if="isGM">
      <img :src="previousIcon" alt="Previous" @click="previousMap" style="width: 40px; height: auto;"/>
      <img :src="nextIcon" alt="Next" @click="nextMap" style="width: 40px; height: auto;" />
    </div>
    <img :src="currentImage" alt="Map or Game" class="map-image" />
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue';

// Props 받아오기
const props = defineProps({
  roomId: {
    type: String,
    required: true,
  },
  mapList: {
    type: Array,
    required: true,
  },
  isGM: {
    type: Boolean,
    required: true,
  },
});
<<<<<<< HEAD
=======

>>>>>>> 3ba9fd659e87ab4a0bb54241aacb034372ee1eb2

const previousIcon = require('@/assets/images/room/previous.png');
const nextIcon = require('@/assets/images/room/next.png');

// 상태 변수 정의
const currentMap = ref(null);
let currentMapIndex = 0;

// MapList에 따라 currentMap 설정
watch(
  () => props.mapList,
  (newMapList) => {
    if (newMapList && newMapList.length > 0) {
      currentMap.value = newMapList[0];
    }
  },
  { immediate: true }
);

// 맵 전환 함수
const previousMap = () => {
  if (currentMapIndex > 0) {
    currentMapIndex -= 1;
  } else {
    currentMapIndex = props.mapList.length - 1;
  }
  currentMap.value = props.mapList[currentMapIndex];
};

const nextMap = () => {
  if (currentMapIndex < props.mapList.length - 1) {
    currentMapIndex += 1;
  } else {
    currentMapIndex = 0;
  }
  currentMap.value = props.mapList[currentMapIndex];
};

// 배경 이미지 스타일 설정
const mapViewStyle = computed(() => ({
  position: 'relative',
  height: '80%',
  backgroundImage: currentMap.value && currentMap.value.bgmURL ? `url(${currentMap.value.bgmURL})` : '',
  backgroundColor: 'transparent',
  borderRadius: '10px',
  width: '70%',
  boxSizing: 'border-box',
  backgroundSize: 'cover',
  backgroundPosition: 'center',
  backgroundRepeat: 'no-repeat',
}));

const currentImage = computed(() => {
  console.log('Current Map Image URL:', currentMap.value ? currentMap.value.imageURL : 'No Map Selected');
  return currentMap.value ? currentMap.value.imageURL : require('@/assets/images/room/gameImage.png');
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
  top: 47%; /* 맵 이미지 안쪽으로 조금 더 내려서 배치 */
  left: 20px; /* 맵 이미지 안쪽으로 더 넣기 위해 left값 조정 */
  right: 20px; /* 맵 이미지 안쪽으로 더 넣기 위해 right값 조정 */
  transform: translateY(-50%);
}

.map-controls img {
  width: 40px; /* 아이콘 크기 조정 */
  height: auto;
  cursor: pointer;
}

.map-image {
  width: 100%;
  height: 100%;
  border-radius: 30px;
  padding: 15px;
}
</style>
