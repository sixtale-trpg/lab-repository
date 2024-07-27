<template>
  <div class="lobby-container">
    <div class="button-container">
      <button class="create-room-button" @click="openCreateRoomModal">방 만들기</button>
      <button class="view-all-rooms-button" @click="showAllRooms">전체 방 보기</button>
      <button class="view-joined-rooms-button" @click="filterJoinedRooms">참가중인 방 보기</button>
    </div>
    <div class="rooms-container">
      <div v-for="room in filteredRooms" :key="room.id" class="room-card">
        <div class="room-image">
          <img :src="defaultImage" alt="Room Image" />
        </div>
        <div class="room-details">
          <div class="room-header">
            <div class="room-title">
              <span>방번호: {{ room.id }}</span>
              <span class="title">{{ room.title }}</span>
              <img v-if="room.is_locked" src="@/assets/images/room/lock.png" alt="Lock Icon" class="lock-icon" />
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
    <CreateRoomModal v-if="isCreateRoomModalOpen" @close="closeCreateRoomModal" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
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
  // 상태도 백엔드에서 가져와야한다.
  switch (status) {
    case 'WAITING':
      return '대기중';
    case 'PLAYING':
      return '게임중';
    case 'UPCOMING':
      return '예정';
    case 'COMPLETE':
      return '완료';
    default:
      return '알 수 없음';
  }
};

const getStatusClass = (status) => {
  switch (status) {
    case 'WAITING':
      return 'status-waiting';
    case 'PLAYING':
      return 'status-playing';
    case 'UPCOMING':
      return 'status-upcoming';
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
  background: #1e1e1e;
  color: white;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
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

.rooms-container {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  width: 100%;
  justify-content: center;
  margin-top: 10px; /* 상단 여백 */
}

.room-card {
  display: flex;
  flex-direction: row; /* 가로 배치 */
  width: calc(50% - 40px); /* 카드 사이의 여백을 고려한 너비 */
  background-color: #3c2c23;
  border-radius: 10px;
  padding: 20px;
  margin-bottom: 20px;
  position: relative;
  transition: transform 0.3s;
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

.room-title {
  display: flex;
  align-items: center;
  gap: 10px;
}

.title {
  font-size: 1.2rem;
  font-weight: bold;
}

.room-info {
  font-size: 0.9rem;
  color: #ccc;
}

.status-box, .players-box {
  background-color: #555;
  padding: 5px 10px;
  border-radius: 5px;
  color: white;
}

.status-box.status-waiting {
  background-color: #555; /* 기존 색상 */
}

.status-box.status-playing {
  background-color: red;
}

.status-box.status-upcoming {
  background-color: blue;
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
</style>
