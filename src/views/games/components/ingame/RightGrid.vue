<template>
  <div class="right-grid-container">
    <Token class="token-area" />
    <Action class="action-area" />
    <Inventory class="inventory-area" />
    <Dice class="dice-area" />
  </div>
</template>

<script setup>
import Token from '@/views/games/components/ingame/Token.vue';
import Action from '@/views/games/components/ingame/Action.vue';
import Dice from '@/views/games/components/ingame/Dice.vue';
import Inventory from '@/views/games/components/ingame/Inventory.vue';
</script>

<style scoped>
.right-grid-container {
  display: grid;
  grid-template-columns: 1fr 2fr;
  grid-template-rows: 2fr 1fr 1fr;
  gap: 10px;
  height: 100%;
}

.token-area, .action-area {
  overflow-y: auto;
  height: 100%;
}

.inventory-area {
  grid-column: 1 / 3;
  grid-row: 2 / 3;
  overflow-y: auto; /* 스크롤 허용 */
  box-sizing: border-box;
  height: 100%;
}

.dice-area {
  grid-column: 1 / 3;
  grid-row: 3 / 4;
  height: 100%;
  overflow-y: auto;
}
</style>
