<template>
  <div class="dice-area" :style="backgroundStyle">
    <div class="dice-grid">
      <div v-for="dice in diceList" :key="dice.diceId" class="dice-container">
        <img :src="dice.image" :alt="dice.name" class="dice" />
        <div class="dice-controls">
          <button @click="decreaseCount(dice.diceId)" class="control-button">-</button>
          <span class="dice-count">{{ dice.count }}</span>
          <button @click="increaseCount(dice.diceId)" class="control-button">+</button>
        </div>
      </div>
      <div class="roll-container">
        <button @click="emitRollDice" class="roll-dice-button">Roll</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue';
import eventBus from '@/common/lib/eventBus.js';

const diceList = reactive([
  { diceId: 4, name: 'D4', image: require('@/assets/images/ingame/Dice4.png'), count: 0 },
  { diceId: 6, name: 'D6', image: require('@/assets/images/ingame/Dice6.png'), count: 0 },
  { diceId: 8, name: 'D8', image: require('@/assets/images/ingame/Dice8.png'), count: 0 },
  { diceId: 10, name: 'D10', image: require('@/assets/images/ingame/Dice10.png'), count: 0 },
  { diceId: 12, name: 'D12', image: require('@/assets/images/ingame/Dice12.png'), count: 0 },
  { diceId: 20, name: 'D20', image: require('@/assets/images/ingame/Dice20.png'), count: 0 },
  { diceId: 100, name: 'D100', image: require('@/assets/images/ingame/Dice100.png'), count: 0 },
]);

const emitRollDice = () => {
  const diceTypesToRoll = [];
  diceList.forEach(dice => {
    for (let i = 0; i < dice.count; i++) {
      diceTypesToRoll.push({ type: dice.diceId, id: dice.diceId });
    }
  });
  console.log('Emitting roll-dice event with:', diceTypesToRoll);
  eventBus.emit('roll-dice', diceTypesToRoll);
};

const increaseCount = (diceId) => {
  const dice = diceList.find(dice => dice.diceId === diceId);
  if (dice) {
    dice.count++;
  }
};

const decreaseCount = (diceId) => {
  const dice = diceList.find(dice => dice.diceId === diceId);
  if (dice && dice.count > 0) {
    dice.count--;
  }
};

const backgroundStyle = {
  backgroundImage: `url(${require('@/assets/images/ingame/Border4.png')})`,
  backgroundSize: 'cover',
  borderRadius: '5px',
  width: '100%',
  height: '100%',
  padding: '10px',
  boxSizing: 'border-box',
};
</script>


<style scoped>
.dice-area {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  box-sizing: border-box;
  padding: 5px;
}

.dice-grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr) auto;
  gap: 5px;
  width: 100%;
  align-items: center;
}

.dice-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 0;
  margin: 0;
}

.dice {
  width: 35px;
  height: 35px;
}

.dice-controls {
  display: flex;
  align-items: center;
  gap: 2px;
  margin-top: 2px;
}

.dice-count {
  color: white;
  font-weight: bold;
}

.control-button {
  background: none;
  border: none;
  color: white;
  font-size: 12px;
  cursor: pointer;
  padding: 0;
  margin: 0;
}

.roll-container {
  display: flex;
  justify-content: center;
  align-items: center;
}

.roll-dice-button {
  padding: 5px 15px;
  background-color: #564307;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  width: 100%;
}
</style>
