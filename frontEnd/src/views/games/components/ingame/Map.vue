<template>
  <div class="map-section-container" @dragover.prevent @drop="onDrop">
    <!-- 디버그용 버튼 최상단에 위치 -->
    <!-- <button class="debug-button" @click="logSelectedMap">Check selectedMap</button> -->

    <!-- selectedMap prop을 사용하여 이미지 렌더링 -->
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
          <div
            v-if="isLaserActive(row, col)"
            class="laser-effect"
            @mouseenter="onLaserMouseEnter(row, col)"
            @mouseleave="onLaserMouseLeave(row, col)"
            @click="openModal"
          ></div>
        </div>
      </div>
    </div>
    <div class="info-panel" v-if="hoveredDescription.title">
      <img
        class="info-background"
        :src="infoBackground"
        alt="Information Background"
      />
      <div class="info-content">
        <h3>{{ hoveredDescription.title }}</h3>
        <p>{{ hoveredDescription.details }}</p>
      </div>
    </div>
    <!-- 레이저 설명을 위한 툴팁 컨테이너 -->
    <div
      v-if="hoveredLaserDescription && hoveredLaserDescription.title"
      class="tooltip"
      :style="{ top: tooltipPosition.y + 'px', left: tooltipPosition.x + 'px' }"
    >
      <img
        v-if="hoveredLaserDescription.image"
        :src="hoveredLaserDescription.image"
        class="tooltip-image"
        alt="Tooltip Image"
      />
      <h4>{{ hoveredLaserDescription.title }}</h4>
      <p>{{ hoveredLaserDescription.details }}</p>
    </div>

    <!-- Bootstrap Modal -->
    <div
      class="modal fade"
      id="eventModal"
      tabindex="-1"
      aria-labelledby="eventModalLabel"
      aria-hidden="true"
    >
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content custom-modal" :style="modalStyle">
          <div class="modal-header custom-modal-header">
            <img :src="titleImage" alt="Background" class="modal-title-image" />
            <div class="overlay-text">맵 변경</div>
            <button
              type="button"
              class="btn-close"
              data-bs-dismiss="modal"
              aria-label="Close"
            ></button>
          </div>
          <div class="modal-body">
            <p>현재 맵을 새 맵으로 교체하시겠습니까?</p>
          </div>
          <div class="modal-footer custom-modal-footer">
            <button
              type="button"
              class="btn footer-button"
              data-bs-dismiss="modal"
            >
              <img :src="cancelImage" alt="닫기" />
              <span class="button-text">아니요</span>
            </button>
            <button
              type="button"
              class="btn footer-button"
              @click="changeMap(hoveredLaserDescription.position.row, hoveredLaserDescription.position.col)"
              data-bs-dismiss="modal"
            >
              <img :src="okImage" alt="저장" />
              <span class="button-text">예</span>
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, watch } from "vue";
import ThreeJSManager from "@/common/lib/ThreeJSManager";
import eventBus from "@/common/lib/eventBus.js";
import { useMapStore } from "@/store/map/mapStore"; // 맵 스토어 사용

// 이미지 경로 설정
import scheduleModal from '@/assets/images/ingame/map/background.png';
import titleImage from '@/assets/images/ingame/map/title.png';
import cancelImage from '@/assets/images/ingame/map/cancel.png';
import okImage from '@/assets/images/ingame/map/ok.png';

// 컴포넌트의 props 정의
const props = defineProps({
  selectedMap: {
    type: Object,
    default: () => ({}),
  },
});

const rendererContainer = ref(null);
let threeJSManager = null;

// 맵 스토어
const mapStore = useMapStore();
mapStore.loadDummyData(); // 더미 데이터 로드

const tokenImage = require("@/assets/images/ingame/Token.png");
const defaultMapImage = require("@/assets/images/maps/map1.png");
const mapImage = ref(defaultMapImage); // 기본 맵 이미지
const infoBackground = ref(require("@/assets/images/hover/token_hover.png")); // 기본 정보 배경 이미지
const placedTokens = ref([]);
const showGrid = ref(true);
const gridSize = 50;

