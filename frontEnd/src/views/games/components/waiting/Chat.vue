<template>
  <div class="chat-section">
    <div class="chat-tabs">
      <div @click="selectTab('all')" :class="{ active: selectedTab === 'all', all: true }">전체</div>
      <div @click="selectTab('chat')" :class="{ active: selectedTab === 'chat', chat: true }">채팅</div>
      <div @click="selectTab('whisper')" :class="{ active: selectedTab === 'whisper', whisper: true }">귓속말</div>
    </div>
    <div class="chat-window">
      <div v-for="message in filteredMessages" :key="message.id" class="chat-message">
        <span class="sender">{{ message.sender }}:</span>
        <span class="text">{{ message.text }}</span>
      </div>
    </div>
    <input v-model="newMessage" @keydown.enter="sendMessage" placeholder="메시지를 입력하세요..." />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';

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
</script>

<style scoped>
.chat-section {
  height: 45%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  background-color: #291707; 
  border: 1px solid #ccc;
  border-radius: 10px;
  padding: 10px;
  margin: 10px; 
  box-sizing: border-box;
}

.chat-tabs {
  display: flex;
  color: white;
  justify-content: space-around;
}

.chat-tabs div {
  cursor: pointer;
  padding: 10px;
  border: 1px solid #491414;
  flex: 1;
  text-align: center;
  margin: 0 5px;
  border-radius: 5px;
}

.chat-tabs .all {
  background-color: #291707;
}

.chat-tabs .chat {
  background-color: #1f2c39;
}

.chat-tabs .whisper {
  background-color: #380c30;
}

.chat-tabs .active {
  font-weight: bold;
  border: 2px solid #cbcde3;
}

.chat-window {
  flex-grow: 1;
  overflow-y: auto;
  padding: 10px;
  background-color: #291707;
  border: 1px solid #ccc;
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

input {
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  width: 100%;
  box-sizing: border-box;
  background-color: #ffffff; 
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
