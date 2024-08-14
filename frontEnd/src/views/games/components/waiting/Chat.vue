<template>
  <div :style="chatSectionStyle" class="chat-section">
    <div class="chat-tabs">
      <!-- 탭 클릭 시 탭 전환 -->
      <div
        @click="selectTab('ALL')"
        :class="{ tab: true, active: selectedTab.value === 'ALL' }"
        :style="tabAllStyle"
      >
        <span>전체</span>
      </div>
      <div
        @click="selectTab('CHAT')"
        :class="{ tab: true, active: selectedTab.value === 'CHAT' }"
        :style="tabChatStyle"
      >
        <span>채팅</span>
      </div>
      <div
        @click="selectTab('WHISPER')"
        :class="{ tab: true, active: selectedTab.value === 'WHISPER' }"
        :style="tabWhisperStyle"
      >
        <span>귓속말</span>
      </div>
    </div>
    <div :style="chatWindowStyle" class="chat-window">
      <div v-if="selectedTab === 'WHISPER'" class="d-flex">
        <div class="text-white me-2">상대 선택</div>
        <select v-model="selectedUser" class="whisper-dropdown flex-grow-1">
          <option v-for="user in props.users" :key="user.id" :value="user">{{ user.name }}</option>
        </select>
      </div>
      <!-- 필터링된 메시지 출력 -->
      <div v-for="message in filteredMessages" :key="message.id" class="chat-message">
        <span class="sender">{{ message.nickName }}:</span>
        <span class="text">{{ message.content }}</span>
      </div>
    </div>
    <div :style="inputContainerStyle" class="input-container">
      <input
        v-model="newMessage"
        @keydown.enter="sendMessage"
        placeholder="메시지를 입력하세요..."
        :style="chatInputStyle"
        class="chat-input"
      />
      <button @click="sendMessage" :style="sendButtonStyle" class="send-button"></button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, defineProps, watch } from "vue";
import WebSocketService from "@/store/websocket/waiting"; // WebSocket 서비스 가져오기
import { getMemberInfo } from "@/common/api/mypageAPI";
import { useRoute, useRouter } from "vue-router";

const props = defineProps({
  users: {
    type: Array,
    required: true
  }
});

// 이미지 동적 로드
const background1 = require("@/assets/images/room/chat/chat_background1.png");
const background2 = require("@/assets/images/room/chat/chat_background2.png");
const tabAllImage = require("@/assets/images/room/chat/chat_all.png");
const tabChatImage = require("@/assets/images/room/chat/chat_common.png");
const tabWhisperImage = require("@/assets/images/room/chat/chat_whisper.png");
const inputBackground = require("@/assets/images/room/chat/chat_input.png");
const sendButtonImage = require("@/assets/images/room/chat/Send_Button.png");

const selectedTab = ref("ALL");
const newMessage = ref("");
const messages = ref([]); // 모든 메시지를 저장하는 배열
const roomInfo = ref(null); // 방 정보를 저장할 변수
const user = ref({});
const route = useRoute();
// 로그로직 추가해야한다 ! //
const router = useRouter();
const selectedUser = ref(null);

watch(selectedUser, (newSelectedUser) => {
  console.log("newSelectedUser:", newSelectedUser);
});

// 컴포넌트가 마운트되면 WebSocket 연결 설정 및 방 정보 가져오기
onMounted(async () => {
  //회원 기본 정보
  getMemberInfo()
    .then((response) => {
      console.log(response.data.data);
      user.value = response.data.data;
    })
    .catch((error) => {
      console.error("Failed to fetch member info:", error);
    });

  WebSocketService.connect(route.params.roomId, user.value.id);

  // 서버로부터 메시지를 수신할 때마다 콜백 실행
  WebSocketService.onMessageReceived((message) => {
    console.log("경민"+" "+message)
    switch(message.type){
      case "ENTER":
        console.log("222")
      case "TALK":
        console.log("222111")
    }
    messages.value.push(message); // 메시지 목록에 추가
  });
});

const selectTab = (tab) => {
  selectedTab.value = tab.toUpperCase(); // 탭 선택
};