// 모달 상태 변수
const selectedMapIndex = ref(0); // 선택된 맵의 인덱스

// selectedMap prop의 변경 사항 감시
watch(
  () => props.selectedMap,
  (newMap) => {
    // 새로운 맵의 이미지 URL로 mapImage를 업데이트
    if (newMap && newMap.imageURL) {
      mapImage.value = newMap.imageURL;
    } else {
      mapImage.value = defaultMapImage;
    }
  },
  { immediate: true }
);

// 창 크기와 그리드 크기를 기반으로 그리드 행과 열 계산
const gridRows = computed(() =>
  Array.from({ length: Math.ceil(window.innerHeight / gridSize) }, (_, i) => i)
);
const gridCols = computed(() =>
  Array.from({ length: Math.ceil(window.innerWidth / gridSize) }, (_, i) => i)
);

// 하드코딩된 덤프 데이터로 활성 레이저 설정
const activeLasers = ref(new Set(["2-3", "4-5", "1-1", "6-7"]));
const hoveredDescription = ref({ title: "", details: "" }); // 현재 마우스가 올려진 그리드 셀의 설명 저장

// 툴팁 데이터와 위치
const hoveredLaserDescription = ref(null);
const tooltipPosition = ref({ x: 0, y: 0 });
let tooltipTimeout = null; // 툴팁 타임아웃 변수

// 레이저 효과 위에 마우스를 올렸을 때
const onLaserMouseEnter = (row, col) => {
  if (tooltipTimeout) clearTimeout(tooltipTimeout); // 이전 타임아웃 클리어
  const description = getDescription(row, col);
  console.log(`Mouse entered on laser at (${row}, ${col})`, description);
  hoveredLaserDescription.value = { ...description, position: { row, col } };

  // 레이저 효과에 위치한 이미지로 infoBackground 업데이트
  if (hoveredLaserDescription.value.image) {
    infoBackground.value = hoveredLaserDescription.value.image;
  } else {
    // 레이저에 이미지가 없을 경우 기본 이미지로 설정
    infoBackground.value = require("@/assets/images/hover/token_hover.png");
  }

  console.log("Tooltip Image:", hoveredLaserDescription.value.image); // 이미지 데이터 로그 확인
};

// 레이저 효과에서 마우스를 내렸을 때
const onLaserMouseLeave = (row, col) => {
  console.log(`Mouse left laser at (${row}, ${col})`);
  tooltipTimeout = setTimeout(() => {
    hoveredLaserDescription.value = null;
    infoBackground.value = require("@/assets/images/hover/token_hover.png");
  }, 1000); // 1초 후에 툴팁 제거
};

// 마우스를 올린 그리드 셀에 대한 설명 표시
const showDescription = (row, col) => {
  const description = getDescription(row, col);
  console.log(`Hovered on grid cell: ${row}-${col}`, description);
  hoveredDescription.value = description;
};

// 그리드 셀에서 마우스가 벗어나면 설명 숨김
const hideDescription = () => {
  hoveredDescription.value = { title: "", details: "" };
  if (tooltipTimeout) clearTimeout(tooltipTimeout); // 기존 타임아웃 제거
  tooltipTimeout = setTimeout(() => {
    hoveredLaserDescription.value = null;
  }, 1000); // 1초 후에 툴팁 제거
};

// 행과 열에 따라 각 그리드 셀에 대한 설명 제공
const getDescription = (row, col) => {
  const map = mapStore.getMapByPosition(row, col);
  if (map) {
    return {
      title: map.name,
      details: map.description,
      image: map.imageURL,
    };
  }
  return { title: "", details: "" };
};

