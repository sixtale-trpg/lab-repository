<template>
  <div class="lobby-container">
    <div class="button-container">
      <button class="create-room-button" @click="openCreateRoomModal">방 만들기</button>
      <button class="view-all-rooms-button" @click="showAllRooms">전체 방 보기</button>
      <button class="view-joined-rooms-button" @click="filterJoinedRooms">참가중인 방 보기</button>
    </div>
    <div class="rectangle-box">
      <div class="rooms-container">
        <div v-for="room in paginatedRooms" :key="room.id" class="room-card">
          <div class="room-image">
            <img :src="defaultImage" alt="Room Image" />
          </div>
          <div class="room-details">
            <div class="room-header">
              <div class="room-title-container">
                <div class="room-number-box">
                  <span class="room-number">{{ room.id }}</span>
                </div>
                <div class="room-title-box">
                  <span class="room-title">{{ room.title }}</span>
                </div>
              </div>
              <div class="room-info">
                <span>{{ room.is_short_story ? '단편' : '장편' }}</span>
              </div>
            </div>
            <div class="room-footer">
              <div :class="['status-box', getStatusClass(room.status)]">
                <span>{{ getStatus(room.status) }}</span>
              </div>
              <div class="players-box">
                <span>{{ room.current_count }} / {{ room.max_count }}</span>
              </div>
              <button v-if="room.status === 'WAITING'" @click="enterRoom(room.id)" class="enter-button">입장</button>
            </div>
          </div>
        </div>
      </div>
      <div class="pagination">
        <button class="pagination-button" @click="prevPage" :disabled="currentPage === 1">
          <img src="@/assets/images/lobby/Arrow_Left.png" alt="Previous" class="pagination-arrow" />
        </button>
        <button class="pagination-button" @click="nextPage" :disabled="currentPage === totalPages">
          <img src="@/assets/images/lobby/Arrow_Right.png" alt="Next" class="pagination-arrow" />
        </button>
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
  { id: 1, title: '방제목 1', scenario_id: 1, current_count: 4, max_count: 8, is_short_story: false, status: 'PLAYING', is_locked: true },
  { id: 2, title: '방제목 2', scenario_id: 2, current_count: 3, max_count: 6, is_short_story: true, status: 'WAITING', is_locked: false },
  { id: 3, title: '방제목 3', scenario_id: 3, current_count: 4, max_count: 7, is_short_story: false, status: 'PLAYING', is_locked: false },
  { id: 4, title: '방제목 4', scenario_id: 4, current_count: 2, max_count: 4, is_short_story: false, status: 'WAITING', is_locked: false },
  { id: 5, title: '방제목 5', scenario_id: 5, current_count: 6, max_count: 8, is_short_story: false, status: 'UPCOMING', is_locked: true },
  { id: 6, title: '방제목 6', scenario_id: 6, current_count: 6, max_count: 8, is_short_story: true, status: 'WAITING', is_locked: false },
  { id: 7, title: '방제목 7', scenario_id: 7, current_count: 6, max_count: 8, is_short_story: false, status: 'PLAYING', is_locked: true },
  { id: 8, title: '방제목 8', scenario_id: 8, current_count: 5, max_count: 8, is_short_story: false, status: 'UPCOMING', is_locked: true },
  { id: 9, title: '방제목 9', scenario_id: 9, current_count: 4, max_count: 7, is_short_story: true, status: 'WAITING', is_locked: false },
  { id: 10, title: '방제목 10', scenario_id: 10, current_count: 4, max_count: 6, is_short_story: false, status: 'UPCOMING', is_locked: false },
  { id: 11, title: '방제목 11', scenario_id: 11, current_count: 2, max_count: 5, is_short_story: true, status: 'PLAYING', is_locked: true },
  { id: 12, title: '방제목 12', scenario_id: 12, current_count: 3, max_count: 4, is_short_story: false, status: 'WAITING', is_locked: false },
  { id: 13, title: '방제목 13', scenario_id: 13, current_count: 6, max_count: 6, is_short_story: true, status: 'UPCOMING', is_locked: true },
  { id: 14, title: '방제목 14', scenario_id: 14, current_count: 5, max_count: 6, is_short_story: false, status: 'WAITING', is_locked: false },
  { id: 15, title: '방제목 15', scenario_id: 15, current_count: 4, max_count: 8, is_short_story: true, status: 'PLAYING', is_locked: true },
]);

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
  filteredRooms.value = rooms.value.filter(room => room.status !== 'UPCOMING');
};

