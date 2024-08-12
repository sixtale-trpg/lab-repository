<template>
  <div class="job-card" @mouseover="hover = true" @mouseleave="hover = false">
    <div class="card-border" :style="borderStyle"></div>
    <div :class="{ 'card-content': true, hidden: hover }">
      <img :src="image" alt="Job Image" class="job-image" />
      <h3 class="job-name">{{ name }}</h3>
    </div>
    <div v-if="hover" class="hover-name">
      <h2>{{ name }}</h2>
      <p>{{ description }}</p>
      <div v-if="!selected">
        <button @click="selectCard">선택</button>
      </div>
      <div v-else-if="selectedByMe">
        <button @click="editCard">수정하기</button>
        <button @click="deleteCard">삭제하기</button>
      </div>
      <div v-else>
        <p>선택한 유저: {{ selectedBy }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';

const props = defineProps({
  name: {
    type: String,
    required: true
  },
  image: {
    type: String,
    required: true
  },
  description: {
    type: String,
    required: true
  },
  selectedBy: {
    type: String,
    default: ''
  },
  generatedImages: {
    type: Array,
    default: () => []
  },
  selectedImage: {
    type: String,
    default: ''
  }
});

const emit = defineEmits(['select-card', 'edit-card', 'delete-card', 'open-character-sheet']);

const hover = ref(false);
const borderImage = require('@/assets/images/character/character_border.png');

const borderStyle = computed(() => ({
  backgroundImage: `url(${borderImage})`,
  backgroundSize: 'cover',
  backgroundRepeat: 'no-repeat',
  zIndex: 1,
  position: 'absolute',
  top: '0',
  left: '0',
  right: '0',
  bottom: '0'
}));

const selected = computed(() => props.selectedBy !== '');
const selectedByMe = computed(() => props.selectedBy === 'currentUser'); // currentUser를 실제 사용자 ID로 변경

const openCharacterSheet = () => {
  emit('open-character-sheet', {
    jobName: props.name,
    generatedImages: props.generatedImages,
    selectedImage: props.selectedImage
  });
};

const selectCard = () => {
  emit('select-card', props.name);
};

const editCard = () => {
  emit('edit-card', props.name);
};

const deleteCard = () => {
  emit('delete-card', props.name);
};
</script>

<style scoped>
.job-card {
  background-color: #444;
  color: white;
  padding: 3%;
  border-radius: 5px;
  text-align: center;
  width: 75%; /* 카드의 너비를 적절히 조정 */
  height: 95%; /* 카드의 높이를 조정 */
  /* margin: 10px; 카드 간격을 조정 */
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative; /* 추가 */
  /* overflow: hidden; 추가 */
}

.card-border {
  position: relative;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-size: cover;
  background-repeat: no-repeat;
  z-index: 1;
}

.card-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  transition: opacity 0.3s;
  z-index: 2; /* 컨텐츠가 테두리 위에 표시되도록 z-index 조정 */
}

.card-content.hidden {
  opacity: 0; /* hover 시 숨김 */
}

.job-image {
  width: 100%; /* 패딩 값을 제외한 너비 설정 */
  height: 100%; /* 패딩 값을 제외한 높이 설정 */
  object-fit: cover;
  /* margin-bottom: 10px; 이미지와 텍스트 사이의 간격을 추가 */
  transition: opacity 0.3s; /* 추가 */
}

.job-name {
  margin-top: 2%;
  font-size: 1.2rem; /* 이름 텍스트 크기 조정 */
}

.hover-name {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: white;
  text-align: center;
  z-index: 3; /* hover 상태의 이름이 가장 위에 표시되도록 z-index 조정 */
  width: 80%;
}

.hover-name h2 {
  margin: 0;
  font-size: 1.4rem; /* hover 상태의 이름 크기 조정 */
}

.hover-name p {
  font-size: 0.9rem; /* hover 상태의 설명 텍스트 크기 조정 */
  margin-top: 5px;
}

button {
  margin-top: 10px;
  padding: 5px 10px;
  border: none;
  border-radius: 5px;
  background-color: #007bff;
  color: white;
  cursor: pointer;
  transition: background-color 0.3s;
}

button:hover {
  background-color: #0056b3;
}
</style>
