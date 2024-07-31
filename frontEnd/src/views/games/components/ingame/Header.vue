<template>
  <div class="header-container" v-bind="$attrs">
    <div class="icon-container">
      <img src="@/assets/images/ingame/Rulebook.png" alt="Rulebook" class="icon" @click="openRulebookModal" />
      <img v-if="isGM" src="@/assets/images/ingame/Scenario.png" @click="openScenarioModal" class="icon" alt="Scenario" />
      <img v-if="isGM" src="@/assets/images/ingame/Map.png" @click="openMapModal" class="icon" alt="Map" />
      <img v-if="isGM" src="@/assets/images/ingame/Grid.png" @click="toggleGrid" class="icon" alt="Grid" />
      <img v-if="isGM" src="@/assets/images/ingame/MuteAll.png" class="icon" alt="Mute" />
      <img v-if="isGM" src="@/assets/images/ingame/Drawing.png" class="icon" alt="Paint" />
    </div>
    <div class="title-container">
      <h1>{{ scenarioTitle }}</h1>
    </div>
    <div class="header-right">
      <div class="timer-container">
        <p>{{ timer }}</p>
      </div>
      <template v-if="isGM">
        <img src="@/assets/images/ingame/Close.png" alt="Close" class="close-icon" @click="openGameEndModal" />
      </template>
    </div>
  </div>

  <!-- Modals -->
  <RuleBookModal :isOpen="showRulebookModal" @close="showRulebookModal = false" />
  <ScenarioModal v-if="showScenarioModal" @close="closeScenarioModal" />
  <MapModal v-if="showMapModal" @close="closeMapModal" />
  <GameEndModal v-if="showGameEndModal" @close="closeGameEndModal" />
</template>

<script setup>
import { ref } from 'vue';
import RuleBookModal from '@/views/games/components/Modal/RulebookModal.vue'; 
import ScenarioModal from '@/views/games/components/Modal/ScenarioModal.vue';
import MapModal from '@/views/games/components/Modal/MapModal.vue';
import GameEndModal from '@/views/games/components/Modal/GameEndModal.vue';

const isGM = ref(true);
const scenarioTitle = ref('시나리오 제목');
const timer = ref('00:00:00');

const showRulebookModal = ref(false);
const showScenarioModal = ref(false);
const showMapModal = ref(false);
const showGameEndModal = ref(false);

const openRulebookModal = () => {
  console.log("Opening Rulebook Modal");
  showRulebookModal.value = true;
};

const closeRulebookModal = () => {
  showRulebookModal.value = false;
};

const openScenarioModal = () => {
  showScenarioModal.value = true;
};

const closeScenarioModal = () => {
  showScenarioModal.value = false;
};

const openMapModal = () => {
  showMapModal.value = true;
};

const closeMapModal = () => {
  showMapModal.value = false;
};

const openGameEndModal = () => {
  showGameEndModal.value = true;
};

const closeGameEndModal = () => {
  showGameEndModal.value = false;
};

const toggleGrid = () => {
  // 그리드 토글 로직이 필요하다면 추가
};
</script>

<style scoped>
.header-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  background-color: #333;
  color: white;
}

.icon-container {
  display: flex;
  gap: 10px;
}

.icon {
  width: 30px;
  height: 30px;
  cursor: pointer;
}

.title-container {
  flex-grow: 1;
  text-align: center;
}

.header-right {
  display: flex;
  align-items: center;
}

.timer-container {
  font-size: 24px;
  margin-right: 20px;
}

.close-icon {
  width: 30px;
  height: 30px;
  cursor: pointer;
}


</style>
