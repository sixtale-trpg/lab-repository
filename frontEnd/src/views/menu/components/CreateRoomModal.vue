<template>
  <div class="create-room-modal" @click.self="closeModal">
    <div class="modal-content">
      <div class="modal-header">
        <img src="@/assets/images/lobby/Title_Light.png" alt="Title Background" class="title-background" />
        <h4>방 만들기</h4>
      </div>
      <div class="modal-body">
        <form @submit.prevent="createRoom">
          <div class="form-group card">
            <label for="roomName" class="input-label">방제목</label>
            <input id="roomName" v-model="roomName" type="text" />
          </div>
          
          <div class="form-group card">
            <label for="password" class="input-label">비밀번호</label>
            <div class="password-container">
              <input id="password" v-model="password" type="password" :disabled="!isPrivate" />
              <label for="isPrivate" class="checkbox-label">
                <input id="isPrivate" v-model="isPrivate" type="checkbox" class="styled-checkbox"/>
                비밀방
              </label>
            </div>
          </div>
          
          <div class="form-group card">
            <label for="rule" class="input-label">룰</label>
            <select id="rule" v-model="rule">
              <!-- 옵션 예시 -->
              <option value="룰1">룰1</option>
              <option value="룰2">룰2</option>
            </select>
          </div>
          
          <div class="form-group card">
            <label for="scenario" class="input-label">시나리오</label>
            <select id="scenario" v-model="scenario">
              <!-- 옵션 예시 -->
              <option value="시나리오1">시나리오1</option>
              <option value="시나리오2">시나리오2</option>
            </select>
          </div>
          
          <div class="form-group card">
            <div class="form-group-half">
              <label for="maxPlayers" class="input-label">인원</label>
              <select id="maxPlayers" class="margin-right" v-model="maxPlayers">
                <option v-for="n in 8" :key="n" :value="n">{{ n }}</option>
              </select>
            </div>
            <div class="form-group-half">
              <label for="playTime" class="input-label">예상 시간</label>
              <select id="playTime" v-model="playTime">
                <option v-for="n in [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 24, 48, 72]" :key="n" :value="n">{{ n }}시간</option>
              </select>
            </div>
          </div>
          
          <div class="form-group card">
            <div class="form-group-half">
              <label for="narrative" class="input-label">서사</label>
              <div class="checkbox-container">
                <input id="isLongStory" v-model="isLongStory" type="checkbox" class="styled-checkbox" @change="handleLongStoryChange" />
                <label for="isLongStory" class="checkbox-label no-card">장편</label>
                <input id="isShortStory" v-model="isShortStory" type="checkbox" class="styled-checkbox" @change="handleShortStoryChange" />
                <label for="isShortStory" class="checkbox-label no-card">단편</label>
              </div>
            </div>
            <div class="form-group-half">
              <label for="isVoice" class="input-label">음성</label>
              <input id="isVoice" v-model="isVoice" type="checkbox" class="styled-checkbox" />
            </div>
          </div>
          
          <div class="form-actions">
            <button type="button" @click="closeModal" class="cancel-button">취소</button>
            <button type="submit" class="create-button">생성</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { defineEmits } from 'vue';

const emit = defineEmits(['close']);
const roomName = ref('');
const isPrivate = ref(false);
const password = ref('');
const rule = ref('');
const scenario = ref('');
const maxPlayers = ref(6);
const playTime = ref(12);
const isShortStory = ref(false);
const isVoice = ref(false);

const isLongStory = computed({
  get: () => !isShortStory.value,
  set: (value) => { isShortStory.value = !value; }
});

const closeModal = () => {
  emit('close');
};

const createRoom = () => {
  const roomData = {
    roomName: roomName.value,
    isPrivate: isPrivate.value,
    password: isPrivate.value ? password.value : '',
    rule: rule.value,
    scenario: scenario.value,
    maxPlayers: maxPlayers.value,
    playTime: playTime.value,
    narrative: isShortStory.value ? '단편' : '장편',
    isVoice: isVoice.value,
  };
  
  console.log(roomData);
  closeModal();
};

const handleLongStoryChange = () => {
  if (!isLongStory.value) {
    isShortStory.value = false;
  }
};

const handleShortStoryChange = () => {
  if (!isShortStory.value) {
    isLongStory.value = false;
  }
};
</script>

<style scoped>
.create-room-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background: rgba(0, 0, 0, 0.5); /* 배경 어둡게 */
  z-index: 1000;
}