const sendMessage = () => {
  if (newMessage.value.trim() === "") return;

  const messageData = {
    roomID: parseInt(route.params.roomId,10), // 채팅방 ID
    memberID: user.value.id,
    // recipient: selectedTab.value === "whisper" ? "recipientUsername" : "", // 귓속말 대상자, 귓속말일 때만 설정
    content: newMessage.value, // 메시지 내용
    type: selectedTab.value === "WHISPER" ? "WHISPER" : "CHAT", // 메시지 유형
    //roomType: roomInfo.value ? roomInfo.value.type : null, // 방 정보에서 roomType 사용
  };

  WebSocketService.sendMessage(messageData);

  newMessage.value = ""; // 입력 필드 초기화
  console.log("메시지가 추가되었습니다:", newMessage.value);
};

const filteredMessages = computed(() => {
  if (selectedTab.value === "ALL") {
    return messages.value; // 전체 메시지 반환
  }
  return messages.value.filter((message) => message.type === selectedTab.value); // 선택된 탭에 맞는 메시지 필터링
});

const chatSectionStyle = {
  height: "45%",
  width: "97%",
  display: "flex",
  flexDirection: "column",
  justifyContent: "space-between",
  backgroundImage: `url(${background2})`,
  backgroundRepeat: "no-repeat",
  backgroundPosition: "center center",
  backgroundSize: "cover",
  borderRadius: "10px",
  padding: "10px",
  margin: "10px",
  boxSizing: "border-box",
};

const chatWindowStyle = {
  flexGrow: 1,
  overflowY: "auto",
  padding: "10px",
  backgroundImage: `url(${background1})`,
  backgroundRepeat: "no-repeat",
  backgroundPosition: "center center",
  backgroundSize: "cover",
  marginBottom: "10px",
  borderRadius: "5px",
};

const tabStyle = {
  cursor: "pointer",
  width: "20%", // 필요에 따라 너비 조정
  height: "auto", // 필요에 따라 높이 조정
  backgroundSize: "cover",
  backgroundPosition: "center",
  borderRadius: "5px",
  display: "flex",
  alignItems: "center",
  justifyContent: "center",
  color: "white",
  fontWeight: "bold",
  transition: "background-color 0.3s, border 0.3s", // 부드러운 효과를 위한 전환 추가
};

const tabAllStyle = {
  ...tabStyle,
  backgroundImage: `url(${tabAllImage})`,
};

const tabChatStyle = {
  ...tabStyle,
  backgroundImage: `url(${tabChatImage})`,
};

const tabWhisperStyle = {
  ...tabStyle,
  backgroundImage: `url(${tabWhisperImage})`,
};

const activeTabStyle = {
  backgroundColor: "rgba(255, 255, 255, 0.3)", // 활성 탭의 배경색 변경
  border: "5px solid #fff", // 활성 탭에 테두리 추가
};

const inputContainerStyle = {
  display: "flex",
  alignItems: "center",
  backgroundImage: `url(${inputBackground})`,
  backgroundRepeat: "no-repeat",
  backgroundSize: "cover",
  borderRadius: "5px",
  padding: "5px",
};

const chatInputStyle = {
  flexGrow: 1,
  border: "none",
  background: "transparent",
  color: "white",
  padding: "10px",
  outline: "none",
  borderRadius: "5px",
};

const sendButtonStyle = {
  width: "40px",
  height: "40px",
  backgroundImage: `url(${sendButtonImage})`,
  backgroundSize: "cover",
  backgroundPosition: "center",
  backgroundColor: "transparent", // 배경색 투명으로 설정
  border: "none",
  padding: "0", // 패딩 제거
  margin: "0", // 마진 제거
  outline: "none",
  cursor: "pointer",
};
</script>

<style scoped>
.chat-tabs {
  display: flex;
}

.tab.active {
  background-color: rgba(255, 255, 255, 0.3);
  border: 2px solid #fff;
}

.chat-window {
  flex-grow: 1;
  overflow-y: auto;
  padding: 10px;
  background-color: rgba(41, 23, 7, 0.8); /* 가독성을 위한 약간의 오버레이 추가 */
  margin-bottom: 10px;
  border-radius: 5px;
}

.chat-message {
  margin-bottom: 5px;
  color: white;
}

.sender {
  font-weight: bold;
}

input::placeholder {
  color: white;
}

@media (max-width: 768px) {
  .chat-section {
    height: auto;
    margin: 5px;
  }

  .chat-tabs {
    flex-direction: column;
  }

  .chat-tabs div {
    margin: 5px 0;
  }
}
</style>