const onDrop = (event) => {
  event.preventDefault();

  const tokenData = event.dataTransfer.getData("text/plain");
  try {
    const parsedToken = JSON.parse(tokenData);
    if (!placedTokens.value.find((t) => t.id === parsedToken.id)) {
      const mapRect = event.currentTarget.getBoundingClientRect();
      placedTokens.value.push({
        ...parsedToken,
        x: event.clientX - mapRect.left,
        y: event.clientY - mapRect.top,
      });

      const removeEvent = new CustomEvent("remove-token-from-list", {
        detail: parsedToken,
      });
      window.dispatchEvent(removeEvent);
    }
  } catch (error) {
    console.error("유효하지 않은 JSON 데이터:", tokenData);
  }
};

const deleteTokenFromMap = (token) => {
  placedTokens.value = placedTokens.value.filter((t) => t.id !== token.id);
};

const returnToken = (token) => {
  deleteTokenFromMap(token);
  window.dispatchEvent(new CustomEvent("add-token-to-list", { detail: token }));
};

let draggingToken = null;
let offsetX = 0;
let offsetY = 0;

const startDrag = (token, event) => {
  draggingToken = token;
  const tokenRect = event.target.getBoundingClientRect();
  offsetX = event.clientX - tokenRect.left;
  offsetY = event.clientY - tokenRect.top;
  document.addEventListener("mousemove", onDrag);
  document.addEventListener("mouseup", stopDrag);
};

const onDrag = (event) => {
  if (draggingToken) {
    const mapRect = document
      .querySelector(".map-section-container")
      .getBoundingClientRect();
    const newX = event.clientX - mapRect.left - offsetX;
    const newY = event.clientY - mapRect.top - offsetY;

    draggingToken.x = newX;
    draggingToken.y = newY;
  }
};

const stopDrag = () => {
  draggingToken = null;
  document.removeEventListener("mousemove", onDrag);
  document.removeEventListener("mouseup", stopDrag);
};

const handleDiceRolled = (results) => {
  console.log("주사위 굴림 결과:", results);
  results.forEach((result) =>
    console.log(`${result.type}면체 주사위 결과: ${result.value}`)
  );
};

const isLaserActive = (row, col) => {
  return activeLasers.value.has(`${row}-${col}`);
};

// 마우스 움직임에 따라 툴팁 위치 업데이트
document.addEventListener("mousemove", (event) => {
  tooltipPosition.value.x = event.pageX + 10; // 마우스 포인터에서 약간 떨어지도록 오프셋 추가
  tooltipPosition.value.y = event.pageY + 10;
});

const openModal = () => {
  // 부트스트랩 모달 열기
  const modal = new bootstrap.Modal(document.getElementById("eventModal"), {
    backdrop: false, // 백드롭을 비활성화합니다
  });
  modal.show();
};

// 맵 변경 로직
const changeMap = (row, col) => {
  const map = mapStore.getMapByPosition(row, col);
  if (map) {
    mapImage.value = map.imageURL;
    mapStore.setSelectedMap(map);
    console.log("Map changed to:", map.name);
  } else {
    console.log("해당 좌표에 맵이 없습니다.");
  }
};

// 모달 스타일에 배경 이미지를 설정합니다.
const modalStyle = computed(() => ({
  backgroundImage: `url(${scheduleModal})`,
  backgroundSize: 'cover',
  backgroundPosition: 'center',
  backgroundRepeat: 'no-repeat',
  zIndex: 1070, // 모달 z-index 설정
}));

onMounted(() => {
  threeJSManager = new ThreeJSManager(rendererContainer.value);
  eventBus.on("dice-rolled", handleDiceRolled);
  window.addEventListener("toggle-grid", (event) => {
    showGrid.value = event.detail;
  });

  window.addEventListener("delete-token", (event) => {
    deleteTokenFromMap(event.detail);
  });

  // 초기 이벤트 좌표 설정
  activeLasers.value = new Set(["2-3", "4-5", "1-1", "6-7"]);
});

