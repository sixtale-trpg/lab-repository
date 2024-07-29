<template>
  <div class="map-section-container" @dragover.prevent @drop="onDrop">
    <img :src="mapImage" alt="Map" class="map-image" />
    <div 
      v-for="token in placedTokens" 
      :key="token.id" 
      class="token" 
      :style="{ left: token.x + 'px', top: token.y + 'px' }"
      @mousedown="startDrag(token, $event)"
    >
      <img :src="tokenImage" :alt="token.name" />
    </div>
    <div v-if="showGrid" class="grid-overlay">
      <div v-for="row in gridRows" :key="row" class="grid-row">
        <div v-for="col in gridCols" :key="col" class="grid-cell">
          {{ col }}, {{ row }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue';

const tokenImage = require('@/assets/images/ingame/Token.png');
const mapImage = require('@/assets/images/maps/map1.png');
const placedTokens = ref([]);
const showGrid = ref(true);
const gridSize = 50;

const gridRows = computed(() => Array.from({ length: Math.ceil(window.innerHeight / gridSize) }, (_, i) => i));
const gridCols = computed(() => Array.from({ length: Math.ceil(window.innerWidth / gridSize) }, (_, i) => i));

const onDrop = (event) => {
  event.preventDefault();
  
  const tokenData = event.dataTransfer.getData('text/plain');
  try {
    const parsedToken = JSON.parse(tokenData);
    if (!placedTokens.value.find(t => t.id === parsedToken.id)) {
      const mapRect = event.currentTarget.getBoundingClientRect();
      placedTokens.value.push({
        ...parsedToken,
        x: event.clientX - mapRect.left,
        y: event.clientY - mapRect.top
      });

      const removeEvent = new CustomEvent('remove-token-from-list', { detail: parsedToken });
      window.dispatchEvent(removeEvent);
    }
  } catch (error) {
    console.error("Invalid JSON data:", tokenData);
  }
};

const deleteTokenFromMap = (token) => {
  placedTokens.value = placedTokens.value.filter(t => t.id !== token.id);
};

let draggingToken = null;
let offsetX = 0;
let offsetY = 0;

const startDrag = (token, event) => {
  draggingToken = token;
  const tokenRect = event.target.getBoundingClientRect();
  offsetX = event.clientX - tokenRect.left;
  offsetY = event.clientY - tokenRect.top;
  document.addEventListener('mousemove', onDrag);
  document.addEventListener('mouseup', stopDrag);
};

const onDrag = (event) => {
  if (draggingToken) {
    const mapRect = document.querySelector('.map-section-container').getBoundingClientRect();
    const newX = event.clientX - mapRect.left - offsetX;
    const newY = event.clientY - mapRect.top - offsetY;
    
    // 토큰이 맵 밖으로 드래그되었을 때 위치 업데이트
    draggingToken.x = newX;
    draggingToken.y = newY;
  }
};

const stopDrag = () => {
  draggingToken = null;
  document.removeEventListener('mousemove', onDrag);
  document.removeEventListener('mouseup', stopDrag);
};

onMounted(() => {
  window.addEventListener('toggle-grid', (event) => {
    showGrid.value = event.detail;
  });

  window.addEventListener('delete-token', (event) => {
    deleteTokenFromMap(event.detail);
  });
});

onUnmounted(() => {
  window.removeEventListener('toggle-grid', () => {});
  window.removeEventListener('delete-token', () => {});
});
</script>

<style scoped>
.map-section-container {
  width: 100%;
  height: 100%;
  position: relative;
  overflow: hidden;
}

.map-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.token {
  position: absolute;
  width: 40px;
  height: 40px;
  cursor: move;
}

.token img {
  width: 100%;
  height: 100%;
}

.grid-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: grid;
  pointer-events: none;
}

.grid-row {
  display: flex;
}

.grid-cell {
  width: 50px;
  height: 50px;
  border: 1px solid rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 12px;
  color: rgba(0, 0, 0, 0.6);
}
</style>
