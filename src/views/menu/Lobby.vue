<template>
  <div class="lobby-container">
    <div class="room-table-container">
      <button class="create-room-button" @click="openCreateRoomModal">방 만들기</button>
      <table class="room-table">
        <thead>
          <tr>
            <th>번호</th>
            <th>방제목</th>
            <th>인원 수</th>
            <th>서사</th>
            <th>현재 상태</th>
            <th>입장</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="room in rooms" :key="room.id">
            <td>{{ room.id }}</td>
            <td>
              {{ room.title }}
              <img v-if="!room.isPublic" :src="require('@/assets/images/room/lock.png')" alt="비공개" class="icon" />
            </td>
            <td>{{ room.currentPlayers }} / {{ room.totalPlayers }}</td>
            <td>{{ room.type }}</td>
            <td>
              {{ room.status }}
              <img :src="room.is_voice ? require('@/assets/images/room/voice.png') : require('@/assets/images/room/voicex.png')" 
                   alt="Voice Status" class="icon" />
              <img :src="room.is_camera ? require('@/assets/images/room/video.png') : require('@/assets/images/room/videox.png')" 
                   alt="Video Status" class="icon" />
            </td>
            <td>
              <button v-if="room.status === '대기중'" @click="enterRoom(room.id)" class="enter-button">입장</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div v-if="!isMessengerOpen" class="friend-icon" @click="openMessenger">
      <img src="@/assets/images/messenger/friend-icon.png" alt="Friends" />
    </div>
    <Messenger v-if="isMessengerOpen" @close="closeMessenger" @startChat="openChat" />
    <CreateRoomModal v-if="isCreateRoomModalOpen" @close="closeCreateRoomModal" />
    <Chat v-if="isChatOpen" @close="closeChat" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import Messenger from './components/Messenger.vue';
import CreateRoomModal from './components/CreateRoomModal.vue';
import Chat from './components/Chat.vue';

const router = useRouter();

const rooms = ref([
  { id: 1, title: '방제목 입니다.우와아아아', currentPlayers: 4, totalPlayers: 8, type: '장편', status: '게임중', isPublic: false, is_voice: true, is_camera: false },
  { id: 2, title: '연습중', currentPlayers: 3, totalPlayers: 6, type: '장편', status: '대기중', isPublic: true, is_voice: false, is_camera: true },
  { id: 3, title: '던전월드 ㅋㅋ', currentPlayers: 4, totalPlayers: 7, type: '단편', status: '게임중', isPublic: false, is_voice: true, is_camera: true },
  { id: 4, title: '이어하기', currentPlayers: 2, totalPlayers: 4, type: '장편', status: '대기중', isPublic: true, is_voice: false, is_camera: false },
  { id: 5, title: '오늘같은 밤이면', currentPlayers: 6, totalPlayers: 8, type: '장편', status: '게임중', isPublic: false, is_voice: true, is_camera: false },
  { id: 6, title: '프레첼', currentPlayers: 6, totalPlayers: 8, type: '장편', status: '대기중', isPublic: true, is_voice: false, is_camera: true },
  { id: 7, title: 'ㅎ2용', currentPlayers: 6, totalPlayers: 8, type: '장편', status: '게임중', isPublic: false, is_voice: true, is_camera: true },
]);

const isMessengerOpen = ref(false);
const isCreateRoomModalOpen = ref(false);
const isChatOpen = ref(false);

// 백엔드에서 방 목록을 받아오는 로직 (주석 처리)
/*
const fetchRooms = async () => {
  try {
    const response = await axios.get('your-endpoint-to-fetch-rooms');
    rooms.value = response.data;
  } catch (error) {
    console.error('Error fetching rooms:', error);
  }
};

onMounted(() => {
  fetchRooms();
});
*/

const enterRoom = (roomId) => {
  router.push({ name: 'Waiting', params: { roomId } });
};

const openMessenger = () => {
  isMessengerOpen.value = true;
};

const closeMessenger = () => {
  isMessengerOpen.value = false;
};

const openCreateRoomModal = () => {
  isCreateRoomModalOpen.value = true;
};

const closeCreateRoomModal = () => {
  isCreateRoomModalOpen.value = false;
};

const openChat = (friendId) => {
  console.log(`Starting chat with friend ID ${friendId}`);
  isChatOpen.value = true;
};

const closeChat = () => {
  isChatOpen.value = false;
};
</script>

<style scoped>
.lobby-container {
  padding: 40px 2rem;
  background: linear-gradient(180deg, #2c2c2c, #1c1c1c);
  color: white;
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  position: relative;
}

.room-table-container {
  width: 75%;
  position: relative;
}

.create-room-button {
  position: relative; 
  top: 0;
  left: 0;
  background-color: #091d31;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
  margin-bottom: 20px; 
}

.room-table {
  width: 100%;
  border-collapse: collapse;
  border-radius: 15px;
  overflow: hidden;
  background-color: #343434;
}

.room-table th, .room-table td {
  border: 1px solid #555;
  padding: 10px;
  text-align: center;
}

.room-table th {
  background-color: #b58b39;
}

.room-table tr {
  background-color: #444444; 
}

.enter-button {
  padding: 5px 10px;
  background-color: #4caf50;
  color: white;
  border: none;
  cursor: pointer;
  border-radius: 5px;
}

.enter-button:hover {
  background-color: #45a049;
}

.friend-icon {
  position: fixed;
  bottom: 20px;
  right: 20px;
  cursor: pointer;
  z-index: 1000;
}

.friend-icon img {
  width: 50px;
  height: 50px;
}

.icon {
  width: 20px;
  height: 20px;
  margin-left: 5px;
}
</style>
