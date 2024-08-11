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
import { ref, computed, onMounted } from 'vue';
import { getMapList } from '@/common/api/RoomsAPI';

// roomId를 props로 받아옵니다.
const props = defineProps({
  roomId: {
    type: String,
    required: true,
  },
  isGM: {
    type: Boolean,
    required: true,
  },
});

// 상태 변수 정의
const currentMap = ref(null);
const maps = ref([]);
let currentMapIndex = 0;

// API를 통해 맵 목록을 가져오는 함수
const fetchMapList = async () => {
  try {
    const fetchedMaps = await getMapList(props.roomId);
    console.log('Fetched maps:', fetchedMaps);
    maps.value = fetchedMaps;
    if (fetchedMaps.length > 0) {
      currentMap.value = fetchedMaps[0];
    }
  } catch (error) {
    console.error('Error fetching map list:', error);
  }
};

// 컴포넌트가 마운트될 때 맵 목록을 가져옴
onMounted(() => {
  if (props.roomId) {
    fetchMapList();
  }
});

// 맵 전환 함수
const previousMap = () => {
  if (currentMapIndex > 0) {
    currentMapIndex -= 1;
  } else {
    currentMapIndex = maps.value.length - 1;
  }
  currentMap.value = maps.value[currentMapIndex];
};

const nextMap = () => {
  if (currentMapIndex < maps.value.length - 1) {
    currentMapIndex += 1;
  } else {
    currentMapIndex = 0;
  }
  currentMap.value = maps.value[currentMapIndex];
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
  return props.isGM && currentMap.value ? currentMap.value.imageURL : require('@/assets/images/room/gameImage.png');
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
