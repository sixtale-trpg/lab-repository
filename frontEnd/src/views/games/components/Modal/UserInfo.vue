<template>
    <div class="modal" @click.self="closeModal">
      <div class="modal-content">
        <div class="profile-header">
          <div class="profile-tab">프로필</div>
          <button class="close-button" @click="$emit('close')">
            <img src="@/assets/images/room/kick.png" alt="">
          </button>
        </div>
        <div class="profile-body">
          <div class="profile-image-box">
            <img :src="user.profileImage" alt="User Profile" class="profile-image" />
          </div>
          <div class="profile-info">
            <div class="info-item">
              <div class="info-label">아이디</div>
              <div class="info-value">{{ user.id }}</div>
            </div>
            <div class="info-item">
              <div class="info-label">닉네임</div>
              <div class="info-value">{{ user.nickname }}</div>
            </div>
          </div>
        </div>
        <div class="preferences">
          <div class="preferences-header">성향</div>
          <div class="preferences-body">
            <p>{{ userPreferences }}</p>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { onMounted, ref } from 'vue';
  import axios from 'axios';
  import { defineProps, defineEmits } from 'vue';
  
  const props = defineProps({
    user: Object,
  });

  const emit = defineEmits(['close'])

  const closeModal = () => {
  emit('close');
};
  
  const userPreferences = ref('');
  
  onMounted(() => {
    axios.get(`/api/member_detail/${props.user.id}`)
      .then(response => {
        const data = response.data;
        userPreferences.value = `
          RP Type: ${data.rp_type}
          Chat Type: ${data.chat_type}
          Talk Type: ${data.talk_type}
          Taste Type: ${data.taste_type}
        `;
      })
      .catch(error => {
        console.error('Error fetching user preferences:', error);
      });
  });
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
    background-color: #3a2e1f;
    border: 1px solid #5a4d41;
    border-radius: 10px;
    padding: 20px;
    width: 80%;
    max-width: 600px;
  }
  
  .profile-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    background-color: #4b3a29;
    padding: 10px;
    border-radius: 10px 10px 0 0;
  }
  
  .profile-tab {
    font-size: 1.5em;
    color: #fff;
    background-color: #4b3a29;
    padding: 10px;
    border-radius: 10px 10px 0 0;
  }
  
  .close-button {
    background: none;
    border: none;
    font-size: 1.5em;
    color: #fff;
    cursor: pointer;
  }
  
  .profile-body {
    display: flex;
    align-items: flex-start;
    margin-top: 20px;
    background-color: #2d261d;
    padding: 20px;
  }
  
  .profile-image-box {
    background-color: #5a4d41;
    border: 3px solid gold;
    padding: 10px;
    border-radius: 50%;
    margin-right: 20px;
  }
  
  .profile-image {
    width: 100px;
    height: 100px;
    border-radius: 50%;
  }
  
  .profile-info {
    flex: 1;
  }
  
  .info-item {
    display: flex;
    align-items: center;
    margin-bottom: 10px;
    background-color: #4b3a29;
    padding: 10px;
    border-radius: 5px;
  }
  
  .info-label {
    width: 80px;
    font-weight: bold;
    color: #fff;
  }
  
  .info-value {
    color: #fff;
  }
  
  .preferences {
    background-color: #2d261d;
    padding: 20px;
    width: 100%;
  }
  
  .preferences-header {
    font-size: 1.2em;
    color: #fff;
    margin-bottom: 10px;
  }
  
  .preferences-body {
    background-color: #291707;
    border: 1px solid #5a4d41;
    padding: 10px;
    color: #fff;
  }
  </style>
  