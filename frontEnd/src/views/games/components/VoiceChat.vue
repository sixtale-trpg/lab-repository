<!-- 음성 채팅 버튼부분 따로 뺄까 생각 중  -->
<template>
    <div class="voice-chat">
      <button class="voice-chat-button" @click="toggleVoiceChat">
        <img :src="voiceIcon" alt="Voice chat" />
      </button>
    </div>
  </template>
  
  <script setup>
  import { computed, toRefs } from 'vue';
  import { useSessionStore } from '@/store/session';
  
  const props = defineProps({
    userId: {
      type: Number,
      required: true,
    },
  });
  
  const { userId } = toRefs(props);
  
  const sessionStore = useSessionStore();
  
  const voiceIcon = computed(() =>
    sessionStore.isVoiceOn(userId.value)
      ? require('@/assets/images/ingame/voice.png')
      : require('@/assets/images/ingame/voicex.png')
  );
  
  const toggleVoiceChat = () => {
    sessionStore.toggleVoiceChat(userId.value);
  };
  </script>
  
  <style scoped>
  .voice-chat-button {
    background: none;
    border: none;
    cursor: pointer;
    padding: 0;
  }
  
  .voice-chat-button img {
    width: 15px;
    height: 15px;
  }
  </style>
  