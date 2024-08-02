<template>
    <div class="modal" @click.self="closeModal">
      <div class="modal-content">
        <div class="modal-header">
          <h2>게임 종료</h2>
          <button class="close-button" @click="closeModal">×</button>
        </div>
        <div class="modal-body">
          <p>정말 방을 폭파하시겠습니까?</p>
        </div>
        <div class="actions">
          <button class="exit-button" @click="confirmClose">게임 종료</button>
          <button class="cancel-button" @click="closeModal">취소</button>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { defineEmits } from 'vue';
  const emit = defineEmits(['confirm', 'close']);
  
  const closeModal = () => {
    emit('close');
  };
  
  const confirmClose = () => {
    emit('confirm');
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
    z-index: 1000;
  }
  
  .modal-content {
    background-color: #3a2e1f;
    border: 1px solid #5a4d41;
    border-radius: 10px;
    width: 400px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  }
  
  .modal-header {
    background-color: #4b3a29;
    color: white;
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 10px;
    border-top-left-radius: 10px;
    border-top-right-radius: 10px;
    position: relative;
  }
  
  .close-button {
    position: absolute;
    right: 10px;
    background: none;
    border: none;
    color: white;
    font-size: 1.5em;
    cursor: pointer;
  }
  
  .modal-body {
    padding: 20px;
    background-color: #080527;
    text-align: center;
    color: white;
  }
  
  .actions {
    display: flex;
    justify-content: space-around;
    padding: 20px;
    background-color: #080527;
    border-bottom-left-radius: 10px;
    border-bottom-right-radius: 10px;
  }
  
  .actions button {
    background-color: #080527;
    color: white;
    border: none;
    padding: 10px 20px;
    border-radius: 5px;
    cursor: pointer;
  }
  
  .actions button:hover {
    background-color: #e74c3c;
  }
  
  .exit-button {
    background-color: #c0392b;
  }
  
  .cancel-button {
    background-color: #95a5a6;
  }
  
  .actions button:hover.exit-button {
    background-color: #e74c3c;
  }
  
  .actions button:hover.cancel-button {
    background-color: #7f8c8d;
  }
  </style>
  