<template>
  <div class="log-section-container" :style="backgroundStyle">
    <div class="tabs">
      <button class="tab" :class="{ active: activeTab === 'allLogs' }" @click="selectTab('allLogs')">
        전체 로그 기록
      </button>
    </div>
    <div class="log-content">
        <div v-if="activeTab === 'allLogs'">
          <div v-for="msg in messages" :key="msg.id">
          <p>{{ msg.content }}</p>
        </div>
    <!-- <div class="message-input">
      <input v-model="newMessage" @keyup.enter="sendMessage" placeholder="Enter your message" />
      <button @click="sendMessage">Send</button>
    </div> -->
      </div>
  <!-- <div class="message-input">
    <input v-model="newMessage" @keyup.enter="sendMessage" placeholder="Enter your message" />
    <button @click="sendMessage">Send</button>
  </div> -->
    </div>
  </div>
</template>

<script setup>
import GameLogWebSocketService from '@/store/websocket/gameLog'; // WebSocket 서비스 가져오기
import { ref, computed, onMounted } from 'vue';
import { useRoute,useRouter } from 'vue-router';
import { getRoomInfo } from '@/common/api/RoomsAPI';
import { map } from 'sockjs-client/lib/transport-list';
import { useMapStore } from '@/store/map/mapStore';
import { getMapList } from '@/common/api/RoomsAPI';

const route = useRoute();
// 로그로직 추가해야한다 ! //
const router = useRouter();
const newMessage = ref('');
const messages = ref([]); // 모든 메시지를 저장하는 배열
const roomId = ref(null);
const mapStore = useMapStore();
const mapData = ref([]);

// 로컬 스토리지 저장
const loadMessagesFromLocalStorage = () => {
const storedMessages = localStorage.getItem(`messages-${roomId.value}`);
if (storedMessages) {
  messages.value = JSON.parse(storedMessages);
}
};

const saveMessagesToLocalStorage = () => {
localStorage.setItem(`messages-${roomId.value}`, JSON.stringify(messages.value));
};


// 컴포넌트가 마운트되면 WebSocket 연결 설정 및 방 정보 가져오기
onMounted(async () => {
 try {
  // 룸 id 받아옴
  roomId.value = route.params.roomId;
  console.log('Room ID from route:', roomId.value);
  
  const response = await getMapList(roomId.value);
  mapData.value = response.mapList || [];

  // 웹소켓 연결
  GameLogWebSocketService.connect(roomId.value);

  // Handle incoming messages
  // 메세지 받아오는것
  GameLogWebSocketService.onMessageReceived((message) => {
    switch(message.gameType){
      case "MAP_CHANGE":
        const selectedMap = mapData.value[message.nextMapID];
        mapStore.setSelectedMap(selectedMap);  //맵 저장
        break;
      case "GAME_START":
        // 게임 시작 시 페이지 이동
        router.push(`/game/${route.params.roomId}/in-game`);
        break;
      default:
        // 다른 메시지 타입의 처리 로직
        break;
    }
    messages.value.push(message);
    saveMessagesToLocalStorage(); // 메시지를 로컬 스토리지에 저장
  });

   // 로컬 스토리지에서 메시지 로드
   loadMessagesFromLocalStorage();

} catch (error) {
  console.error('Error fetching room info or connecting to WebSocket:', error);
}
});

const sendMessage = () => {
if (newMessage.value.trim() === '') return;

const messageData = {
  gameType: 'MAP_CHANGE', 
  roomID: roomId.value, 
  currentMapID: 1, 
  nextMapID: 2,
};

// Send the message to the server
GameLogWebSocketService.sendMessage(messageData); 
newMessage.value = ''; // Clear the input field
};


const activeTab = ref('allLogs');

// 이미지 경로 설정
const backgroundImage = require('@/assets/images/ingame/Border7.png');

// 배경 스타일 설정
const backgroundStyle = {
  backgroundImage: `url(${backgroundImage})`,
  backgroundSize: 'cover',
  backgroundPosition: 'center',
  padding: '10px',
  boxSizing: 'border-box',
  display: 'flex',
  flexDirection: 'column',
  height: '100%'
};

const selectTab = (key) => {
  activeTab.value = key;
};
</script>

<style scoped>
.log-section-container {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.tabs {
  display: flex;
  justify-content: center;
  margin-bottom: 0;
}

.tab {
  flex: 1;
  padding: 5px; /* 높이를 줄임 */
  cursor: pointer;
  border: 1px solid #333;
  color: white;
  background: linear-gradient(to bottom, #1A4E23, #102F12); 
  text-align: center; /* 텍스트 중앙 배치 */
}

.tab.active {
  background-color: #555;
  color: #fff;
}

.log-content {
  flex: 1;
  padding: 10px;
  overflow-y: auto;
  border: 1px solid #444;
  color: white;
}

/* 스크롤바 스타일링 */
.log-content::-webkit-scrollbar {
  width: 8px;
}

.log-content::-webkit-scrollbar-track {
  background: #a56722;
  border-radius: 5px;
}

.log-content::-webkit-scrollbar-thumb {
  background-color: #274e13;
  border-radius: 5px;
  border: 1px solid #a56722;
}
</style>
