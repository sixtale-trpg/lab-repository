<template>
  <div class="map-section-container" @dragover.prevent @drop="onDrop">
    <img v-if="mapImage" :src="mapImage" alt="Map" class="map-image" />
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
            @click="openModal(row, col)" 
          ></div>
          <img
            v-if="npcData[`${row}-${col}`]"
            :src="npcData[`${row}-${col}`].imageUrl"
            alt="NPC"
            class="npc-image"
          />
        </div>
      </div>
    </div>

    <div class="info-panel" v-if="hoveredDescription.title">
      <img
        class="info-background"
        :src="hoveredDescription.nextMapUrl"
        alt="Information Background"
      />
      <div class="info-content">
        <h3>{{ hoveredDescription.details }}</h3>
      </div>
    </div>

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
              @click="changeMap(hoveredLaserDescription)"
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
import { useMapStore } from "@/store/map/mapStore";
import { getMapPlace, getMapNpcs } from '@/common/api/RoomsAPI';
import { useRoute } from 'vue-router';

import scheduleModal from '@/assets/images/ingame/map/background.png';
import titleImage from '@/assets/images/ingame/map/title.png';
import cancelImage from '@/assets/images/ingame/map/cancel.png';
import okImage from '@/assets/images/ingame/map/ok.png';
import backButtonImage from '@/assets/images/ingame/map/back_button.png';

const props = defineProps({
  selectedMap: {
    type: Object,
    default: () => ({}),
  },
});

const rendererContainer = ref(null);
let threeJSManager = null;

const mapStore = useMapStore();
const route = useRoute();
const roomId = ref(route.params.roomId);

const tokenImage = require("@/assets/images/ingame/Token.png");
const defaultMapImage = require("@/assets/images/maps/map1.png");
const mapImage = ref(defaultMapImage);
const infoBackground = ref(require("@/assets/images/hover/token_hover.png"));
const placedTokens = ref([]);
const showGrid = ref(true);
const gridSize = 50;

const npcData = ref({}); // NPC 정보를 저장할 객체
const selectedMapIndex = ref(0);
const cellDescriptions = ref({});

// selectedMap prop의 변경 사항 감시
watch(
  () => props.selectedMap,
  async (newMap) => {
    if (newMap && newMap.imageURL) {
      mapImage.value = newMap.imageURL;
      console.log("Selected Map ID:", newMap.id);

      try {
        const mapId = newMap.id;
        const roomId2 = roomId.value;

        const mapInfo = await getMapPlace(roomId2, mapId);
        const npcInfo = await getMapNpcs(roomId2, mapId);  // NPC 데이터 가져오기

        if (mapInfo && Array.isArray(mapInfo.placeEvents)) {
          activeLasers.value = new Set(
            mapInfo.placeEvents.map(event => {
              const coord = `${event.row}-${event.col}`;
              console.log("Laser activated at:", coord);
              return coord;
            })
          );

          cellDescriptions.value = {};
          mapInfo.placeEvents.forEach(event => {
            cellDescriptions.value[`${event.row}-${event.col}`] = {
              title: `Event at (${event.row}, ${event.col})`,
              details: event.description,
              nextMapId: event.nextMapId,
              nextMapUrl: event.nextMapUrl,
            };
          });

          console.log("Map Info:", mapInfo);
        } else {
          console.warn("Invalid map data or placeEvents is undefined");
        }

        if (npcInfo && Array.isArray(npcInfo.npcEventList)) {
          npcInfo.npcEventList.forEach(npcEvent => {
            npcData.value[`${npcEvent.row}-${npcEvent.col}`] = {
              description: npcEvent.description,
              currentHp: npcEvent.currentHp,
              imageUrl: backButtonImage,  // 여기에 실제 NPC 이미지 경로를 설정하세요
            };
          });

          console.log("NPC Info:", npcInfo);
        } else {
          console.warn("Invalid NPC data or npcEventList is undefined");
        }

      } catch (error) {
        console.error("Error loading map or NPC data:", error);
      }
    } else {
      mapImage.value = defaultMapImage;
      console.warn("Selected Map is null or does not have an image URL.");
    }
  },
  { immediate: true }
);

