<template>
    <div ref="rendererContainer" class="renderer-container"></div>
    <div class="controls">
      <div v-for="type in diceTypes" :key="type" class="dice-control">
        <div :class="'dice dice-' + type"></div>
        <button @click="decreaseCount(type)">&#8722;</button>
        <span>{{ diceCounts[type] }}</span>
        <button @click="increaseCount(type)">&#43;</button>
      </div>
      <button @click="rollDice">주사위 굴리기</button>
    </div>
  </template>
  
  <script>
  import ThreeJSManager from './ThreeJSManager';
  
  export default {
    data() {
      return {
        diceTypes: [4, 6, 8, 10, 12, 20, 100],
        diceCounts: { 4: 0, 6: 0, 8: 0, 10: 0, 12: 0, 20: 0, 100: 0 },
        threeJSManager: null,
      };
    },
    mounted() {
      this.threeJSManager = new ThreeJSManager(this.$refs.rendererContainer);
    },
    methods: {
      increaseCount(type) {
        this.diceCounts[type]++;
      },
      decreaseCount(type) {
        if (this.diceCounts[type] > 0) {
          this.diceCounts[type]--;
        }
      },
      rollDice() {
        const diceTypesToRoll = [];
        for (const [type, count] of Object.entries(this.diceCounts)) {
          for (let i = 0; i < count; i++) {
            diceTypesToRoll.push(Number(type));
          }
        }
        if (diceTypesToRoll.length > 0) {
          this.threeJSManager.rollDice(diceTypesToRoll);
        }
      },
    },
  };
  </script>
  
  <style>
  .renderer-container {
    width: 100%;
    height: 80vh;
    background-color: #000;
  }
  
  .controls {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-top: 10px;
  }
  
  .dice-control {
    display: flex;
    align-items: center;
    margin: 10px 0;
  }
  
  .dice {
    width: 40px;
    height: 40px;
    margin-right: 10px;
    background-color: #8b5e3c;
    display: flex;
    justify-content: center;
    align-items: center;
    color: #fff;
    font-weight: bold;
  }
  
  .dice-4::after { content: '4'; }
  .dice-6::after { content: '6'; }
  .dice-8::after { content: '8'; }
  .dice-10::after { content: '10'; }
  .dice-12::after { content: '12'; }
  .dice-20::after { content: '20'; }
  .dice-100::after { content: '100'; }
  
  button {
    margin: 0 5px;
    padding: 5px 10px;
    background-color: #8b5e3c;
    color: #fff;
    border: none;
    cursor: pointer;
  }
  
  button:hover {
    background-color: #a0764a;
  }
  </style>
  