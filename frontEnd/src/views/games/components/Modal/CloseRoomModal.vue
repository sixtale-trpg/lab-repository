<template>
  <div class="modal" @click.self="closeModal">
    <div class="modal-content custom-modal" :style="modalStyle">
      <div class="modal-header custom-modal-header">
        <img :src="titleImage" alt="Background" class="modal-title-image" />
        <div class="overlay-text">게임 종료</div>
      </div>
      <div class="modal-body">
        <p class="modal-message">기록은 저장되지 않습니다. 정말 종료하시겠습니까?</p>
      </div>
      <div class="modal-footer custom-modal-footer">
        <button type="button" class="btn footer-button" @click="closeModal">
          <img :src="cancelImage" alt="취소" />
          <span class="button-text">취소</span>
        </button>
        <button type="button" class="btn footer-button" @click="confirmClose">
          <img :src="okImage" alt="게임 종료" />
          <span class="button-text">게임 종료</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineEmits, computed } from 'vue';
import scheduleModal from '@/assets/images/room/calendar_modal/background.png';
import titleImage from '@/assets/images/room/calendar_modal/title.png';
import cancelImage from '@/assets/images/room/calendar_modal/cancel.png';
import okImage from '@/assets/images/room/calendar_modal/ok.png';

const emit = defineEmits(['confirm', 'close']);

const closeModal = () => {
  emit('close');
};

const confirmClose = () => {
  emit('confirm');
};

const modalStyle = computed(() => ({
  backgroundImage: `url(${scheduleModal})`,
  backgroundSize: 'cover',
  backgroundPosition: 'center',
  backgroundRepeat: 'no-repeat',
}));
</script>

<style scoped>
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.custom-modal {
  background-color: transparent;
  color: #fff;
  border-radius: 10px;
  padding: 0 !important;
  width: 400px;
  overflow: hidden;
}

.custom-modal-header {
  position: relative;
  padding: 0 !important;
}

.modal-title-image {
  width: 100%;
  height: auto;
  display: block;
}

.overlay-text {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: rgb(214, 205, 170);
  font-size: 24px;
  font-weight: bold;
  text-shadow: 2px 2px 4px rgba(0,0,0,0.5);
}

.modal-body {
  padding: 30px 20px;
  text-align: center;
  color: rgb(214, 205, 170);
  /* background-color: rgba(0, 0, 0, 0.6); */
  /* margin: 0 !important;
  padding: 0 !important; */
  border: transparent !important;
}

.modal-message {
  font-size: 1.2em;
  margin: 0;
}

.custom-modal-footer {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
  /* background-color: rgba(0, 0, 0, 0.6); */
}

.footer-button {
  position: relative;
  margin: 0 10px;
  background: none;
  /* border: none; */
  cursor: pointer;
}

.footer-button img {
  display: block;
}

.button-text {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: rgb(214, 205, 170);
  pointer-events: none;
  font-size: 13px;
}
</style>