const gridRows = computed(() =>
  Array.from({ length: Math.ceil(window.innerHeight / gridSize) }, (_, i) => i)
);
const gridCols = computed(() =>
  Array.from({ length: Math.ceil(window.innerWidth / gridSize) }, (_, i) => i)
);

const activeLasers = ref(new Set());
const hoveredDescription = ref({ title: "", details: "" });

const hoveredLaserDescription = ref(null);
const tooltipPosition = ref({ x: 0, y: 0 });
let tooltipTimeout = null;

const onLaserMouseEnter = (row, col) => {
  if (tooltipTimeout) clearTimeout(tooltipTimeout);
  const description = getDescription(row, col);
  console.log(`Mouse entered on laser at (${row}, ${col})`, description);
  hoveredLaserDescription.value = { ...description, position: { row, col } };

  if (hoveredLaserDescription.value.nextMapUrl) {
    infoBackground.value = hoveredLaserDescription.value.nextMapUrl;
  } else {
    infoBackground.value = require("@/assets/images/hover/token_hover.png");
  }

  console.log("Tooltip Image:", hoveredLaserDescription.value.nextMapUrl);
};

const onLaserMouseLeave = (row, col) => {
  console.log(`Mouse left laser at (${row}, ${col})`);
  tooltipTimeout = setTimeout(() => {
    hoveredLaserDescription.value = null;
    infoBackground.value = require("@/assets/images/hover/token_hover.png");
  }, 1000);
};

const showDescription = (row, col) => {
  const description = getDescription(row, col);
  console.log(`Hovered on grid cell: ${row}-${col}`, description);
  hoveredDescription.value = description;
};

const hideDescription = () => {
  hoveredDescription.value = { title: "", details: "" };
  if (tooltipTimeout) clearTimeout(tooltipTimeout);
  tooltipTimeout = setTimeout(() => {
    hoveredLaserDescription.value = null;
  }, 1000);
};

const getDescription = (row, col) => {
  const descriptionKey = `${row}-${col}`;
  const description = cellDescriptions.value[descriptionKey];
  if (description) {
    return description;
  }
  return { title: "Unknown Location", details: "No description available." };
};

