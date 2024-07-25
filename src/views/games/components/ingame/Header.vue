<template>
  <div class="header-container" v-bind="$attrs">
    <div class="icon-container">
      <img src="@/assets/images/ingame/Rulebook.png" @click="showRulebookModal" class="icon" alt="Rulebook" />
      <img v-if="isGM" src="@/assets/images/ingame/Scenario.png" @click="showScenarioModal" class="icon" alt="Scenario" />
      <img v-if="isGM" src="@/assets/images/ingame/Map.png" @click="showMapModal" class="icon" alt="Map" />
      <img v-if="isGM" src="@/assets/images/ingame/Grid.png" class="icon" alt="Grid" />
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
  <RulebookModal v-if="showRulebook" @close="showRulebook = false" />
  <ScenarioModal v-if="showScenario" @close="showScenario = false" />
  <MapModal v-if="showMap" @close="showMap = false" />
  <GameEndModal v-if="showGameEndModal" @close="showGameEndModal = false" />
</template>

<script setup>
import { ref, onMounted } from 'vue';
import RulebookModal from '@/views/games/components/Modal/RulebookModal.vue';
import ScenarioModal from '@/views/games/components/Modal/ScenarioModal.vue';
import MapModal from '@/views/games/components/Modal/MapModal.vue';
import GameEndModal from '@/views/games/components/Modal/GameEndModal.vue';

const isGM = ref(true);
const scenarioTitle = ref('시나리오 제목');
const timer = ref('00:00:00');

const showRulebook = ref(false);
const showScenario = ref(false);
const showMap = ref(false);
const showGameEndModal = ref(false);

const showRulebookModal = () => {
  showRulebook.value = true;
};

const showScenarioModal = () => {
  showScenario.value = true;
};

const showMapModal = () => {
  showMap.value = true;
};

const openGameEndModal = () => {
  showGameEndModal.value = true;
};

onMounted(() => {
  // 백엔드에서 시나리오 제목 받아오기. 실제로는 아래 주석을 해제하고 axios 등을 사용.
  /*
  axios.get('/api/scenario-title').then(response => {
    scenarioTitle.value = response.data.title;
  }).catch(error => {
    console.error("Error fetching scenario title:", error);
  });
  */
});
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
