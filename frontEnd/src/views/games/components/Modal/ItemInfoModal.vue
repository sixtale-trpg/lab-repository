<template>
  <div class="modal-overlay" @click.self="close">
    <div class="modal-content" :style="modalContentStyle">
      <div class="modal-header">
        <div class="modal-title-container">
          <img src="@/assets/images/character_sheet/title.png" alt="제목" class="modal-title-image">
          <h2 class="modal-title-text">{{ item.name }}</h2>
        </div>
      </div>
      <div class="modal-body" :style="modalBodyStyle">
        <div class="item-left">
          <img :src="item.image" :alt="item.name" class="item-image" />
        </div>
        <div class="item-right">
          <div class="info-item">
            <div class="title-container">
              <img src="@/assets/images/character_sheet/nickname_light.png" alt="무게 배경" class="title-image">
              <span class="title-text">무게</span>
            </div>
            <div class="info-text">{{ item.weight }} kg</div>
          </div>
          <div class="info-item">
            <div class="title-container">
              <img src="@/assets/images/character_sheet/nickname_light.png" alt="횟수 배경" class="title-image">
              <span class="title-text">현재 횟수</span>
            </div>
            <div class="info-text">
              <img v-if="isGM" src="@/assets/images/ingame/Minus.png" alt="감소" class="control-button" @click="decreaseCount">
              <span>{{ localItem.count }}</span>
              <img v-if="isGM" src="@/assets/images/ingame/Plus.png" alt="증가" class="control-button" @click="increaseCount">
            </div>
          </div>
          <div v-if="showWarning" class="warning-message">
            하중을 넘어섬으로 추가할 수 없습니다.
          </div>
        </div>
      </div>
      <div class="item-description-container">
        <p class="item-description">{{ item.description }}</p>
      </div>
      <div class="modal-footer" :style="modalFooterStyle">
        <div class="weight-info">
          현재 하중: {{ currentWeight }} / 전체 하중: {{ totalWeight }}
        </div>
        <div class="footer-buttons">
          <button class="footer-button" :style="closeButtonStyle" @click="closeModal">닫기</button>
          <button v-if="isGM" class="footer-button save-button" :style="saveButtonStyle" @click="saveChanges">저장</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, defineProps, defineEmits, watch } from 'vue';

const props = defineProps({
  item: {
    type: Object,
    required: true,
  },
  isGM: {
    type: Boolean,
    default: true,
  },
  currentWeight: {
    type: Number,
    required: true,
  },
  totalWeight: {
    type: Number,
    required: true,
  },
});

const emit = defineEmits(['close', 'update-item']);

const localItem = ref({ ...props.item });
const showWarning = ref(false);

const closeModal = () => {
  emit('close');
};

const decreaseCount = () => {
  if (localItem.value.count > 1) {
    localItem.value.count--;
    showWarning.value = false;
  }
};

const increaseCount = () => {
  const newWeight = props.currentWeight + (localItem.value.weight * (localItem.value.count + 1));
  if (newWeight > props.totalWeight) {
    showWarning.value = true;
  } else {
    localItem.value.count++;
    showWarning.value = false;
  }
};

const saveChanges = () => {
  if (!showWarning.value) {
    emit('update-item', localItem.value);
    closeModal();
  }
};

// Watch for changes in props.item and update localItem accordingly
watch(
  () => props.item,
  (newItem) => {
    localItem.value = { ...newItem };
  }
);

const modalContentStyle = computed(() => ({
  background: `url(${require('@/assets/images/character_sheet/main_background.png')}) no-repeat center center`,
  backgroundSize: 'cover',
}));

const modalBodyStyle = computed(() => ({
  background: `url(${require('@/assets/images/character_sheet/tab_background.png')}) no-repeat center center`,
  backgroundSize: 'cover',
  marginTop: '10px',
  display: 'flex',
  flexDirection: 'row',
  alignItems: 'center',
  padding: '20px',
  height: '200px',
}));

const modalFooterStyle = computed(() => ({
  background: `url(${require('@/assets/images/character_sheet/main_background.png')}) no-repeat center center`,
  backgroundSize: 'cover',
  display: 'flex',
  justifyContent: 'space-between',
  alignItems: 'center',
}));

// 닫기 버튼 스타일
const closeButtonStyle = computed(() => ({
  background: `url(${require('@/assets/images/maps/background/close.png')}) no-repeat center center`,
  backgroundSize: 'cover',
}));

// 저장 버튼 스타일
const saveButtonStyle = computed(() => ({
  background: `url(${require('@/assets/images/maps/background/save.png')}) no-repeat center center`,
  backgroundSize: 'cover',
}));
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: transparent;
  padding: 0;
  border-radius: 10px;
  width: 550px; /* 크기 줄임 */
  max-width: 90%;
  height: 450px; /* 크기 줄임 */
  max-height: 90%;
  position: relative;
  color: #fff;
  overflow: hidden;
}

.modal-header {
  text-align: center;
  margin-bottom: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  width: 100%;
  overflow: hidden;
}

.modal-title-container {
  position: relative;
  width: 100%;
}

.modal-title-image {
  width: 100%;
  height: auto;
  object-fit: cover;
}

.modal-title-text {
  position: absolute;
  top: 50%;
  left: 15%;
  transform: translate(-50%, -50%);
  color: white;
  font-size: 1.5rem;
}

.modal-body {
  padding: 20px;
  display: flex;
  flex-direction: row;
  align-items: center;
}

.modal-body::-webkit-scrollbar {
  width: 8px;
}

.modal-body::-webkit-scrollbar-track {
  background: #201805;
  border-radius: 5px;
}

.modal-body::-webkit-scrollbar-thumb {
  background-color: #855e2fee;
  border-radius: 5px;
  border: 2px solid #201805;
}

.item-left {
  flex: 0 0 150px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.item-image {
  width: 150px;
  height: 150px;
}

.item-right {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  padding-left: 10px;
  height: 150px;
  justify-content: space-around;
}

.info-item {
  display: flex;
  align-items: center;
}

.title-container {
  position: relative;
  display: inline-block;
  margin-right: 10px;
}

.title-image {
  display: block;
}

.title-text {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: #fff;
  font-size: 1.4rem;
  white-space: nowrap;
}

.info-box {
  position: relative;
  display: flex;
  align-items: center;
  height: 50px;
}

.info-box-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.info-text {
  color: #fff;
  font-size: 1.4rem;
  display: flex;
  align-items: center;
}

.control-button {
  width: 20px;
  height: 20px;
  cursor: pointer;
  margin: 0 10px; /* 여백 추가 */
}

.item-description-container {
  width: 100%;
  padding: 20px;
  box-sizing: border-box;
  background: rgba(0, 0, 0, 0.2);
  border-top: 1px solid rgba(255, 255, 255, 0.2);
  height: 150px; /* 크기 줄임 */
  overflow-y: auto;
}

.item-description {
  color: white;
  font-size: 1rem;
  margin-bottom: 10px;
  text-align: center;
}

.modal-footer {
  display: flex;
  padding: 10px;
  border-radius: 0 0 10px 10px;
}

.footer-buttons {
  display: flex;
  gap: 10px; /* 버튼 간의 간격 조정 */
}

.footer-button {
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  background-size: cover;
  color: #fff;
  cursor: pointer;
}

.footer-button:hover {
  transform: translateY(-2px); /* 버튼 호버 시 살짝 위로 움직이는 효과 */
}

.warning-message {
  color: red;
  font-size: 1rem;
  margin-top: 10px;
}

.weight-info {
  align-self: center;
  font-size: 1rem;
  color: #fff;
  margin-left: 30px;
}
</style>
