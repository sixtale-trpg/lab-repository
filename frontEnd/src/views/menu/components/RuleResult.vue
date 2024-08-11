<template>
    <div class="section">
      <div v-for="(message, i) in messages" :key="i" class="mb-4" :class="{ 'text-right': message.role === 'user', 'text-left': message.role !== 'user' }">
        <b-badge variant="primary" class="mb-2">
          {{ capitalize(message.role) }}
        </b-badge>
        <b-card body-class="px-3 py-2 message-card" style="border-radius: 18px; background-color: #CFD8DC;">
          <div class="content" v-html="formatMessageContent(message.content)"></div>
        </b-card>
      </div>
    </div>
  </template>
  
  <script setup>
  import MarkdownIt from 'markdown-it';
  import { defineProps } from 'vue';
  
  defineProps({
    messages: {
      type: Array,
      default: () => [],
    },
  });
  
  const md = new MarkdownIt();
  
  const formatMessageContent = (content) => {
    return md.render(content); // Markdown을 HTML로 변환
  };
  
  const capitalize = (v) => `${v.charAt(0).toUpperCase()}${v.slice(1)}`;
  </script>
  
  <style scoped>
  .section {
    overflow-y: auto; 
    overflow-x: hidden; 
  }
  
  .message-card {
    max-width: 10px !;
  }
  
  .text-right {
    text-align: right;
  }
  
  .text-left {
    text-align: left;
  }

  </style>
  