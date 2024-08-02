<template>
  <div class="lobby-container">
    <div class="rectangle-box">
      <div class="button-container">
        <button class="view-all-rooms-button" @click="showAllRooms" :class="{ active: isAllRooms }">
          전체 방 보기
        </button>
        <button class="view-joined-rooms-button" @click="filterJoinedRooms" :class="{ active: !isAllRooms }">
          참가중인 방 보기
        </button>
      </div>
      <div class="rooms-container">
        <div v-for="room in paginatedRooms" :key="room.id" class="room-card" @click="enterRoom(room.id)">
          <div class="room-image">
            <img :src="defaultImage" alt="Room Image" />
          </div>
          <div class="room-details">
            <div class="room-title-container">
              <div class="room-number-box">
                <span class="room-number">{{ room.id }}</span>
              </div>
              <div class="room-title-box">
                <span class="room-title">{{ room.title }}</span>
              </div>
            </div>
            <div class="room-info">
              <div class="scenario-id-box">
                <span class="scenario-id">S#{{ room.scenario_id }}</span>
              </div>
              <div class="scenario-title-box">
                <span class="scenario-title">{{ room.scenario_name }}</span>
              </div>
            </div>
            <div class="room-footer">
              <div class="room-status">
                <img :src="getStatusImage(room.status)" alt="Room Status" />
              </div>
              <div class="room-icons">
                <img v-if="room.is_locked" src="@/assets/images/lobby/Key.png" alt="Locked Room" class="room-icon" />
                <img v-if="room.is_voice_chat" src="@/assets/images/lobby/Voice.png" alt="Voice Chat" class="room-icon" />
              </div>
              <div :class="['players-box', { 'full-room': room.current_count === room.max_count }]">
                <span>{{ room.current_count }} / {{ room.max_count }}</span>
              </div>
              <div class="room-type">
                <span>{{ room.is_short_story ? '단편' : '장편' }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="pagination-container">
        <div class="pagination">
          <button class="pagination-button" @click="prevPage" :disabled="currentPage === 1">
            <img src="@/assets/images/lobby/Arrow_Left.png" alt="Previous" class="pagination-arrow" />
          </button>
          <button class="pagination-button" @click="nextPage" :disabled="currentPage === totalPages">
            <img src="@/assets/images/lobby/Arrow_Right.png" alt="Next" class="pagination-arrow" />
          </button>
        </div>
        <button class="create-room-button" @click="openCreateRoomModal">방 만들기</button>
      </div>
    </div>
    <CreateRoomModal v-if="isCreateRoomModalOpen" @close="closeCreateRoomModal" />
  </div>
</template>



<script setup>
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import CreateRoomModal from '@/views/menu/components/CreateRoomModal.vue';

const router = useRouter();

const rooms = ref([
  { id: 1, title: '방제목 1', scenario_id: 1, current_count: 8, max_count: 8, is_short_story: false, status: 'PLAYING', is_locked: true, scenario_name: '레전드 드래곤', is_voice_chat: true, },
  { id: 2, title: '방제목 2', scenario_id: 2, current_count: 3, max_count: 6, is_short_story: true, status: 'WAITING', is_locked: false, scenario_name: '레전드 드래곤', is_voice_chat: true, },
  { id: 3, title: '방제목 3', scenario_id: 3, current_count: 4, max_count: 7, is_short_story: false, status: 'PLAYING', is_locked: false, scenario_name: '레전드 드래곤', is_voice_chat: false, },
  { id: 4, title: '방제목 4', scenario_id: 4, current_count: 2, max_count: 4, is_short_story: false, status: 'WAITING', is_locked: false, scenario_name: '레전드 드래곤', is_voice_chat: true, },
  { id: 5, title: '방제목 5', scenario_id: 5, current_count: 6, max_count: 8, is_short_story: false, status: 'UPCOMING', is_locked: true, scenario_name: '레전드 드래곤', is_voice_chat: false, },
  { id: 6, title: '방제목 6', scenario_id: 6, current_count: 6, max_count: 8, is_short_story: true, status: 'WAITING', is_locked: false, scenario_name: '레전드 드래곤', is_voice_chat: true, },
  { id: 7, title: '방제목 7', scenario_id: 7, current_count: 6, max_count: 8, is_short_story: false, status: 'PLAYING', is_locked: true, scenario_name: '레전드 드래곤', is_voice_chat: false, },
  { id: 8, title: '방제목 8', scenario_id: 8, current_count: 5, max_count: 8, is_short_story: false, status: 'UPCOMING', is_locked: true, scenario_name: '레전드 드래곤', is_voice_chat: true, },
  { id: 9, title: '방제목 9', scenario_id: 9, current_count: 4, max_count: 7, is_short_story: true, status: 'WAITING', is_locked: false, scenario_name: '레전드 드래곤', is_voice_chat: true, },
  { id: 10, title: '방제목 10', scenario_id: 10, current_count: 6, max_count: 6, is_short_story: false, status: 'UPCOMING', is_locked: false, scenario_name: '레전드 드래곤', is_voice_chat: false, },
  { id: 11, title: '방제목 11', scenario_id: 11, current_count: 2, max_count: 5, is_short_story: true, status: 'PLAYING', is_locked: true, scenario_name: '레전드 드래곤', is_voice_chat: true, },
  { id: 12, title: '방제목 12', scenario_id: 12, current_count: 3, max_count: 4, is_short_story: false, status: 'WAITING', is_locked: false, scenario_name: '레전드 드래곤', is_voice_chat: true, },
  { id: 13, title: '방제목 13', scenario_id: 13, current_count: 6, max_count: 6, is_short_story: true, status: 'UPCOMING', is_locked: true, scenario_name: '레전드 드래곤', is_voice_chat: true, },
  { id: 14, title: '방제목 14', scenario_id: 14, current_count: 5, max_count: 6, is_short_story: false, status: 'WAITING', is_locked: false, scenario_name: '레전드 드래곤', is_voice_chat: true, },
  { id: 15, title: '방제목 15', scenario_id: 15, current_count: 4, max_count: 8, is_short_story: true, status: 'PLAYING', is_locked: true, scenario_name: '레전드 드래곤', is_voice_chat: true, },
]);
const isAllRooms = ref(true);

const filteredRooms = ref(rooms.value.filter(room => room.status !== 'UPCOMING'));
const isCreateRoomModalOpen = ref(false);

const defaultImage = require('@/assets/images/scenario.png');

const currentPage = ref(1);
const roomsPerPage = 6;
const totalPages = computed(() => Math.ceil(filteredRooms.value.length / roomsPerPage));



const paginatedRooms = computed(() => {
  const start = (currentPage.value - 1) * roomsPerPage;
  return filteredRooms.value.slice(start, start + roomsPerPage);
});

const nextPage = () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value += 1;
  }
};

