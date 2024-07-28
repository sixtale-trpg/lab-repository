<template>
  <div class="create-room-modal" @click.self="closeModal">
    <div class="modal-header">
      <h2>방 만들기</h2>
      <button class="close-button" @click="closeModal">×</button>
    </div>
    <div class="modal-body">
      <form @submit.prevent="createRoom">
        <div class="form-group">
          <label for="roomName">방 이름</label>
          <input id="roomName" v-model="roomName" type="text" />
        </div>
        
        <div class="form-group">
          <label for="isPublic">공개 여부</label>
          <small>체크시, 비밀번호 입력 필드는 사라집니다</small>
          <input id="isPublic" v-model="isPublic" type="checkbox" />
        </div>
        
        <div v-if="!isPublic" class="form-group">
          <label for="password">비밀번호 설정</label>
          <input id="password" v-model="password" type="password" />
        </div>
        
        <div class="form-group">
          <label for="scenario">시나리오 선택</label>
          <select id="scenario" v-model="scenario">
            <option value="기본 시나리오">기본 시나리오</option>
            <option value="나의 시나리오">나의 시나리오</option>
            <option value="관심 시나리오">관심 시나리오</option>
          </select>
        </div>
        
        <div class="form-group">
          <label for="maxPlayers">최대 인원</label>
          <select id="maxPlayers" v-model="maxPlayers">
            <option v-for="n in 8" :key="n" :value="n">{{ n }}</option>
          </select>
        </div>
        
        <div class="form-group">
          <label for="playTime">예상 플레이 시간</label>
          <input id="playTime" v-model="playTime" type="text" placeholder="예: 2시간" />
        </div>
        
        <div class="form-group">
          <label for="isVoice">보이스 플레이</label>
          <input id="isVoice" v-model="isVoice" type="checkbox" />
        </div>
        
        <div class="form-group">
          <label for="isCamera">화상 기능 추가</label>
          <input id="isCamera" v-model="isCamera" type="checkbox" />
        </div>
        
        <div class="form-group">
          <label>세션 회차</label>
          <div class="radio-group">
            <label>
              <input v-model="storyType" type="radio" value="단편" />
              단편 (1회차 엔딩)
            </label>
            <label>
              <input v-model="storyType" type="radio" value="장편" />
              장편 (다회차 엔딩)
            </label>
          </div>
        </div>
        
        <div class="form-actions">
          <button type="button" @click="closeModal" class="cancel-button">취소</button>
          <button type="submit" class="create-button">생성</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { defineEmits } from 'vue';

const emit = defineEmits(['close']);
const roomName = ref('');
const isPublic = ref(true);
const password = ref('');
const scenario = ref('');
const maxPlayers = ref(1);
const playTime = ref('');
const isVoice = ref(false);
const isCamera = ref(false);
const storyType = ref('단편');

const closeModal = () => {
  emit('close');
};

const createRoom = () => {
  // 방 생성 로직 작성해야함
  console.log({
    roomName: roomName.value,
    isPublic: isPublic.value,
    password: isPublic.value ? '' : password.value,
    scenario: scenario.value,
    maxPlayers: maxPlayers.value,
    playTime: playTime.value,
    isVoice: isVoice.value,
    isCamera: isCamera.value,
    storyType: storyType.value,
  });
  closeModal();
};
</script>

<style scoped>
.create-room-modal {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 90%;
  max-width: 600px;
  background-color: #f5f5f5;
  border: 1px solid #ccc;
  padding: 20px;
  border-radius: 10px;
  z-index: 1000;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  max-height: 90vh;
  overflow-y: auto;
}

@media (max-width: 600px) {
  .create-room-modal {
    width: 90%;
  }
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  background-color: #091d31;
  padding: 10px;
  border-radius: 10px 10px 0 0;
}

.modal-header h2 {
  margin: 0;
  color: white;
}

.close-button {
  background: none;
  border: none;
  color: white;
  font-size: 1.5rem;
  cursor: pointer;
}

.modal-body {
  color: #333;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  color: #333;
}

.form-group input[type="text"],
.form-group input[type="password"],
.form-group select {
  width: 100%;
  padding: 8px;
  box-sizing: border-box;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.form-group input[type="checkbox"] {
  margin-left: 10px;
}

.radio-group {
  display: flex;
  gap: 10px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

button[type="button"],
button[type="submit"] {
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

button[type="button"].cancel-button {
  background-color: #f44336;
  color: white;
}

button[type="submit"].create-button {
  background-color: #4caf50;
  color: white;
}
</style>
