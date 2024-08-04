<template>
    <div class="modal-overlay" v-if="isVisible">
      <div class="modal-content" :style="modalContentStyle">
        <div class="modal-header">
          <div class="modal-title-container">
            <img src="@/assets/images/character_sheet/title.png" alt="제목" class="modal-title-image">
            <h2 class="modal-title-text">골드</h2>
          </div>
        </div>
        <div class="modal-body" :style="modalBodyStyle">
          <div class="gold-container">
            <img src="@/assets/images/ingame/Gold.png" alt="골드 이미지" class="gold-image">
            <div class="gold-info">
              <div class="title-container">
                <img src="@/assets/images/character_sheet/nickname_light.png" alt="골드 배경" class="title-image">
                <span class="title-text">현재 골드</span>
              </div>
              <div class="gold-control">
                <img src="@/assets/images/ingame/Minus.png" alt="감소" class="control-button" @click="decreaseGold">
                <span class="gold-amount">{{ goldAmount }}</span>
                <img src="@/assets/images/ingame/Plus.png" alt="증가" class="control-button" @click="increaseGold">
              </div>
            </div>
          </div>
        </div>
        <div class="modal-footer" :style="modalFooterStyle">
          <button class="footer-button save-button" @click="saveGold">저장</button>
          <button class="footer-button" @click="closeModal">닫기</button>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, computed, defineProps, defineEmits, watch } from 'vue';
  
  const props = defineProps({
    currentGold: {
      type: Number,
      required: true
    },
    isVisible: {
      type: Boolean,
      required: true
    }
  });
  
  const emit = defineEmits(['close', 'update-gold']);
  
  const goldAmount = ref(props.currentGold);
  
  watch(() => props.currentGold, (newGold) => {
    goldAmount.value = newGold;
  });
  
  const closeModal = () => {
    emit('close');
  };
  
  const saveGold = () => {
    emit('update-gold', goldAmount.value);
    closeModal();
  };
  
  const decreaseGold = () => {
    if (goldAmount.value > 0) {
      goldAmount.value--;
    }
  };
  
  const increaseGold = () => {
    goldAmount.value++;
  };
  
  const modalContentStyle = computed(() => ({
    background: `url(${require('@/assets/images/character_sheet/main_background.png')}) no-repeat center center`,
    backgroundSize: 'cover',
  }));
  
  const modalBodyStyle = computed(() => ({
    background: `url(${require('@/assets/images/character_sheet/tab_background.png')}) no-repeat center center`,
    backgroundSize: 'cover',
    marginTop: '10px',
    display: 'flex',
    flexDirection: 'row',
    alignItems: 'center',
    padding: '20px',
    height: '150px',
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
    width: 400px;
    max-width: 90%;
    height: 300px;
    max-height: 90%;
    position: relative;
    color: #fff;
    overflow: hidden;
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
    display: flex;
    flex-direction: row;
    align-items: center;
  }
  
  .gold-container {
    display: flex;
    align-items: center;
  }
  
  .gold-image {
    width: 60px;
    height: 60px;
    margin-right: 10px;
  }
  
  .gold-info {
    display: flex;
    flex-direction: column;
  }
  
  .title-container {
    position: relative;
    display: inline-block;
    margin-bottom: 10px;
  }
  
  .title-image {
    display: block;
  }
  
  .title-text {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    color: #fff;
    font-size: 1.4rem;
    white-space: nowrap;
  }
  
  .gold-control {
    display: flex;
    align-items: center;
    margin-top: 10px;
    margin-left: 80PX;
  }
  
  .gold-amount {
    color: #fff;
    font-size: 1.4rem;
    margin: 0 10px;
  }
  
  .control-button {
    width: 30px;
    height: 30px;
    cursor: pointer;
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
  
  .footer-button:hover {
    background: #555;
  }
  
  .save-button {
    background: #28a745;
  }
  
  .save-button:hover {
    background: #218838;
  }
  </style>
  