<template>
  <div class="map-section-container">
    <img src="@/assets/images/maps/map1.png" alt="Map" class="map-image" />
    <div ref="rendererContainer" class="renderer-container"></div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import ThreeJSManager from '@/common/lib/ThreeJSManager';
import eventBus from '@/common/lib/eventBus.js'; // Ensure this path is correct

const rendererContainer = ref(null);
let threeJSManager = null;

const handleRollDice = (diceTypesToRoll) => {
  console.log('Received roll-dice event with:', diceTypesToRoll);
  if (threeJSManager) {
    threeJSManager.rollDice(diceTypesToRoll).then(results => {
      results.forEach(result => console.log(`${result.type}면체 주사위 결과: ${result.value}`));
    });
  }
};

onMounted(() => {
  threeJSManager = new ThreeJSManager(rendererContainer.value);
  eventBus.on('roll-dice', handleRollDice);
});

onUnmounted(() => {
  eventBus.off('roll-dice', handleRollDice);
});
</script>

<style scoped>
.map-section-container {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  overflow: hidden;
  position: relative;
}

.map-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  z-index: 1;
}

.renderer-container {
  width: 100%;
  height: 80vh;
  background-color: transparent; /* Change this to transparent */
  position: absolute; /* Make it position absolute */
  top: 0;
  left: 0;
  z-index: 2; /* Set z-index to be above the map image */
}
</style>
