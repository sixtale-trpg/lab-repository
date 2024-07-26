<template>
  <div class="token-area" :style="backgroundStyle">
    <div 
      class="token-slot" 
      v-for="token in tokens" 
      :key="token.id" 
      @dragstart="dragStart(token)" 
      draggable="true" 
      @mouseover="showTooltip(token, $event)" 
      @mouseleave="hideTooltip"
      @click="showModal(token)"
    >
      <img :src="tokenImage" :alt="token.name" class="token" />
      <div v-if="hoveredToken === token.id" class="tooltip" :style="tooltipStyle">
        {{ token.name }} - {{ token.info }}
      </div>
    </div>
    <div class="token-slot add-token" @click="showInput">
      <img :src="require('@/assets/images/ingame/Plus.png')" alt="Add Token" class="add-icon" />
    </div>
    <div class="token-slot delete-token">
      <img :src="require('@/assets/images/ingame/Trash.png')" alt="Delete Token" class="delete-icon" @dragover.prevent @drop="deleteToken" />
    </div>
    <div v-if="inputVisible" class="input-container" @click.self="closeInput">
      <input v-model="newTokenName" @keyup.enter="addToken" placeholder="Enter token name" />
      <textarea v-model="newTokenInfo" placeholder="Enter token info" rows="3"></textarea>
      <div>
        <button @click="addToken">Add</button>
        <button @click="closeInput">Cancel</button>
      </div>
    </div>
    <div v-if="modalVisible" class="modal" @click.self="closeModal">
      <div class="modal-content">
        <h2>{{ selectedToken.name }}</h2>
        <p>{{ selectedToken.info }}</p>
        <button @click="closeModal">Close</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

const tokens = ref([]);
const tokenImage = require('@/assets/images/ingame/Token.png');
const backgroundStyle = {
  backgroundImage: `url(${require('@/assets/images/ingame/Border.png')})`,
  backgroundSize: '100% 100%',
  backgroundRepeat: 'no-repeat',
  backgroundPosition: 'center',
  display: 'grid',
  gridTemplateColumns: 'repeat(2, 1fr)',
  gap: '10px',
  padding: '20px',
  boxSizing: 'border-box',
  width: '100%',
  height: '100%',
  position: 'relative'
};

const newTokenName = ref('');
const newTokenInfo = ref('');
const inputVisible = ref(false);
const hoveredToken = ref(null);
const tooltipStyle = ref({ top: '0px', left: '0px' });
const modalVisible = ref(false);
const selectedToken = ref({});

const showInput = () => {
  inputVisible.value = true;
};

const addToken = () => {
  if (newTokenName.value) {
    tokens.value.push({ 
      id: tokens.value.length + 1, 
      name: newTokenName.value,
      info: newTokenInfo.value || `This is the token for ${newTokenName.value}`
    });
    newTokenName.value = '';
    newTokenInfo.value = '';
    inputVisible.value = false;
  }
};

const closeInput = () => {
  inputVisible.value = false;
};

const dragStart = (token) => {
  token.dragging = true;
};

const deleteToken = (event) => {
  const token = tokens.value.find(t => t.dragging);
  if (token) {
    tokens.value = tokens.value.filter(t => t.id !== token.id);
  }
};

const showTooltip = (token, event) => {
  hoveredToken.value = token.id;
  tooltipStyle.value = {
    top: `${event.clientY + 10}px`,
    left: `${event.clientX + 10}px`
  };
};

const hideTooltip = () => {
  hoveredToken.value = null;
};

const showModal = (token) => {
  selectedToken.value = token;
  modalVisible.value = true;
};

const closeModal = () => {
  modalVisible.value = false;
};

// 더미 데이터 추가
const addDummyData = () => {
  tokens.value = [
    { id: 1, name: 'Player 1', info: 'This is the token for Player 1' },
    { id: 2, name: 'Player 2', info: 'This is the token for Player 2' },
    { id: 3, name: 'Player 3', info: 'This is the token for Player 3' },
    { id: 4, name: 'Player 4', info: 'This is the token for Player 4' }
  ];
};

onMounted(async () => {
  addDummyData();

  // 실제 백엔드 API 호출 주석 처리
  // const playerCount = await getPlayerCount();
  // tokens.value = Array.from({ length: playerCount }, (_, i) => ({
  //   id: i + 1,
  //   name: `Player ${i + 1}`,
  //   info: `This is the token for Player ${i + 1}`
  // }));
});
</script>

<style scoped>
.token-area {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 10px;
  padding: 20px;
  box-sizing: border-box;
  width: 100%;
  height: 100%;
  background-size: 100% 100%;
  background-repeat: no-repeat;
  background-position: center;
}

.token-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  height: 100%;
}

.token {
  width: 60%;
  height: auto;
  cursor: grab;
}

.add-icon,
.delete-icon {
  width: 30px;
  height: auto;
  cursor: pointer;
}

.input-container {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 200px;
  background-color: white;
  padding: 10px;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0,0,0,0.1);
}

.input-container input,
.input-container textarea {
  width: 60%;
  margin-bottom: 10px;
}

.tooltip {
  position: fixed;
  background-color: rgba(0, 0, 0, 0.8);
  color: rgb(186, 17, 17);
  padding: 5px;
  border-radius: 3px;
  z-index: 10;
  max-width: 200px;
  word-wrap: break-word;
}

.modal {
  position: fixed;
  z-index: 1;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  overflow: auto;
  background-color: rgba(0,0,0,0.4);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background-color: #fefefe;
  padding: 20px;
  border: 1px solid #888;
  width: 300px;
  max-width: 80%;
}

.modal-content button {
  margin-top: 10px;
}
</style>
