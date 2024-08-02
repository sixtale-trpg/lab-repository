<template>
    <div class="room-header">
      <div class="left-section">
        <div class="back-button" @click="goBack">
          <img src="@/assets/images/room/arrow.png" alt="Back" />
          <span class="room-title">{{ roomTitle }}</span>
        </div>
      </div>

    </div>
  </template>
  
  <script setup>
  import { defineProps } from 'vue';
  import { useRouter } from 'vue-router';
  
  const props = defineProps({
    roomTitle: String,
    nextSchedule: String,
  });
  
  const router = useRouter();
  
  const goBack = () => {
    router.push({ name: 'Lobby' });
  };
  
  const createSchedule = () => {
    console.log('Creating new schedule');
  };
  </script>
  
  <style scoped>
  .room-header {
    display: grid;
    grid-template-columns: 1fr 1fr; 
    align-items: center;
    background-color: #081017;
    color: white;
    padding: 0 20px;
    box-sizing: border-box;
    height: 8%;
  }
  
  .left-section {
    display: flex;
    align-items: center;
  }
  
  .back-button {
    display: flex;
    align-items: center;
    cursor: pointer;
  }
  
  .back-button img {
    width: 30px; 
    height: 30px;
    margin-right: 10px;
  }
  
  .room-title {
    font-size: 1.5em;
  }
  
  .right-section {
    display: flex;
    justify-content: align; 
    align-items: center;
    gap: 10px; 
  }
  
  .next-schedule-box {
    display: flex;
    align-items: center;
    background-color: #564307; 
    border: 1px solid #ccc;
    padding: 5px 10px;
    border-radius: 5px;
    color: white; 
  }
  
  .next-schedule {
    margin-right: 10px;
  }
  
  .create-button {
    padding: 5px 10px;
    background-color: #564307; 
    color: white; /* 하얀 글씨 */
    border: none;
    border-radius: 5px;
    cursor: pointer;
  }
  </style>
  