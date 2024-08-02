<template>
  <div class="chatting-container" :style="backgroundStyle">
    <div class="tabs">
      <button v-for="tab in tabs" :key="tab.key" @click="selectTab(tab.key)" :class="{ active: activeTab === tab.key, [tab.key]: true }">
        {{ tab.label }}
      </button>
    </div>
    <div class="chat-content">
      <div v-if="activeTab === 'all'">전체 채팅 기록</div>
      <div v-else-if="activeTab === 'chat'">채팅 로그</div>
      <div v-else-if="activeTab === 'whisper'">
        <select v-model="selectedUser" class="whisper-dropdown">
          <option v-for="user in users" :key="user.id" :value="user">{{ user.name }}</option>
        </select>
        <div class="whisper-chat" v-if="selectedUser">
          <div v-for="msg in whisperMessages[selectedUser.id]" :key="msg.id">{{ msg.text }}</div>
        </div>
      </div>
    </div>
    <div class="chat-input">
      <input type="text" v-model="message" @keydown.enter="sendMessage" placeholder="메시지를 입력하세요" />
      <button @click="sendMessage" :style="sendButtonStyle"></button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
// import axios from 'axios'; // 필요시 axios 또는 다른 HTTP 클라이언트를 사용

const tabs = [
  { key: 'all', label: '전체' },
  { key: 'chat', label: '채팅' },
  { key: 'whisper', label: '귓속말' }
];

const activeTab = ref('all');
const message = ref('');
const users = ref([
  { id: 1, name: 'Player1' },
  { id: 2, name: 'Player2' },
  { id: 3, name: 'Player3' },
  { id: 4, name: 'Player4' },
  { id: 5, name: 'Player5' },
  { id: 6, name: 'Player6' },
  { id: 7, name: 'Player7' },
  { id: 8, name: 'Player8' },
  { id: 'GM', name: 'Game Master' }
]); // 예시 사용자 데이터
const selectedUser = ref(null);
const whisperMessages = ref({});

// 이미지 경로 설정
const backgroundImage = require('@/assets/images/ingame/Border3.png');
const sendButtonImage = require('@/assets/images/ingame/Send_Button.png');

// 배경 스타일 설정
const backgroundStyle = {
  backgroundImage: `url(${backgroundImage})`,
  backgroundSize: '100% 100%',
  backgroundPosition: 'center',
  padding: '10px',
  boxSizing: 'border-box',
  display: 'flex',
  flexDirection: 'column',
  height: '100%'
};

// 전송 버튼 스타일 설정
const sendButtonStyle = {
  width: '40px',
  height: '40px',
  backgroundImage: `url(${sendButtonImage})`,
  backgroundSize: 'contain',
  border: 'none',
  cursor: 'pointer'
};

// 주석 처리된 실제 데이터 가져오기 로직
// const fetchRoomData = async (roomID) => {
//   try {
//     const response = await axios.get(`/rooms/${roomID}`);
//     if (response.data.statusCode === 200) {
//       const { playMemberList, gmNickname } = response.data.data;
//       // 플레이어와 GM 정보를 통합하여 사용자가 귓속말을 보낼 수 있도록 설정
//       users.value = playMemberList.map(member => ({
//         id: member.playMemberID,
//         name: member.playMemberNickname
//       }));
//       // GM 정보를 추가
//       users.value.push({ id: 'GM', name: gmNickname });
//     }
//   } catch (error) {
//     console.error('Error fetching room data:', error);
//   }
// };

// onMounted(() => {
//   const roomID = 5; // 예시 Room ID, 실제로는 동적으로 받아와야 함
//   fetchRoomData(roomID);
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

const selectTab = (key) => {
  activeTab.value = key;
  if (key !== 'whisper') {
    selectedUser.value = null;
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
  margin-bottom: 0;
}

.tabs button {
  flex: 1;
  padding: 8px;
  cursor: pointer;
  border: 1px solid #333;
  color: white;
  background: linear-gradient(to bottom, #444, #222);
}

.tabs button.active {
  background-color: #555;
  color: #fff;
}

.tabs button.all {
  background: linear-gradient(to bottom, #1A4E23, #102F12); 
}

.tabs button.chat {
  background: linear-gradient(to bottom, #0B3A73, #062048); 
}

.tabs button.whisper {
  background: linear-gradient(to bottom, #8C2A0F, #601A0A); 
}

.chat-content {
  flex: 1;
  padding: 10px;
  overflow-y: auto;
  border: 1px solid #444;
  color: white;
}

.chat-input {
  display: flex;
  align-items: center;
  padding: 5px;
  background-color: #4b3a29;
}

.chat-input input {
  flex: 1;
  padding: 8px;
  border: 1px solid #444;
  background-color: #222;
  color: white;
}

.chat-input input::placeholder {
  color: white;
}

.whisper-dropdown {
  width: 100%;
  margin: 10px 0;
  padding: 5px;
  border: 1px solid #444;
  background-color: #333;
  color: white;
}

.whisper-chat {
  padding: 10px;
  border-top: 1px solid #ddd;
}

.chat-content > div {
  color: #000; 
}
</style>