.modal-content {
  background: #19120C;
  padding: 40px;
  padding-top: 0px;
  border-radius: 20px;
  width: 969px;
  max-width: 90%;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  color: #E1D3A8;
  border: 1px solid #4A3A2E;
}

.modal-header {
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  margin-bottom: 20px;
}

.modal-header h4 {
  position: absolute;
  color: #E1D3A8; /* 제목 텍스트 색상 */
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  text-shadow: 0px 4px 4px rgba(0, 0, 0, 0.35);
}

.title-background {
  width: 100%;
  height: 60px; /* 적절한 높이 설정 */
}

.close-button {
  position: absolute;
  top: 10px;
  right: 10px;
  background: none;
  border: none;
  color: white;
  font-size: 1.5rem;
  cursor: pointer;
}

.modal-body {
  color: #E1D3A8;
}

.card {
  background: #19120C;
  border: 1px solid #4A3A2E;
  border-radius: 10px;
  padding: 10px;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
}

.form-group {
  display: flex;
  flex-direction: row;
  align-items: center;
  margin-bottom: 20px;
}

.input-label {
  width: 120px;
  margin-right: 10px;
  box-shadow: inset 0px 4px 4px rgba(157, 131, 112, 0.25);
  filter: drop-shadow(0px 4px 4px rgba(0, 0, 0, 0.25));
  height: 44.65px;
  background: #46362A;
  border: 1px solid rgba(0, 0, 0, 0.5);
  border-radius: 5px;
  display: flex;
  justify-content: center;
  align-items: center;
  color: #E1D3A8;
  text-shadow: 0px 4px 4px rgba(0, 0, 0, 0.35);
}

.form-group-half {
  width: 50%;
  display: flex;
  align-items: center;

}


.form-group input[type="text"],
.form-group input[type="password"],
.form-group select {
  flex: 1;
  padding: 8px;
  box-sizing: border-box;
  background: #57504A;
  border: 1px solid rgba(0, 0, 0, 0.5);
  border-radius: 5px;
  box-shadow: inset 0px 4px 4px rgba(157, 131, 112, 0.25);
  filter: drop-shadow(0px 4px 4px rgba(0, 0, 0, 0.25));
  color: #E1D3A8; /* 인풋 텍스트 색상 */
}

.form-group-inline {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}

.password-container {
  display: flex;
  align-items: center;
  flex: 1;
}

.password-container input[type="password"] {
  flex: 1;
}

.password-container .checkbox-label {
  margin-left: 10px;
  color: #E1D3A8; /* 레이블 텍스트 색상 */
  text-shadow: 0px 4px 4px rgba(0, 0, 0, 0.35);
}

.form-group input[type="checkbox"] {
  margin-left: 10px;
}

.checkbox-label {
  color: #E1D3A8; /* 체크박스 레이블 텍스트 색상 */
  text-shadow: 0px 4px 4px rgba(0, 0, 0, 0.35);
}

.checkbox-container {
  display: flex;
  gap: 10px;
  justify-content: center;
}

.checkbox-container .styled-checkbox {
  margin: 0 auto;
}

.form-group-half .styled-checkbox {
  margin-left: 0;
}

.no-card {
  background: none;
  box-shadow: none;
  border: none;
  height: auto;
  color: #E1D3A8;
  text-shadow: 0px 4px 4px rgba(0, 0, 0, 0.35);
}

.form-actions {
  display: flex;
  justify-content: center;
  gap: 20px;
}

button[type="button"].cancel-button {
  width: 140px;
  height: 40px;
  background: url('~@/assets/images/lobby/Cancel.png') no-repeat center center;
  background-size: cover;
  border: none;
  cursor: pointer;
  color: #E1D3A8;
}

button[type="submit"].create-button {
  width: 140px;
  height: 40px;
  background: url('~@/assets/images/lobby/Ok.png') no-repeat center center;
  background-size: cover;
  border: none;
  cursor: pointer;
  color: #E1D3A8;
}

.styled-checkbox {
  width: 25px;
  height: 25px;
  border-radius: 5px;
  background-color: #514339;
  border: 2px solid #2F251E;
  appearance: none;
  -webkit-appearance: none;
  cursor: pointer;
  position: relative;
}

.styled-checkbox:checked {
  background-color: #514339;
}

.styled-checkbox:checked::after {
  content: '';
  position: absolute;
  top: 5px;
  left: 5px;
  width: 10px;
  height: 10px;
  border: solid #A59E87;
  border-width: 0 2px 2px 0;
  transform: rotate(45deg);
}

.checkbox-label {
  display: flex;
  align-items: center;
  gap: 5px;
}

.margin-right {
  margin-right: 10px;
}
</style>
