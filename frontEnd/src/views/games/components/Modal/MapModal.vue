<template>
  <div class="modal-overlay" @click.self="close">
    <div class="modal-content">
      <div class="modal-header">
        <h2>시나리오 맵 목록</h2>
      </div>
      <div class="modal-body">
        <button class="arrow left-arrow" @click="prevMap">◀</button>
        <div class="map-display">
          <img :src="maps[currentMapIndex].image" :alt="maps[currentMapIndex].name" />
          <div class="map-description">
            <h3>{{ maps[currentMapIndex].name }}</h3>
            <p>{{ maps[currentMapIndex].description }}</p>
          </div>
        </div>
        <button class="arrow right-arrow" @click="nextMap">▶</button>
      </div>
      <button class="close-button" @click="close">Close</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const props = defineProps({
  isOpen: {
    type: Boolean,
    default: false,
  },
});

const emit = defineEmits(['close']);

const maps = ref([
  { id: 1, name: 'Map 1', image: require('@/assets/images/maps/map1.png'), description: 'This is the first map.' },
  { id: 2, name: 'Map 2', image: require('@/assets/images/maps/map2.png'), description: 'This is the second map.' },
  { id: 3, name: 'Map 3', image: require('@/assets/images/maps/map3.png'), description: 'This is the third map.' },
]);

const currentMapIndex = ref(0);

const prevMap = () => {
  if (currentMapIndex.value > 0) {
    currentMapIndex.value--;
  }
};

const nextMap = () => {
  if (currentMapIndex.value < maps.value.length - 1) {
    currentMapIndex.value++;
  }
};

const close = () => {
  emit('close');
};

// onMounted(() => {
//   // 실제 백엔드 요청 예제
//   axios.get('/api/scenario/maps', { params: { scenarioId: props.scenarioId } })
//     .then(response => {
//       maps.value = response.data;
//     })
//     .catch(error => {
//       console.error('Error fetching maps:', error);
//     });
// });
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000; /* 모달 오버레이의 z-index를 높게 설정 */
}

.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  width: 80%;
  max-width: 800px;
  position: relative;
  z-index: 1001; /* 모달 내용의 z-index를 높게 설정 */
}

.modal-header {
  background-color: #4b3a29; /* 헤더 배경색 */
  color: white; /* 텍스트 색상 */
  padding: 10px;
  text-align: center;
  border-radius: 8px 8px 0 0;
}

.modal-body {
  display: flex;
  align-items: center;
  padding: 20px;
}

.arrow {
  background: none;
  border: none;
  font-size: 2rem;
  cursor: pointer;
}

.map-display {
  display: flex;
  flex-direction: column;
  align-items: center;
  flex: 1;
}

.map-display img {
  width: 100%;
  height: auto;
  max-height: 400px;
  border-radius: 8px;
}

.map-description {
  margin-top: 10px;
  text-align: center;
}

.close-button {
  background-color: #4b3a29;
  color: white;
  border: none;
  padding: 10px 20px;
  cursor: pointer;
  border-radius: 4px;
  position: absolute;
  bottom: 20px;
  right: 20px;
}
</style>
