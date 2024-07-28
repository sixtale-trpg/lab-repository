<template>
  <div class="calendar">
    <el-calendar v-model="value">
      <template #header="{ date }">
        <el-button-group>
          <el-button size="small" @click="changeMonth(-1)">
            &lsaquo;
          </el-button>
          <span>{{ formatDate(date) }}</span>
          <el-button size="small" @click="changeMonth(1)">
            &rsaquo;
          </el-button>
        </el-button-group>
      </template>
      <template #date-cell="{ data }">
        <button 
          class="date-button" 
          :class="{ 'is-today': isToday(data.date), 'selected-date': isSelected(data.date) }"
          @click="selectDate(data.date)">
          {{ new Date(data.date).getDate() }}
        </button>
      </template>
    </el-calendar>
    <!-- <button @click="showModal">일정 생성</button> -->

    <!-- Bootstrap Modal -->
    <div class="modal fade" id="eventModal" tabindex="-1" aria-labelledby="eventModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="eventModalLabel">일정 생성</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <p>선택된 날짜: {{ selectedDate }}</p>
            <div>
              <label for="startTime">시작 시간:</label>
              <select id="startTime" v-model="startTime">
                <option v-for="time in timeOptions" :key="time" :value="time">{{ time }}</option>
              </select>
            </div>
            <div>
              <label for="endTime">종료 시간:</label>
              <select id="endTime" v-model="endTime">
                <option v-for="time in timeOptions" :key="time" :value="time">{{ time }}</option>
              </select>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
            <button type="button" class="btn btn-primary" @click="saveEvent">저장</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap';

const value = ref(new Date());
const selectedDate = ref('');
const startTime = ref('');
const endTime = ref('');
const selectedDates = ref([]);

const timeOptions = computed(() => {
  const times = [];
  for (let hour = 0; hour < 24; hour++) {
    for (let minute = 0; minute < 60; minute += 30) {
      const formattedHour = hour.toString().padStart(2, '0');
      const formattedMinute = minute.toString().padStart(2, '0');
      times.push(`${formattedHour}:${formattedMinute}`);
    }
  }
  return times;
});

const formatDate = (date) => {
  const d = new Date(date);
  const year = d.getFullYear();
  const month = (d.getMonth() + 1).toString().padStart(2, '0');
  const day = d.getDate().toString().padStart(2, '0');
  return `${year}-${month}-${day}`;
};

const changeMonth = (increment) => {
  const newDate = new Date(value.value);
  newDate.setMonth(newDate.getMonth() + increment);
  value.value = newDate;
};

const goToToday = () => {
  value.value = new Date();
};

const showModal = () => {
  const modal = new bootstrap.Modal(document.getElementById('eventModal'));
  modal.show();
};

const selectDate = (date) => {
  selectedDate.value = formatDate(date);
  showModal();
};

const saveEvent = () => {
  console.log(`날짜: ${selectedDate.value}, 시작 시간: ${startTime.value}, 종료 시간: ${endTime.value}`);
  const modal = bootstrap.Modal.getInstance(document.getElementById('eventModal'));
  modal.hide();
};

const isToday = (date) => {
  const today = new Date();
  return (
    date.getFullYear() === today.getFullYear() &&
    date.getMonth() === today.getMonth() &&
    date.getDate() === today.getDate()
  );
};

const isSelected = (date) => {
  return formatDate(date) === selectedDate.value;
};
</script>

<style scoped>
.calendar {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  background-color: #291707;
  border-radius: 10px;
}

:deep(.el-calendar) {
  --el-calendar-border: none;
  --el-calendar-header-border-bottom: none;
  background-color: transparent;
}

:deep(.el-calendar__header) {
  display: flex;
  justify-content: center;
  align-items: center;
  color: #ffffff;
}

.custom-arrow-button {
  background-color: #444444; /* 원하는 배경색으로 변경 */
  border-color: #444444; /* 원하는 배경색으로 변경 */
  color: #ffffff; /* 버튼 글자 색상 */
}

:deep(.el-calendar-table thead th) {
  color: #ffffff;
  position: relative;
}

:deep(.el-calendar-table thead th::before) {
  content: attr(data-content);
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  text-align: center;
}

:deep(.el-calendar-table thead th:nth-child(1)) {
  visibility: hidden;
}
:deep(.el-calendar-table thead th:nth-child(1)::before) {
  content: '일';
  visibility: visible;
}

:deep(.el-calendar-table thead th:nth-child(2)) {
  visibility: hidden;
}
:deep(.el-calendar-table thead th:nth-child(2)::before) {
  content: '월';
  visibility: visible;
}

:deep(.el-calendar-table thead th:nth-child(3)) {
  visibility: hidden;
}
:deep(.el-calendar-table thead th:nth-child(3)::before) {
  content: '화';
  visibility: visible;
}

:deep(.el-calendar-table thead th:nth-child(4)) {
  visibility: hidden;
}
:deep(.el-calendar-table thead th:nth-child(4)::before) {
  content: '수';
  visibility: visible;
}

:deep(.el-calendar-table thead th:nth-child(5)) {
  visibility: hidden;
}
:deep(.el-calendar-table thead th:nth-child(5)::before) {
  content: '목';
  visibility: visible;
}

:deep(.el-calendar-table thead th:nth-child(6)) {
  visibility: hidden;
}
:deep(.el-calendar-table thead th:nth-child(6)::before) {
  content: '금';
  visibility: visible;
}

:deep(.el-calendar-table thead th:nth-child(7)) {
  visibility: hidden;
}
:deep(.el-calendar-table thead th:nth-child(7)::before) {
  content: '토';
  visibility: visible;
}

:deep(.el-calendar__body) {
  padding: 12px 20px 35px 20px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

:deep(.el-calendar-table) {
  width: 100%;
  table-layout: fixed;
}

:deep(.el-calendar-table td) {
  border: none;
  padding: 5px 0;
  text-align: center;
}

:deep(.el-calendar-table .el-calendar-day) {
  color: #ffffff; /* 기본 날짜 색상 */
}

.date-button {
  background: none;
  border: none;
  color: inherit;
  font: inherit;
  cursor: pointer;
  width: 30px;
  height: 30px;
  line-height: 30px;
  text-align: center;
  border-radius: 50%;
  display: inline-flex;
  justify-content: center;
  align-items: center;
}

.date-button.is-today {
  color: red !important; /* 오늘 날짜 글씨 색상 */
}

.date-button.selected-date {
  background-color: wheat;
  color: black;
}

.modal-content {
  background-color: #444444; /* 모달 창 배경색 변경 */
  color: #ffffff; /* 모달 창 글자 색상 변경 */
}

.modal-header, .modal-footer {
  border-color: #666666; /* 모달 헤더 및 푸터 경계선 색상 */
}

.modal-title {
  color: #ffffff; /* 모달 타이틀 색상 */
}

.btn-close {
  filter: invert(1); /* 닫기 버튼 색상 변경 (흰색) */
}

.btn-secondary, .btn-primary {
  background-color: #555555; /* 버튼 배경색 */
  border-color: #555555; /* 버튼 경계선 색상 */
}

.btn-primary {
  background-color: #007bff; /* 기본 primary 버튼 색상 */
  border-color: #007bff; /* 기본 primary 버튼 경계선 색상 */
}
</style>
