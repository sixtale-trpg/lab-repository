<template>
  <div v-if="isOpen" class="modal-overlay" @click.self="closeModal">
    <div class="modal-content" :style="modalStyle">
      <div class="modal-header">
        <h2>Rulebook</h2>
      </div>
      <div class="modal-body">
        <p>여기에 룰북 내용이 들어갑니다.</p>
      </div>
      <div class="modal-footer">
        <button @click="closeModal">닫기</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue';

const props = defineProps({
  isOpen: Boolean,
});

const emit = defineEmits(['close']);

const closeModal = () => {
  emit('close');
};

// 이미지 경로 설정
const rulebookImage = require('@/assets/images/room/rulebook_modal/book.png');

const modalStyle = computed(() => ({
  backgroundImage: `url(${rulebookImage})`,
  backgroundSize: 'contain',
  backgroundPosition: 'center',
  backgroundRepeat: 'no-repeat'
}));
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000; /* Ensure the overlay is on top */
}

.modal-content {
  position: relative;
  padding: 40px 20px;
  border-radius: 10px;
  max-width: 1050px;
  width: 90%;
  height: 90%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
  background-size: contain;
  z-index: 1001; /* Ensure the modal content is on top of the overlay */
}

.modal-header h2 {
  margin: 0;
  font-size: 24px;
  color: #000;
}

.modal-body {
  flex-grow: 1;
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-body p {
  margin: 0;
  font-size: 18px;
  color: #000;
}

.modal-footer {
  margin-top: 20px;
}

button {
  padding: 10px 20px;
  font-size: 16px;
  background-color: #444;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}
</style>