const onDrop = (event) => {
  event.preventDefault();

  const tokenData = event.dataTransfer.getData("text/plain");
  try {
    const parsedToken = JSON.parse(tokenData);
    if (!placedTokens.value.find((t) => t.id === parsedToken.id)) {
      const mapRect = event.currentTarget.getBoundingClientRect();
      const tokenX = event.clientX - mapRect.left;
      const tokenY = event.clientY - mapRect.top;
      placedTokens.value.push({
        ...parsedToken,
        x: tokenX,
        y: tokenY,
      });

      console.log(`Token placed at: (${tokenX.toFixed(1)}, ${tokenY.toFixed(1)})`);

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

    console.log(`Dragging token to: (${newX.toFixed(1)}, ${newY.toFixed(1)})`);
  }
};

const stopDrag = () => {
  if (draggingToken) {
    console.log(`Token dropped at: (${draggingToken.x.toFixed(1)}, ${draggingToken.y.toFixed(1)})`);
  }
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

document.addEventListener("mousemove", (event) => {
  tooltipPosition.value.x = event.pageX + 10;
  tooltipPosition.value.y = event.pageY + 10;
});

const openModal = (row, col) => {
  const description = getDescription(row, col);
  if (description) {
    hoveredLaserDescription.value = description;
    const modal = new bootstrap.Modal(document.getElementById("eventModal"), {
      backdrop: false,
    });
    modal.show();
    console.log("Modal opened for position:", row, col);
  } else {
    console.warn("No event found for this position.");
  }
};

const changeMap = async (description) => {
  if (description && description.nextMapUrl && description.nextMapId) {
    console.log(`Changing map to URL: ${description.nextMapUrl}`);
    mapImage.value = description.nextMapUrl;

    try {
      const newMapId = description.nextMapId;
      const mapInfo = await getMapPlace(roomId.value, newMapId);
      const npcInfo = await getMapNpcs(roomId.value, newMapId);  // NPC 데이터도 함께 가져오기

      if (mapInfo && Array.isArray(mapInfo.placeEvents)) {
        activeLasers.value = new Set(
          mapInfo.placeEvents.map(event => {
            const coord = `${event.row}-${event.col}`;
            console.log("Laser activated at:", coord);
            return coord;
          })
        );

        cellDescriptions.value = {};
        mapInfo.placeEvents.forEach(event => {
          cellDescriptions.value[`${event.row}-${event.col}`] = {
            title: `Event at (${event.row}, ${event.col})`,
            details: event.description,
            nextMapId: event.nextMapId,
            nextMapUrl: event.nextMapUrl,
          };
        });

        console.log("Updated Map Info:", mapInfo);
      } else {
        console.warn("Invalid map data or placeEvents is undefined");
      }

      if (npcInfo && Array.isArray(npcInfo.npcEventList)) {
        npcData.value = {};  // 기존 NPC 데이터를 초기화합니다.
        npcInfo.npcEventList.forEach(npcEvent => {
          npcData.value[`${npcEvent.row}-${npcEvent.col}`] = {
            description: npcEvent.description,
            currentHp: npcEvent.currentHp,
            imageUrl: backButtonImage,  // 여기에 실제 NPC 이미지 경로를 설정하세요
          };
        });

        console.log("Updated NPC Info:", npcInfo);
      } else {
        console.warn("Invalid NPC data or npcEventList is undefined");
      }

    } catch (error) {
      console.error("Error loading new map or NPC data:", error);
    }
  } else {
    console.warn("No next map available for this position.");
  }
};

const modalStyle = computed(() => ({
  backgroundImage: `url(${scheduleModal})`,
  backgroundSize: 'cover',
  backgroundPosition: 'center',
  backgroundRepeat: 'no-repeat',
  zIndex: 1070,
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

  if (props.selectedMap && props.selectedMap.id) {
    console.log("Initial Selected Map ID:", props.selectedMap.id);
  } else {
    console.warn("Selected Map is null or ID is not available on initial load.");
  }
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
  z-index: 10;
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
  z-index: 1;
}

.token {
  position: absolute;
  width: 40px;
  height: 40px;
  cursor: move;
  z-index: 2;
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
  z-index: 3;
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
  overflow: visible;
  z-index: 2;
}

.laser-effect {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background-color: red;
  box-shadow: 0 0 10px 5px red;
  animation: pulse 1s infinite;
  z-index: 1;
  pointer-events: auto;
}

.npc-image {
  position: absolute;
  width: 30px;
  height: 30px;
  z-index: 4; /* NPC 이미지의 z-index 설정 */
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
  z-index: 5;
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
  top: 10px;
  left: 10px;
  z-index: 100;
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
  z-index: 1000;
  max-width: 250px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
}

.tooltip-image {
  width: 100%;
  height: auto;
  border-radius: 3px;
  margin-bottom: 10px;
  background-color: transparent;
  z-index: 1001;
  position: relative;
  display: block;
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
  z-index: 1070;
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
  text-align: center;
}

.modal-title-image {
  width: 100%;
  height: auto;
  position: relative;
  display: block;
  margin-top: -14%;
}

.overlay-text {
  position: absolute;
  top: -33%;
  left: 50%;
  transform: translateX(-50%);
  color: #ffffff;
  font-size: 20px;
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
  margin: 0 10px;
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

.modal-backdrop {
  display: none !important;
}
</style>
