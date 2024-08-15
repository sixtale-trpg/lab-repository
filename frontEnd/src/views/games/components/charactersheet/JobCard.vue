<template>
  <div class="job-card" @mouseover="hover = true" @mouseleave="hover = false">
    <div class="card-border" :style="borderStyle"></div>
    <div :class="{ 'card-content': true, hidden: hover || disabled }">
      <h2 class="job-name">{{ name }}</h2>
      <img :src="image" alt="Job Image" class="job-image" />
    </div>
    <div v-if="hover || disabled" class="hover-name">
      <h2>{{ name }}</h2>
      <!-- 비활성화 상태라면 "선택 완료"와 삭제 버튼 표시 -->
      <div v-if="disabled">
        <p>선택 완료</p>
        <button @click="showDeleteModal" :style="deleteButtonStyle">삭제하기</button>
      </div>
      <!-- 비활성화 상태가 아니면 선택 버튼과 기타 UI 표시 -->
      <div v-else>
        <p>{{ description }}</p>
        <button @click="selectCard" :style="buttonStyle">직업 선택</button>
      </div>
    </div>
    <!-- 삭제 확인 모달 -->
    <CharacterSheetDeleteModal
      v-if="isDeleteModalVisible"
      :roomId="Number(roomId)" 
      @confirm="deleteCard"
      @close="hideDeleteModal"
    />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import borderImage from '@/assets/images/character/character_border.png';
import saveButtonImage from '@/assets/images/maps/background/save.png'; // "저장" 버튼 이미지
import startButtonImagePath from '@/assets/images/room/start_button.png'; // Start Button Image Path
import CharacterSheetDeleteModal from '@/views/games/components/Modal/CharacterSheetDeleteModal.vue';

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
  },
  disabled: {
    type: Boolean,
    default: false // 비활성화 상태를 나타내는 속성 추가
  },
  roomId: {
    type: Number,
    required: true,  // roomId를 필수로 설정
  }
});

const emit = defineEmits(['select-card', 'delete-card', 'open-character-sheet']);

const hover = ref(false);
const isDeleteModalVisible = ref(false);

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

const buttonStyle = computed(() => ({
  background: `url(${saveButtonImage}) no-repeat center center`,
  backgroundSize: 'cover',
  color: 'white',
  padding: '10px 20px',
  border: 'none',
  borderRadius: '5px',
  cursor: 'pointer',
  textAlign: 'center'
}));

const deleteButtonStyle = computed(() => ({
  background: `url(${startButtonImagePath}) no-repeat center center`,
  backgroundSize: 'cover',
  color: 'white',
  padding: '10px 20px',
  border: 'none',
  borderRadius: '5px',
  cursor: 'pointer',
  textAlign: 'center',
  transition: 'background-color 0.3s',
}));

const selectCard = () => {
  emit('select-card', props.name);
};

const showDeleteModal = () => {
  isDeleteModalVisible.value = true;
};

const hideDeleteModal = () => {
  isDeleteModalVisible.value = false;
};

const deleteCard = () => {
  emit('delete-card', props.name);
  hideDeleteModal();
};
</script>

<style scoped>
.job-card {
  background-color: #444;
  color: white;
  padding: 3%;
  border-radius: 5px;
  text-align: center;
  width: 75%;
  height: 95%;
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
}

.card-border {
  position: absolute;
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
  z-index: 2;
}

.card-content.hidden {
  opacity: 0; /* hover 시 숨김 또는 비활성화 시 숨김 */
}

.job-name {
  margin-top: 0;
  font-size: 1.6rem;
  font-weight: bold;
}

.job-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  margin-bottom: 10px;
  transition: opacity 0.3s;
}

.hover-name {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: white;
  text-align: center;
  z-index: 3;
  width: 80%;
}

.hover-name h2 {
  margin: 0;
  font-size: 1.4rem;
}

.hover-name p {
  font-size: 0.9rem;
  margin-top: 5px;
}

button {
  margin-top: 10px;
  padding: 5px 10px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
  color: white;
}

button:hover {
  background-color: rgba(0, 0, 0, 0.8);
}
</style>
