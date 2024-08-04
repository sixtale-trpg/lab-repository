<template>
    <div class="modal-overlay" v-if="isVisible">
      <div class="modal-content" :style="modalContentStyle">
        <button class="close-button" @click="closeModal" aria-label="닫기">&times;</button>
        <div class="modal-header">
          <div class="modal-title-container">
            <img src="@/assets/images/character_sheet/title.png" alt="제목" class="modal-title-image">
            <h2 class="modal-title-text">아이템 선택</h2>
          </div>
        </div>
        <div class="modal-body" :style="modalBodyStyle">
          <div class="items-grid">
            <div 
              v-for="availableItem in availableItems" 
              :key="availableItem.id" 
              class="available-item" 
              @click="selectItem(availableItem)"
              :class="{ selected: selectedItem && selectedItem.id === availableItem.id }"
            >
              <img :src="availableItem.image" :alt="availableItem.name" class="item-image" />
              <div class="item-details">
                <span class="item-name">{{ availableItem.name }}</span>
                <span class="item-weight">무게: {{ availableItem.weight }}</span>
                <span class="item-description">{{ availableItem.description }}</span>
              </div>
            </div>
          </div>
          <div class="weight-info">
            현재 하중: {{ currentWeight }} / 전체 하중: {{ totalWeight }}
          </div>
          <div v-if="showWarning" class="warning-message">
            하중을 넘어섬으로 추가할 수 없습니다.
          </div>
        </div>
        <div class="modal-footer" :style="modalFooterStyle">
          <button class="footer-button" @click="closeModal">닫기</button>
          <button class="footer-button save-button" @click="addItem" :disabled="!selectedItem">추가</button>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, computed, defineProps, defineEmits } from 'vue';
  
  const props = defineProps({
    currentWeight: Number,
    totalWeight: Number,
  });
  
  const emit = defineEmits(['close', 'select-item']);
  
  const isVisible = ref(true);
  const availableItems = ref([
    { id: 1, name: 'Sword', image: require('@/assets/images/ingame/Sword.png'), description: 'A sharp blade for combat.', weight: 1 },
    { id: 2, name: 'Helmet', image: require('@/assets/images/ingame/Helmet.png'), description: 'Protects your head from attacks.', weight: 1.5 },
  ]);
  
  const selectedItem = ref(null);
  const showWarning = ref(false);
  
  const closeModal = () => {
    emit('close');
  };
  
  const selectItem = (item) => {
    selectedItem.value = item;
    showWarning.value = false;
  };
  
  const addItem = () => {
    if (selectedItem.value) {
      const newWeight = props.currentWeight + selectedItem.value.weight;
      if (newWeight > props.totalWeight) {
        showWarning.value = true;
      } else {
        selectedItem.value.count = 1; // 초기 횟수 설정
        emit('select-item', selectedItem.value);
        closeModal();
      }
    }
  };
  
  const modalContentStyle = computed(() => ({
    background: `url(${require('@/assets/images/character_sheet/main_background.png')}) no-repeat center center`,
    backgroundSize: 'cover',
  }));
  
  const modalBodyStyle = computed(() => ({
    background: `url(${require('@/assets/images/character_sheet/tab_background.png')}) no-repeat center center`,
    backgroundSize: 'cover',
    marginTop: '10px',
    overflowY: 'auto',
    scrollbarWidth: 'thin',
    scrollbarColor: '#855e2fee #201805',
  }));
  
  const modalFooterStyle = computed(() => ({
    background: `url(${require('@/assets/images/character_sheet/main_background.png')}) no-repeat center center`,
    backgroundSize: 'cover',
  }));
  </script>
  
  <style scoped>
  .modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
  }
  
  .modal-content {
    background: transparent;
    padding: 0;
    border-radius: 10px;
    width: 700px;
    max-width: 90%;
    height: 500px;
    max-height: 90%;
    position: relative;
    color: #fff;
    overflow: hidden;
  }
  
  .close-button {
    position: absolute;
    top: 10px;
    right: 10px;
    background: none;
    border: none;
    font-size: 1.5rem;
    cursor: pointer;
    color: #fff;
  }
  
  .modal-header {
    text-align: center;
    margin-bottom: 0;
    display: flex;
    justify-content: center;
    align-items: center;
    position: relative;
    width: 100%;
    overflow: hidden;
  }
  
  .modal-title-container {
    position: relative;
    width: 100%;
  }
  
  .modal-title-image {
    width: 100%;
    height: auto;
    object-fit: cover;
  }
  
  .modal-title-text {
    position: absolute;
    top: 50%;
    left: 15%;
    transform: translate(-50%, -50%);
    color: white;
    font-size: 1.5rem;
  }
  
  .modal-body {
    padding: 20px;
    border-radius: 0 0 10px 10px;
    flex: 1;
    overflow-y: auto;
    margin-top: 10px;
  }
  
  .modal-body::-webkit-scrollbar {
    width: 8px;
  }
  
  .modal-body::-webkit-scrollbar-track {
    background: #201805;
    border-radius: 5px;
  }
  
  .modal-body::-webkit-scrollbar-thumb {
    background-color: #855e2fee;
    border-radius: 5px;
    border: 2px solid #201805;
  }
  
  .modal-footer {
    display: flex;
    justify-content: center;
    padding: 20px;
    border-radius: 0 0 10px 10px;
  }
  
  .footer-button {
    padding: 10px 20px;
    border: none;
    border-radius: 5px;
    background: #3a3a3a;
    color: #fff;
    cursor: pointer;
    margin: 0 10px;
  }
  
  .footer-button.save-button {
    background: #007bff;
  }
  
  .footer-button:disabled {
    background: #555;
    cursor: not-allowed;
  }
  
  .footer-button.save-button:hover {
    background: #0056b3;
  }
  
  .items-grid {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 10px;
    width: 100%;
  }
  
  .available-item {
    display: flex;
    align-items: center;
    cursor: pointer;
    background: rgba(255, 255, 255, 0.1);
    padding: 10px;
    border-radius: 5px;
    transition: background 0.3s;
  }
  
  .available-item:hover {
    background: rgba(255, 255, 255, 0.2);
  }
  
  .available-item.selected {
    background: rgba(0, 123, 255, 0.2);
  }
  
  .item-image {
    width: 60px;
    height: 60px;
    margin-right: 10px;
  }
  
  .item-details {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
  }
  
  .item-name {
    color: white;
    font-weight: bold;
  }
  
  .item-weight {
    color: white;
    font-size: 0.9rem;
  }
  
  .item-description {
    color: white;
    font-size: 0.9rem;
  }
  
  .weight-info {
    margin-top: 10px;
    color: white;
    font-weight: bold;
    text-align: center;
  }
  
  .warning-message {
    margin-top: 10px;
    color: red;
    font-weight: bold;
    text-align: center;
    background: rgba(255, 0, 0, 0.1);
    padding: 10px;
    border-radius: 5px;
  }
  </style>
  