const prevPage = () => {
  if (currentPage.value > 1) {
    currentPage.value -= 1;
  }
};

const openCreateRoomModal = () => {
  isCreateRoomModalOpen.value = true;
};

const closeCreateRoomModal = () => {
  isCreateRoomModalOpen.value = false;
};

const showAllRooms = () => {
  isAllRooms.value = true;
  filteredRooms.value = rooms.value.filter(room => room.status !== 'UPCOMING');
};

const filterJoinedRooms = () => {
  isAllRooms.value = false;
  const joinedRoomIds = [1, 5, 10]; // 임시: 실제로는 백엔드에서 유저 ID를 통해 참가한 방 목록을 가져옵니다.
  filteredRooms.value = rooms.value.filter(room => 
    joinedRoomIds.includes(room.id) && (room.status === 'PLAYING' || room.status === 'UPCOMING')
  );
};

const getStatusImage = (status) => {
  switch (status) {
    case 'WAITING':
      return require('@/assets/images/lobby/WAITING.png');
    case 'PLAYING':
      return require('@/assets/images/lobby/PLAYING.png');
    default:
      return '';
  }
};

const enterRoom = (roomId) => {
  router.push({ name: 'Waiting', params: { roomId } });
};

// 백엔드 API 연동 로직 (주석 처리)
// const fetchRooms = async () => {
//   try {
//     const response = await axios.get('/api/rooms');
//     rooms.value = response.data;
//     filteredRooms.value = rooms.value.filter(room => room.status !== 'UPCOMING');
//   } catch (error) {
//     console.error('Error fetching rooms:', error);
//   }
// };

