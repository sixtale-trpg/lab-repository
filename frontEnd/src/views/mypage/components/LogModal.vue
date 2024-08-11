<template>
  <div class="log-modal" @click.self="closeModal">
    <div class="log-modal-content">
      <h2 class="log-title">플레이 로그</h2>
      <div class="log-details">
        <h3>00{{ room.id }}{{ room.title }}</h3>
        <p>{{ room.scenario_name }}</p>
      </div>
      <div class="log-tabs">
        <button
          v-for="(tab, index) in tabs"
          :key="index"
          :class="['log-tab', { active: activeTab === tab }]"
          @click="activeTab = tab"
        >
          {{ tab }}
        </button>
      </div>
      <div class="log-content">
        <!-- 대기 탭 내용 -->
        <div v-if="activeTab === '대기'">
          <p v-for="(entry, index) in logEntriesWait" :key="index" class="log-entry">
            <span :class="entry.type">{{ entry.user }}:</span> {{ entry.message }}
          </p>
        </div>
        <!-- 플레이 탭 내용 -->
        <div v-if="activeTab === '플레이'">
          <p v-for="(entry, index) in logEntriesPlay" :key="index" class="log-entry">
            <span :class="entry.type">{{ entry.user }}:</span> {{ entry.message }}
          </p>
        </div>
      </div>
      <button class="close-button" @click="closeModal">X</button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from "vue";
import { defineEmits } from "vue";

const emit = defineEmits(["close"]);

const closeModal = () => {
  emit("close");
};

// const isOpen = ref(true); // 모달이 열려 있는지 여부
const activeTab = ref("대기"); // 현재 활성화된 탭
const tabs = ref(["대기", "플레이"]); // 탭 목록

// props로 데이터 받아야 함
const room = ref({
  id: 5,
  title: "부서진 오벨리스크 세션",
  scenario_name: "S# 드래곤의 전설",
});
//대기 탭 로그 데이터 -- 형식 봐야 함
const logEntriesWait = ref([
  { type: "GM", user: "GM", message: "이제 던전월드로 떠나볼까요?" },
  { type: "플레이어", user: "플레이어1", message: "여행은 새로운 장소로 떠나는 것 같아요." },
  { type: "플레이어", user: "플레이어2", message: "동의해요!" },
]);
// 플레이 탭 로그 데이터
const logEntriesPlay = ref([
  { type: "GM", user: "GM", message: "던전에 진입했습니다." },
  { type: "플레이어", user: "플레이어1", message: "탐색을 시작합니다." },
  { type: "플레이어", user: "플레이어2", message: "제가 앞장설게요." },
]);
</script>

<style>
.log-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.log-modal-content {
  background-color: #3a3a3c;
  border-radius: 18px;
  padding: 20px;
  max-width: 600px;
  width: 80%;
  box-shadow: 0px 0px 15px rgba(0, 0, 0, 0.3);
}

.log-title {
  color: white;
  margin-bottom: 10px;
}

.log-details h3 {
  color: #f0f0f0;
  margin: 0;
}

.log-details p {
  color: #bfbfc0;
  margin: 5px 0 20px;
}

.log-tabs {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.log-tab {
  background-color: #343434;
  color: #f0f0f0;
  border: none;
  padding: 10px 20px;
  cursor: pointer;
  border-radius: 5px;
  flex: 1;
  text-align: center;
}

.log-tab.active {
  background-color: #007bff;
}

.log-content {
  max-height: 300px;
  overflow-y: auto;
  background-color: #2c2c2e;
  padding: 15px;
  border-radius: 10px;
}

.log-entry {
  margin-bottom: 10px;
  color: #f0f0f0;
}

.log-entry span.GM {
  color: #007bff;
}

.log-entry span.플레이어 {
  color: #ff7f50;
}

.close-button {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
  margin-top: 20px;
  display: block;
  width: 100%;
  text-align: center;
}

.close-button:hover {
  background-color: #0056b3;
}
</style>
