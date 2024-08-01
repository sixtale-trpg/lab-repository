<template>
  <div class="map-section-container" @dragover.prevent @drop="onDrop">
    <img :src="mapImage" alt="Map" class="map-image" />
    <div ref="rendererContainer" class="renderer-container"></div>
    <div
      v-for="token in placedTokens"
      :key="token.id"
      class="token"
      :style="{ left: token.x + 'px', top: token.y + 'px' }"
      @mousedown="startDrag(token, $event)"
      @dblclick="returnToken(token)"
    >
      <img :src="tokenImage" :alt="token.name" />
    </div>
    <div v-if="showGrid" class="grid-overlay">
      <div v-for="row in gridRows" :key="row" class="grid-row">
        <div
          v-for="col in gridCols"
          :key="col"
          class="grid-cell"
          @mouseenter="showDescription(row, col)"
          @mouseleave="hideDescription"
        >
          <div v-if="isLaserActive(row, col)" class="laser-effect"></div>
        </div>
      </div>
    </div>
    <div class="info-panel" v-if="hoveredDescription.title">
      <img class="info-background" :src="infoBackground" alt="Information Background" />
      <div class="info-content">
        <h3>{{ hoveredDescription.title }}</h3>
        <p>{{ hoveredDescription.details }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue';
import ThreeJSManager from '@/common/lib/ThreeJSManager';
import eventBus from '@/common/lib/eventBus.js';

const rendererContainer = ref(null);
let threeJSManager = null;

const tokenImage = require('@/assets/images/ingame/Token.png');
const mapImage = require('@/assets/images/maps/map1.png');
const infoBackground = require('@/assets/images/hover/token_hover.png');
const placedTokens = ref([]);
const showGrid = ref(true);
const gridSize = 50;

// 창 크기와 그리드 크기를 기반으로 그리드 행과 열을 계산합니다.
const gridRows = computed(() => Array.from({ length: Math.ceil(window.innerHeight / gridSize) }, (_, i) => i));
const gridCols = computed(() => Array.from({ length: Math.ceil(window.innerWidth / gridSize) }, (_, i) => i));

// 하드코딩된 덤프 데이터로 활성 레이저를 설정합니다.
const activeLasers = ref(new Set(['2-3', '4-5', '1-1', '6-7']));
const hoveredDescription = ref({ title: '', details: '' }); // 현재 마우스가 올려진 그리드 셀의 설명을 저장합니다.

// 마우스를 올린 그리드 셀에 대한 설명을 표시합니다.
const showDescription = (row, col) => {
  hoveredDescription.value = getDescription(row, col);
};

// 그리드 셀에서 마우스가 벗어나면 설명을 숨깁니다.
const hideDescription = () => {
  hoveredDescription.value = { title: '', details: '' };
};

// 행과 열에 따라 각 그리드 셀에 대한 설명 세부정보를 제공합니다.
const getDescription = (row, col) => {
  const descriptions = {
    '2-3': {
      title: '만티코어',
      details: '외톨이. 큼. 인공물\n독침 (1d10+1 피해, 관통 1) HP 16 장갑 3\n한걸음, 몇걸음, 파괴적\n특기사항: 날개.\n\n키마이라가 흑마술로 가는 첫 걸음이라면, 만티코어는 한 번 열면 닫을 수 없는 문입니다. 사자와 전갈, 비룡을 섞은 모습을 하고 있지만, 가장 특징적인 것은 그 얼굴입니다. 만티코어를 만드는 데에는 의학이 가득한 인간의 얼굴이 필요합니다. 이를 위해서는 너희 도시와 상관없이 인간을 잡아다가 사체의 일부를 병의 동물의 몸과 연결해야 합니다. 일반 만티코어에게 남아 있는 사람으로서의 기억은 없지만, 그 고통만은 남는다고 합니다. 그래서 그런지 만티코어는 성질이 극도로 사납고 잔인합니다.\n\n본능: 죽이다.\n- 독침으로 쏜다.\n- 무언가를 찢어발긴다.'
    },
    '4-5': {
      title: '다른 이벤트',
      details: '이 이벤트에 대한 설명입니다.'
    }
    // 필요한 설명을 추가로 넣습니다.
  };

  return descriptions[`${row}-${col}`] || { title: '', details: '' };
};

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
    console.error("유효하지 않은 JSON 데이터:", tokenData);
  }
};

const deleteTokenFromMap = (token) => {
  placedTokens.value = placedTokens.value.filter(t => t.id !== token.id);
};

const returnToken = (token) => {
  deleteTokenFromMap(token);
  window.dispatchEvent(new CustomEvent('add-token-to-list', { detail: token }));
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

    draggingToken.x = newX;
    draggingToken.y = newY;
  }
};

const stopDrag = () => {
  draggingToken = null;
  document.removeEventListener('mousemove', onDrag);
  document.removeEventListener('mouseup', stopDrag);
};

const handleRollDice = (diceTypesToRoll) => {
  console.log('주사위 굴림 이벤트를 받았습니다:', diceTypesToRoll);
  if (threeJSManager) {
    threeJSManager.rollDice(diceTypesToRoll).then(results => {
      results.forEach(result => console.log(`${result.type}면체 주사위 결과: ${result.value}`));
    });
  }
};

const isLaserActive = (row, col) => {
  return activeLasers.value.has(`${row}-${col}`);
};

onMounted(() => {
  threeJSManager = new ThreeJSManager(rendererContainer.value);
  eventBus.on('roll-dice', handleRollDice);
  window.addEventListener('toggle-grid', (event) => {
    showGrid.value = event.detail;
  });

  window.addEventListener('delete-token', (event) => {
    deleteTokenFromMap(event.detail);
  });

  // 초기 이벤트 좌표 설정
  activeLasers.value = new Set(['2-3', '4-5', '1-1', '6-7']);
});

onUnmounted(() => {
  eventBus.off('roll-dice', handleRollDice);
  window.removeEventListener('toggle-grid', () => {});
  window.removeEventListener('delete-token', () => {});
});
</script>

<style scoped>
.map-section-container {
  width: 100%;
  height: 100%;
  margin: 5px;
  position: relative;
  overflow: hidden;
}

.renderer-container {
  width: 100%;
  height: 80vh;
  background-color: transparent;
  position: absolute;
  top: 0;
  left: 0;
  z-index: 1;
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
  z-index: 3; /* 토큰이 맵의 최상위에 위치하도록 설정 */
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
  z-index: 2;
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
  position: relative;
  overflow: visible; /* 툴팁이 보이도록 설정 */
}

.laser-effect {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background-color: red;
  box-shadow: 0 0 10px 5px red;
  animation: pulse 1s infinite;
}

.info-panel {
  position: absolute;
  bottom: 20px;
  right: 20px;
  width: 320px;
  background-color: rgba(0, 0, 0, 0.9);
  color: white;
  border-radius: 8px;
  padding: 15px;
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.7);
  z-index: 10;
  transition: opacity 0.3s ease;
}

.info-background {
  width: 100%;
  height: 150px;
  object-fit: cover;
  margin-bottom: 10px;
  border-radius: 5px;
}

.info-content h3 {
  margin: 0;
  font-size: 20px;
  font-weight: bold;
}

.info-content p {
  font-size: 14px;
  line-height: 1.6;
}

@keyframes pulse {
  0% {
    transform: scale(1);
    box-shadow: 0 0 10px 5px red;
  }
  50% {
    transform: scale(1.2);
    box-shadow: 0 0 15px 10px red;
  }
  100% {
    transform: scale(1);
    box-shadow: 0 0 10px 5px red;
  }
}
</style>