const filterJoinedRooms = () => {
  const joinedRoomIds = [1, 5, 10]; // 임시: 실제로는 백엔드에서 유저 ID를 통해 참가한 방 목록을 가져옵니다.
  filteredRooms.value = rooms.value.filter(room => 
    joinedRoomIds.includes(room.id) && (room.status === 'PLAYING' || room.status === 'UPCOMING')
  );
};

const getStatus = (status) => {
  switch (status) {
    case 'WAITING':
      return 'WAITING';
    case 'PLAYING':
      return 'PLAYING';
    default:
      return status;
  }
};

const getStatusClass = (status) => {
  switch (status) {
    case 'WAITING':
      return 'status-waiting';
    case 'PLAYING':
      return 'status-playing';
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
  background: url('/path/to/your/background.png'); /* 배경 이미지 추가 */
  color: white;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
}

.button-container {
  width: 100%;
  display: flex;
  justify-content: flex-start;
  gap: 10px; /* 버튼 사이의 여백을 위해 */
  margin-bottom: 20px;
}

.create-room-button, .view-all-rooms-button, .view-joined-rooms-button {
  background-color: #cc8a00; /* 짙은 노란색 계열 */
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.rectangle-box {
  position: relative;
  width: 1471px;
  height: 835px;
  background: rgba(91, 78, 71, 0.15);
  mix-blend-mode: exclusion;
  box-shadow: inset -4px -4px 4px rgba(0, 0, 0, 0.25), inset 4px 4px 4px rgba(0, 0, 0, 0.25);
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
  width: calc(50% - 40px); /* 카드 사이의 여백을 고려한 너비 */
  background-color: rgba(91, 78, 71, 0.15);
  border-radius: 39px;
  padding: 20px;
  margin-bottom: 20px;
  position: relative;
  transition: transform 0.3s;
  box-shadow: inset -4px -4px 4px rgba(0, 0, 0, 0.25), inset 4px 4px 4px rgba(0, 0, 0, 0.25);
}

.room-card:hover {
  transform: scale(1.05);
}

.room-image {
  width: 25%;
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
  width: 75%;
  padding: 0 10px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.room-header, .room-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
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
  font-size: 0.9rem;
  color: #ccc;
}

.status-box {
  position: absolute;
  width: 177px;
  height: 45px;
  font-family: 'Abhaya Libre ExtraBold';
  font-style: normal;
  font-weight: 800;
  font-size: 43px;
  line-height: 100%;
  text-align: center;
}

.status-box.status-waiting {
  color: #D4DED5;
  border: 3px solid rgba(36, 46, 37, 0.4);
}

.status-box.status-playing {
  color: #DEDAD4;
  border: 3px solid rgba(28, 37, 62, 0.86);
}

.players-box {
  background: #554B45;
  box-shadow: 4px 4px 4px rgba(0, 0, 0, 0.25), inset 4px 4px 4px rgba(255, 255, 255, 0.15);
  border-radius: 10px;
  padding: 5px 10px;
  color: white;
}

.enter-button {
  background-color: #4caf50;
  color: white;
  padding: 5px 15px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.enter-button:hover {
  background-color: #45a049;
}

.lock-icon {
  width: 20px;
  height: 20px;
}

.pagination {
  display: flex;
  justify-content: space-between; /* 좌우 버튼이 양 끝으로 가도록 */
  align-items: center;
  margin-top: 20px;
  width: 100%; /* 버튼들이 양 끝으로 가도록 충분한 너비 설정 */
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
  width: 30px;
  height: 19px;
}
</style>
