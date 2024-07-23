<template>
  <div class="chatting-container">
    <div class="tabs">
      <button 
        v-for="tab in tabs" 
        :key="tab" 
        @click="activeTab = tab" 
        :class="{ active: activeTab === tab, [tab]: true }">
        {{ tab }}
      </button>
    </div>
    <div class="chat-content">
      <div v-if="activeTab === '전체'">전체 채팅 로그</div>
      <div v-else-if="activeTab === '로그'">로그</div>
      <div v-else-if="activeTab === '채팅'">채팅</div>
      <div v-else-if="activeTab === '귓속말'">
        <select v-model="selectedUser" class="whisper-dropdown">
          <option v-for="user in users" :key="user.id" :value="user">{{ user.name }}</option>
        </select>
        <div class="whisper-chat" v-if="selectedUser">
          <div v-for="msg in whisperMessages[selectedUser.id]" :key="msg.id">{{ msg.text }}</div>
        </div>
      </div>
      <div v-else-if="activeTab === '룰 AI'">룰 AI</div>
    </div>
    <div class="chat-input">
      <input type="text" v-model="message" @keydown.enter="sendMessage" placeholder="메시지를 입력하세요" />
      <button @click="sendMessage">전송</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
// import axios from 'axios'; // 필요시 axios 또는 다른 HTTP 클라이언트를 사용

const tabs = ['전체', '로그', '채팅', '귓속말', '룰 AI'];
const activeTab = ref('전체');
const message = ref('');
const users = ref([]);
const selectedUser = ref(null);
const whisperMessages = ref({});

// onMounted(() => {
//   // 백엔드에서 현재 방의 플레이어 목록을 받아오는 요청 예시
//   axios.get('/api/game/players')
//     .then(response => {
//       users.value = response.data;
//     })
//     .catch(error => {
//       console.error('Error fetching players:', error);
//     });
// });

const sendMessage = () => {
  if (message.value.trim() !== '') {
    const target = selectedUser.value ? selectedUser.value.id : 'all';
    const log = whisperMessages.value[target] || [];
    log.push({ id: Date.now(), text: message.value });
    whisperMessages.value = { ...whisperMessages.value, [target]: log };
    message.value = '';
  }
};
</script>

<style scoped>
.chatting-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  border: 2px solid #000; 
}

.tabs {
  display: flex;
}

.tabs button {
  flex: 1;
  padding: 10px;
  cursor: pointer;
  background-color: lightgray; 
  color: white; 
}

.tabs button.active {
  background-color: darkgray; /* 액티브된 탭 색상 */
}

.tabs button.전체 {
  background-color: green;
}

.tabs button.로그 {
  background-color: blue;
}

.tabs button.채팅 {
  background-color: purple;
}

.tabs button.귓속말 {
  background-color: red;
}

.tabs button.챗봇AI {
  background-color: orange;
}

.chat-content {
  flex: 1;
  padding: 10px;
  overflow-y: auto;
  color: white;
  border: 1px solid #ddd;
  background-color: #4b3a29;
}

.chat-input {
  display: flex;
  padding: 10px;
  background-color: #4b3a29; 
}

.chat-input input {
  flex: 1;
  padding: 10px;
  border: 1px solid #4b3a29
}

.chat-input button {
  padding: 10px;
}

.whisper-dropdown {
  width: 100%; 
}

.whisper-chat {
  padding: 10px;
  border-top: 1px solid #ddd;
}

.chat-content > div {
  color: #000; 
}
</style>
