<template>
  <header class="header">
    <div class="header-left">
      <template v-if="isGM">
        <img src="@/assets/images/ingame/Rulebook.png" alt="Rulebook" @click="openRulebookModal" />
        <img src="@/assets/images/ingame/Scenario.png" alt="Scenario" @click="openScenarioModal" />
        <img src="@/assets/images/ingame/Map.png" alt="Map" @click="openMapModal" />
        <img src="@/assets/images/ingame/MuteAll.png" alt="Mute All" @click="muteAll" :class="{ disabled: !isGM }" />
      </template>
      <template v-else>
        <h1>{{ scenarioTitle }}</h1>
      </template>
    </div>
    <div class="header-right" v-if="isGM">
      <img src="@/assets/images/ingame/Close.png" alt="Close" @click="openCloseModal" :class="{ disabled: !isGM }" />
    </div>

    <RuleBookModal :isOpen="showRulebookModal" @close="showRulebookModal = false" />
    <ScenarioModal v-if="showScenarioModal" @close="showScenarioModal = false" />
    <MapModal v-if="showMapModal" @close="showMapModal = false" />
    <CloseRoomModal v-if="showCloseModal" @confirm="closeRoom" @close="showCloseModal = false" />
  </header>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { defineProps } from 'vue';

import RuleBookModal from '@/views/games/components/Modal/RulebookModal.vue'; 
import ScenarioModal from '@/views/games/components/Modal/ScenarioModal.vue';
import CloseRoomModal from '@/views/games/components/Modal/CloseRoomModal.vue';
import MapModal from '@/views/games/components/Modal/MapModal.vue';

const props = defineProps({
  isGM: {
    type: Boolean,
    default: true
  },
});

const scenarioTitle = ref('Scenario Title'); // 시나리오 제목

const showRulebookModal = ref(false);
const showScenarioModal = ref(false);
const showCloseModal = ref(false);
const showMapModal = ref(false);

const router = useRouter();

const openRulebookModal = () => {
  console.log('Opening Rulebook Modal');
  showRulebookModal.value = true;
};

const openScenarioModal = () => {
  showScenarioModal.value = true;
};

const openMapModal = () => {
  showMapModal.value = true;
};

const muteAll = () => {
  if (!props.isGM) return;
  console.log('Mute All Users');
};

const openCloseModal = () => {
  if (!props.isGM) return;
  showCloseModal.value = true;
};

const closeRoom = () => {
  console.log('Close Room');
  router.push('/lobby');
};

onMounted(() => {
  // 백엔드에서 시나리오 제목을 요청하는 부분
  // axios.get('/api/scenario/title')
  //   .then(response => {
  //     scenarioTitle.value = response.data.title;
  //   })
  //   .catch(error => {
  //     console.error('Error fetching scenario title:', error);
  //   });
});
</script>

<style scoped>
.header {
  background-color: #4b3a29;
  color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  height: 100%;
}

.header-left {
  display: flex;
  align-items: center;
  flex: 1;
}

.header-left img {
  width: 32px; 
  height: 32px; 
  margin: 0 10px;
  cursor: pointer;
}

.header-right {
  display: flex;
  align-items: center;
}

.header-right img {
  width: 32px; 
  height: 32px; 
  margin: 0 10px;
  cursor: pointer;
}

.disabled {
  opacity: 0.5;
  pointer-events: none;
}

h1 {
  font-size: 1.5rem;
  margin: 0;
  color: white;
}
</style>