onUnmounted(() => {
  eventBus.off("dice-rolled", handleDiceRolled);
  window.removeEventListener("toggle-grid", () => {});
  window.removeEventListener("delete-token", () => {});
});
</script>

<style scoped>
.map-section-container {
  width: 100%;
  height: 100%;
  margin: 5px;
  position: relative;
  overflow: hidden;
  z-index: 10; /* 기본 z-index 설정 */
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
  image-rendering: auto;
  z-index: 1; /* 맵 이미지의 z-index 설정 */
}

.token {
  position: absolute;
  width: 40px;
  height: 40px;
  cursor: move;
  z-index: 2; /* 토큰의 z-index 설정 */
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
  z-index: 3; /* 그리드 오버레이의 z-index 설정 */
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
  z-index: 2; /* 기본 z-index 설정 */
}

.laser-effect {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background-color: red;
  box-shadow: 0 0 10px 5px red;
  animation: pulse 1s infinite;
  z-index: 1; /* 레이저 효과의 z-index 설정 */
  pointer-events: auto; /* 마우스 이벤트를 받을 수 있도록 설정 */
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
  z-index: 5; /* 정보 패널의 z-index 설정 */
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

.debug-button {
  position: absolute;
  top: 10px; /* 화면 최상단에 위치 */
  left: 10px; /* 왼쪽에 위치 */
  z-index: 100; /* 다른 요소들 위에 위치하도록 설정 */
  padding: 10px 20px;
  background-color: #007bff;
  color: #fff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.debug-button:hover {
  background-color: #0056b3;
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

/* 툴팁 스타일 */
.tooltip {
  position: absolute;
  background-color: rgba(0, 0, 0, 0.75);
  color: #fff;
  padding: 10px;
  border-radius: 5px;
  pointer-events: none;
  z-index: 1000; /* 툴팁의 z-index를 높게 설정 */
  max-width: 250px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
}

.tooltip-image {
  width: 100%;
  height: auto;
  border-radius: 3px;
  margin-bottom: 10px;
  background-color: transparent;
  z-index: 1001; /* 이미지가 다른 요소 위에 위치하도록 */
  position: relative;
  display: block; /* 보이는 상태 유지 */
}

.tooltip h4 {
  margin: 0;
  font-size: 16px;
  font-weight: bold;
}

.tooltip p {
  margin: 5px 0 0;
  font-size: 14px;
  line-height: 1.4;
}

/* 모달 스타일 */
.modal-content {
  border-radius: 10px;
  padding: 20px;
  z-index: 1070; /* 모달의 z-index 설정 */
  background-color: #1e1e1e;
  color: #fff;
}

.modal-header {
  border-bottom: none;
}

.custom-modal-header {
  position: relative;
}

.modal-body {
  text-align: center; /* 텍스트를 가운데 정렬 */
}

.modal-title-image {
  width: 100%;
  height: auto;
  position: relative;
  display: block; /* 보이는 상태 유지 */
  margin-top: -14%; /* 이미지를 더 위쪽으로 이동 */
}

.overlay-text {
  position: absolute;
  top: -33%; /* 필요에 따라 조정 */
  left: 50%;
  transform: translateX(-50%);
  color: #ffffff;
  font-size: 20px; /* 필요에 따라 조정 */
  font-weight: bolder;
}

.modal-footer {
  border-top: none;
}

.custom-modal-footer {
  display: flex;
  justify-content: space-between;
}

.footer-button {
  position: relative;
  margin: 0 10px; /* 필요에 따라 간격 조정 */
}

.footer-button img {
  display: block;
}

.button-text {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: white;
  pointer-events: none;
  font-size: 13px;
}

.btn-close {
  background: none;
  border: none;
  font-size: 20px;
}

/* 백드롭 숨기기 */
.modal-backdrop {
  display: none !important;
}
</style>
