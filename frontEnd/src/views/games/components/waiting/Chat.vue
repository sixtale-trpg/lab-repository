<template>
  <div :style="chatSectionStyle" class="chat-section">
    <div class="chat-tabs">
      <div @click="selectTab('all')" :class="{ tab: true, active: selectedTab.value === 'all' }" :style="tabAllStyle">
        <span>전체</span>
      </div>
      <div @click="selectTab('chat')" :class="{ tab: true, active: selectedTab.value === 'chat' }" :style="tabChatStyle">
        <span>채팅</span>
      </div>
      <div @click="selectTab('whisper')" :class="{ tab: true, active: selectedTab.value === 'whisper' }" :style="tabWhisperStyle">
        <span>귓속말</span>
      </div>
    </div>
    <div :style="chatWindowStyle" class="chat-window">
      <div v-for="message in filteredMessages" :key="message.id" class="chat-message">
        <span class="sender">{{ message.sender }}:</span>
        <span class="text">{{ message.text }}</span>
      </div>
    </div>
    <div :style="inputContainerStyle" class="input-container">
      <input v-model="newMessage" @keydown.enter="sendMessage" placeholder="메시지를 입력하세요..." :style="chatInputStyle" class="chat-input" />
      <button @click="sendMessage" :style="sendButtonStyle" class="send-button"></button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';

// 이미지를 동적으로 가져옵니다.
const background1 = require('@/assets/images/room/chat/chat_background1.png');
const background2 = require('@/assets/images/room/chat/chat_background2.png');
const tabAllImage = require('@/assets/images/room/chat/chat_all.png');
const tabChatImage = require('@/assets/images/room/chat/chat_common.png');
const tabWhisperImage = require('@/assets/images/room/chat/chat_whisper.png');
const inputBackground = require('@/assets/images/room/chat/chat_input.png');
const sendButtonImage = require('@/assets/images/room/chat/Send_Button.png');

const selectedTab = ref('all');
const newMessage = ref('');
const messages = ref([
  { id: 1, sender: 'User1', text: '안녕하세요!', type: 'all' },
  { id: 2, sender: 'User2', text: '안녕하세요, User1!', type: 'all' },
  { id: 3, sender: 'User3', text: '안녕하세요!', type: 'whisper' },
]);

const selectTab = (tab) => {
  selectedTab.value = tab;
};

const sendMessage = () => {
  if (newMessage.value.trim() === '') return;
  messages.value.push({
    id: messages.value.length + 1,
    sender: 'Me',
    text: newMessage.value,
    type: selectedTab.value,
  });
  newMessage.value = '';
};

const filteredMessages = computed(() => {
  if (selectedTab.value === 'all') {
    return messages.value;
  }
  return messages.value.filter((message) => message.type === selectedTab.value);
});

const chatSectionStyle = {
  height: '45%',
  width: '97%',
  display: 'flex',
  flexDirection: 'column',
  justifyContent: 'space-between',
  backgroundImage: `url(${background2})`,
  backgroundRepeat: 'no-repeat',
  backgroundPosition: 'center center',
  backgroundSize: 'cover',
  borderRadius: '10px',
  padding: '10px',
  margin: '10px',
  boxSizing: 'border-box',
};

const chatWindowStyle = {
  flexGrow: 1,
  overflowY: 'auto',
  padding: '10px',
  backgroundImage: `url(${background1})`,
  backgroundRepeat: 'no-repeat',
  backgroundPosition: 'center center',
  backgroundSize: 'cover',
  marginBottom: '10px',
  borderRadius: '5px',
};

const tabStyle = {
  cursor: 'pointer',
  width: '20%', // 필요에 따라 너비 조정
  height: 'auto', // 필요에 따라 높이 조정
  backgroundSize: 'cover',
  backgroundPosition: 'center',
  borderRadius: '5px',
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
  color: 'white',
  fontWeight: 'bold',
  transition: 'background-color 0.3s, border 0.3s', // 부드러운 효과를 위한 전환 추가
};

const tabAllStyle = {
  ...tabStyle,
  backgroundImage: `url(${tabAllImage})`,
};

const tabChatStyle = {
  ...tabStyle,
  backgroundImage: `url(${tabChatImage})`,
};

const tabWhisperStyle = {
  ...tabStyle,
  backgroundImage: `url(${tabWhisperImage})`,
};

const activeTabStyle = {
  backgroundColor: 'rgba(255, 255, 255, 0.3)', // 활성 탭의 배경색 변경
  border: '5px solid #fff', // 활성 탭에 테두리 추가
};

const inputContainerStyle = {
  display: 'flex',
  alignItems: 'center',
  backgroundImage: `url(${inputBackground})`,
  backgroundRepeat: 'no-repeat',
  backgroundSize: 'cover',
  borderRadius: '5px',
  padding: '5px',
};

const chatInputStyle = {
  flexGrow: 1,
  border: 'none',
  background: 'transparent',
  color: 'white',
  padding: '10px',
  outline: 'none',
  borderRadius: '5px',
};

const sendButtonStyle = {
  width: '40px',
  height: '40px',
  backgroundImage: `url(${sendButtonImage})`,
  backgroundSize: 'cover',
  backgroundPosition: 'center',
  backgroundColor: 'transparent', // 배경색 투명으로 설정
  border: 'none',
  padding: '0', // 패딩 제거
  margin: '0', // 마진 제거
  outline: 'none',
  cursor: 'pointer',
};
</script>

<style scoped>
.chat-tabs {
  display: flex;
}

.tab.active {
  background-color: rgba(255, 255, 255, 0.3);
  border: 2px solid #fff;
}

.chat-window {
  flex-grow: 1;
  overflow-y: auto;
  padding: 10px;
  background-color: rgba(41, 23, 7, 0.8); /* 가독성을 위한 약간의 오버레이 추가 */
  margin-bottom: 10px;
  border-radius: 5px;
}

.chat-message {
  margin-bottom: 5px;
  color: white;
}

.sender {
  font-weight: bold;
}

input::placeholder {
  color: white;
}

@media (max-width: 768px) {
  .chat-section {
    height: auto;
    margin: 5px;
  }

  .chat-tabs {
    flex-direction: column;
  }

  .chat-tabs div {
    margin: 5px 0;
  }
}
</style>
