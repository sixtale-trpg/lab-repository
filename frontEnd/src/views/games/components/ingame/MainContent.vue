<template>
  <div class="main-content-inner">
    <div class="map-container">
      <Map class="map" />
    </div>
    <div class="right-section">
      <div class="column-left">
        <Token class="token" />
        <Dice class="dice" />
      </div>
      <div class="column-right">
        <Action class="action" />
        <Inventory class="inventory" />
      </div>
    </div>
  </div>
</template>

<script setup>
import Map from '@/views/games/components/ingame/Map.vue';
import Token from '@/views/games/components/ingame/Token.vue';
import Action from '@/views/games/components/ingame/Action.vue';
import Dice from '@/views/games/components/ingame/Dice.vue';
import Inventory from '@/views/games/components/ingame/Inventory.vue';
</script>

<style scoped>
.main-content-inner {
  display: flex;
  height: 100%;
  box-sizing: border-box;
}

.map-container {
  width: 66.67%; /* 가로의 2/3를 명시적으로 차지하도록 설정 */
  height: 100%;
  display: flex;
}

.map {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.right-section {
  width: 33.33%; /* 가로의 1/3을 명시적으로 차지하도록 설정 */
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.column-left,
.column-right {
  display: flex;
  flex-direction: column;
  gap: 10px;
  flex: 1;
}

.token,
.dice,
.action,
.inventory {
  flex: 1;
  width: 100%;
}
</style>
