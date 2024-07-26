<template>
  <div class="modal" @click.self="closeModal">
    <div class="modal-content">
      <div class="modal-header">시나리오 정보</div>
      <div class="modal-body">
        <h3>{{ scenario.title }}</h3>
        <div class="scenario-details">
          <div class="image-container">
            <img :src="scenario.image_url" alt="Scenario Image" class="scenario-image" />
          </div>
          <div class="info-container">
            <div class="info-box">
              <p><strong>제작자:</strong> {{ scenario.writer_id }}</p>
            </div>
            <div class="info-box">
              <p><strong>요약:</strong> {{ scenario.summary }}</p>
            </div>
            <div class="info-box">
              <p><strong>설명:</strong> {{ scenario.description }}</p>
            </div>
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <button @click="closeModal">닫기</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits, ref, onMounted } from 'vue';
import axios from 'axios';

const props = defineProps({
  scenario: Object
});

const emit = defineEmits(['close']);

// 더미 데이터를 위한 임시 객체
const scenario = ref({
  title: '왕자와 죽음의 개',
  writer_id: 'writer123',
  summary: '이 시나리오는...',
  description: '상세 설명 내용...',
  image_url: 'https://via.placeholder.com/150'
});

// 백엔드에서 시나리오 정보를 받아오는 로직 (주석처리)
/*
onMounted(async () => {
  try {
    const response = await axios.get(`/api/scenario/${props.scenarioId}`);
    scenario.value = response.data;
  } catch (error) {
    console.error('Error fetching scenario details:', error);
  }
});
*/

const closeModal = () => {
  emit('close');
};
</script>

<style scoped>
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background-color: #291707;
  border: 1px solid white;
  border-radius: 10px;
  padding: 20px;
  width: 600px; 
  text-align: center;
}

.modal-header {
  font-size: 1.5em;
  margin-bottom: 20px;
  color: white;
}

.modal-body {
  color: white;
}

.scenario-details {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
}

.image-container {
  flex: 1;
  margin-right: 10px;
}

.scenario-image {
  width: 100%;
  border-radius: 10px;
}

.info-container {
  flex: 2;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.info-box {
  background-color: #564307;
  padding: 10px;
  border-radius: 5px;
  color: white;
}

.modal-footer {
  margin-top: 20px;
}

.modal-footer button {
  padding: 10px 20px;
  background-color: #564307;
  color: white;
}
</style>
