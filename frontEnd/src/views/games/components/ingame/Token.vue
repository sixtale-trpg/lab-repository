<template>
  <div class="token-area" :style="backgroundStyle">
    <div 
      class="token-slot" 
      v-for="token in tokens" 
      :key="token.id" 
      draggable="true"
      @dragstart="dragStart(token, $event)"
      @mouseover="showTooltip"
      @mouseleave="hideTooltip"
    >
      <img :src="tokenImage" :alt="token.name" class="token" />
      <div class="tooltip">맵의 그리드로 자유롭게 이동해보세요</div>
    </div>
    <div class="token-slot add-token" @click="showInput">
      <img :src="plusIcon" alt="Add Token" class="add-icon" />
    </div>
    <div class="token-slot delete-token" @dragover.prevent @drop="deleteToken">
      <img :src="trashIcon" alt="Delete Token" class="delete-icon" />
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

const tokens = ref([]);
const tokenImage = require('@/assets/images/ingame/Token.png');
const plusIcon = require('@/assets/images/ingame/Plus.png');
const trashIcon = require('@/assets/images/ingame/Trash.png');
const tokenAreaBackground = require('@/assets/images/ingame/Border3.png');

const backgroundStyle = {
  backgroundImage: `url(${tokenAreaBackground})`,
  backgroundSize: '100% 100%',
  backgroundRepeat: 'no-repeat',
  backgroundPosition: 'center',
  display: 'grid',
  gridTemplateColumns: 'repeat(2, 1fr)',
  gap: '5px',
  padding: '10px',
  margin: '5px',
  boxSizing: 'border-box',
  width: '100%',
  height: '100%',
  position: 'relative'
};

const newTokenName = ref('');
const newTokenInfo = ref('');
const inputVisible = ref(false);
const modalVisible = ref(false);
const selectedToken = ref({});
let nextTokenId = ref(5); // 고유 ID를 추적하기 위해 사용

const showInput = () => {
  inputVisible.value = true;
};

const addToken = () => {
  if (newTokenName.value) {
    tokens.value.push({ 
      id: nextTokenId.value++, 
      name: newTokenName.value,
      info: newTokenInfo.value || `This is the token for ${newTokenName.value}`
    });
    newTokenName.value = '';
    newTokenInfo.value = '';
    inputVisible.value = false;
  }
  console.log(tokens.value);
};

const closeInput = () => {
  inputVisible.value = false;
};

const dragStart = (token, event) => {
  event.dataTransfer.setData('text/plain', JSON.stringify(token));
  console.log(tokens.value);
};

const deleteToken = (event) => {
  const tokenData = event.dataTransfer.getData('text/plain');
  try {
    const parsedToken = JSON.parse(tokenData);
    tokens.value = tokens.value.filter(t => t.id !== parsedToken.id);
    console.log(parsedToken.id);
    console.log(parsedToken);
    // 토큰 삭제 이벤트 발생
    window.dispatchEvent(new CustomEvent('delete-token', { detail: parsedToken }));
  } catch (error) {
    console.error("Invalid JSON data:", tokenData);
  }
};

const showTooltip = (event) => {
  const tooltip = event.target.closest('.token-slot').querySelector('.tooltip');
  if (tooltip) {
    tooltip.style.visibility = 'visible';
    tooltip.style.opacity = '1';
    const slotRect = event.target.closest('.token-slot').getBoundingClientRect();
    const tooltipRect = tooltip.getBoundingClientRect();
    const top = slotRect.top + slotRect.height + 5; // 토큰 슬롯 아래에 5px 간격
    const left = slotRect.left + slotRect.width - tooltipRect.width; // 오른쪽 하단에 정렬
    tooltip.style.top = `${top}px`;
    tooltip.style.left = `${left}px`;
  }
};

const hideTooltip = (event) => {
  const tooltip = event.target.closest('.token-slot').querySelector('.tooltip');
  if (tooltip) {
    tooltip.style.visibility = 'hidden';
    tooltip.style.opacity = '0';
  }
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

  window.addEventListener('remove-token-from-list', (event) => {
    const token = event.detail;
    tokens.value = tokens.value.filter(t => t.id !== token.id);
  });

  window.addEventListener('add-token-to-list', (event) => {
    const token = event.detail;
    if (!tokens.value.find(t => t.id === token.id)) {
      tokens.value.push(token);
    }
  });
});
</script>

<style scoped>
.token-area {
  height: 100%;  /* 부모의 100% 높이를 사용 */
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
  visibility: hidden;
  width: 150px;
  background-color: rgba(0, 0, 0, 0.8);
  color: #fff;
  text-align: center;
  border-radius: 3px;
  padding: 5px;
  position: fixed; /* 고정 위치 */
  z-index: 10;
  opacity: 0;
  transition: opacity 0.3s;
  font-size: 0.8rem;
}

.tooltip::after {
  content: '';
  position: absolute;
  top: -5px;
  left: 50%;
  margin-left: -5px;
  border-width: 5px;
  border-style: solid;
  border-color: rgba(0, 0, 0, 0.8) transparent transparent transparent;
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