// onMounted(() => {
//   fetchRooms();
// });
</script>


<style scoped>
.lobby-container {
  padding: 20px;
  background: url('~@/assets/images/lobby/Background.png'); /* 배경 이미지 설정 */
  color: white;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
}

.rectangle-box {
  position: relative;
  width: 1471px;
  height: 900px;
  background: rgba(91, 78, 71, 0.15); /* 방 목록 박스 색상 및 투명도 설정 */
  box-shadow: inset -4px -4px 4px rgba(0, 0, 0, 0.25), inset 4px 4px 4px rgba(0, 0, 0, 0.25); /* 내부 그림자 설정 */
  border-radius: 39px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.rooms-container {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  width: 100%;
  justify-content: center;
  margin-top: 10px; /* 상단 여백 */
  overflow: hidden; /* 프레임을 넘어가는 내용 숨김 */
}

.room-card {
  display: flex;
  flex-direction: row; /* 가로 배치 */
  width: calc(50% - 40px);
  background-color: rgba(85, 75, 69, 0.7); /* 방 색깔 및 투명도 설정 */
  border-radius: 39px;
  padding: 20px;
  margin-bottom: 20px;
  position: relative;
  transition: transform 0.3s;
  box-shadow: 4px 4px 4px rgba(0, 0, 0, 0.25), inset 4px 4px 4px rgba(255, 255, 255, 0.15); /* 드롭 섀도우 및 내부 그림자 설정 */
  cursor: pointer; /* 커서가 포인터로 변경 */
}

.room-card:hover {
  transform: scale(1.05);
}

.room-image {
  width: 30%; /* 이미지 크기 조정 */
  padding-right: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.room-image img {
  width: 100%;
  height: auto;
  border-radius: 10px;
}

.room-details {
  width: 70%; /* 이미지 크기 조정에 따른 width 조정 */
  padding: 0 10px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.room-title-container {
  display: flex;
  align-items: center;
  gap: 10px;
  background: rgba(40, 34, 30, 0.31);
  box-shadow: inset 4px 4px 4px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  padding: 5px 10px;
  width: 100%;
  height: 46px;
}

.room-number-box {
  background: #554B45;
  box-shadow: 4px 4px 4px rgba(0, 0, 0, 0.25), inset 4px 4px 4px rgba(255, 255, 255, 0.15);
  border-radius: 10px;
  width: 91px;
  height: 35px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.room-number {
  font-family: 'Abhaya Libre ExtraBold';
  font-style: normal;
  font-weight: 800;
  font-size: 28px;
  line-height: 100%;
  color: rgba(255, 255, 255, 0.72);
}

.room-title-box {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
}

.room-title {
  font-family: 'Abhaya Libre Medium';
  font-style: normal;
  font-weight: 500;
  font-size: 24px;
  line-height: 100%;
  text-align: center;
  color: rgba(255, 255, 255, 0.67);
}

.room-info {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-top: 10px;
}

.scenario-title-box {
  background: rgba(40, 34, 30, 0.31);
  box-shadow: inset 4px 4px 4px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  padding: 5px 10px;
  display: flex;
  align-items: center;
}

.scenario-id-box {
  background: #554B45;
  box-shadow: 4px 4px 4px rgba(0, 0, 0, 0.25), inset 4px 4px 4px rgba(255, 255, 255, 0.15);
  border-radius: 10px;
  height: 35px;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 15%;
  text-align: center;
  font-family: 'Abhaya Libre ExtraBold';
  font-style: normal;
  font-weight: 800;
  font-size: 28px;
  line-height: 100%;
  color: rgba(255, 255, 255, 0.72);
  margin-left: 3%;
}

.scenario-title-box {
  width: 85%;
}

.scenario-id {
  font-size: 1rem;
  color: rgba(255, 255, 255, 0.67);
}

.scenario-title {
  font-size: 1rem;
  color: rgba(255, 255, 255, 0.67);
}

.room-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
}

.room-status {
  font-size: 1.5rem;
  font-family: 'Abhaya Libre ExtraBold';
  color: rgba(212, 222, 213, 1);
}

.status-waiting {
  color: #D4DED5;
  text-shadow: -3px -3px 0 rgba(36, 46, 37, 0.4);
  box-shadow: inset -10px -10px 10px rgba(0, 0, 0, 0.83);
}

.status-playing {
  color: #DEDAD4;
  text-shadow: -3px -3px 0 rgba(28, 37, 62, 0.86);
  box-shadow: inset -10px -10px 10px rgba(0, 0, 0, 0.83);
}

.room-icons {
  display: flex;
  gap: 10px;
}

.room-icon {
  width: 20px;
  height: 20px;
}

.players-box {
  background: rgba(40, 34, 30, 0.31);
  box-shadow: inset 4px 4px 4px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  padding: 5px 10px;
  color: white;
}

.players-box.full-room {
  background: rgba(22, 17, 12, 0.5); /* 더 어둡게 표시 */
  color: rgba(255, 255, 255, 0.72);
}

.room-type {
  background: #554B45;
  box-shadow: 4px 4px 4px rgba(0, 0, 0, 0.25), inset 4px 4px 4px rgba(255, 255, 255, 0.15);
  border-radius: 10px;
  padding: 5px 10px;
  color: white;
  font-size: 0.9rem;
  height: 100%;
  display: flex;
  align-items: center;
}

.pagination-container {
  display: flex;
  justify-content: center; /* 가운데 정렬 */
  align-items: center;
  margin-top: 20px;
  width: 100%;
  gap: 20px; /* 버튼 사이의 간격 설정 */
  position: relative; /* 상대적 위치 설정 */
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
}

.create-room-button {
  width: 200px;
  height: 50px;
  position: absolute;
  right: 10%; /* 오른쪽 끝에 배치 */
  background-color: rgba(101, 78, 53, 0.49);
  box-shadow: 4px 4px 4px rgba(0, 0, 0, 0.25), inset 4px 4px 4px rgba(255, 255, 255, 0.15);
  border-radius: 10px;
  color: rgb(214, 205, 170);
  padding: 10px 20px;
  border: none;
  border-radius: 10px;
  cursor: pointer;
}

.pagination-button {
  width: 126px;
  height: 50px;
  background: rgba(101, 78, 53, 0.49);
  box-shadow: 4px 4px 4px rgba(0, 0, 0, 0.25), inset 4px 4px 4px rgba(255, 255, 255, 0.15);
  border-radius: 10px;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  border: none;
}

.pagination-button:disabled {
  background-color: #555;
  cursor: not-allowed;
}

.pagination-arrow {
  width: auto; /* 가로 비율 자동 조정 */
  height: auto; /* 세로 비율 자동 조정 */
  max-width: 30px; /* 최대 너비 설정 */
  max-height: 19px; /* 최대 높이 설정 */
}

.button-container {
  display: flex;
  justify-content: center; /* 가운데 정렬 */
  align-items: center;
  gap: 20px; /* 버튼 사이의 간격 설정 */
  margin-bottom: 20px; /* 버튼과 방 목록 사이의 간격 설정 */
}

.view-all-rooms-button,
.view-joined-rooms-button {
  background: rgba(101, 78, 53, 0.49);
  box-shadow: 4px 4px 4px rgba(0, 0, 0, 0.25), inset 4px 4px 4px rgba(255, 255, 255, 0.15);
  border-radius: 10px;
  color: rgb(214, 205, 170);
  padding: 10px 20px;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  display: flex;
  align-items: center;
}

.view-all-rooms-button.active,
.view-joined-rooms-button.active {
  background: rgba(150, 150, 150, 0.49);
  color:  rgba(214, 205, 170, 0.847);
}
</style>
