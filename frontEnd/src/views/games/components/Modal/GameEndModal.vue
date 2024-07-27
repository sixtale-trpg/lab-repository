<template>
    <div class="modal-overlay" @click.self="close">
      <div class="modal-content">
        <h2>게임 종료</h2>
        <p>게임을 종료하시겠습니까?</p>
        <button @click="confirmEnd">예</button>
        <button @click="close">아니오</button>
      </div>
    </div>
  </template>
  
  <script setup>
  import { defineEmits } from 'vue';
  
  const emit = defineEmits(['close']);
  
  const close = () => {
    emit('close');
  };
  
  const confirmEnd = () => {
    // 게임 종료 로직을 여기에 추가
    emit('close');
  };
  </script>
  
  <style scoped>
  .modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
  }
  
  .modal-content {
    background-color: white;
    padding: 20px;
    border-radius: 8px;
    width: 300px;
    text-align: center;
  }
  </style>
  