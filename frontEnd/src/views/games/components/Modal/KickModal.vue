<template>
    <div class="modal" @click.self="closeModal">
      <div class="modal-content">
        <div class="modal-header">
          경고
        </div>
        <div class="modal-body">
          {{ user.name }} 님을 추방하시겠습니까?
        </div>
        <div class="modal-footer">
          <button @click="confirmKick">추방</button>
          <button @click="closeModal">취소</button>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  
  const props = defineProps({
    user: Object,
  });
  
  const emit = defineEmits(['close', 'kick']);
  
  const closeModal = () => {
    emit('close');
  };
  
  const confirmKick = () => {
    emit('kick', props.user);
    closeModal();
  };
  </script>
  
  <style scoped>
  .modal {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
  }
  
  .modal-content {
    background-color: #291707;
    border: 1px solid white;
    border-radius: 10px;
    padding: 20px;
    width: 300px;
    text-align: center;
  }
  
  .modal-header {
    font-size: 1.5em;
    margin-bottom: 20px;
    color: white;
  }
  
  .modal-body {
    margin-bottom: 20px;
    color: white;
  }
  
  .modal-footer button {
    margin: 5px;
    padding: 5px 10px;
  }
  </style>
  