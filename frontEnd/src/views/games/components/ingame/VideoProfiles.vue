<template>
  <div class="video-profile">
    <div class="sidebar">
      <button class="icon-button" @click="toggleAIImages">
        <img src="@/assets/images/ingame/Person.png" alt="AI 이미지" />
      </button>
      <button class="icon-button" @click="toggleStats">
        <img src="@/assets/images/ingame/Status.png" alt="캐릭터 스탯" />
      </button>
    </div>
    <div class="profile-cards">
      <div class="profile-card" v-for="user in users" :key="user.id">
        <div class="profile-image">
          <img
            v-if="showAIImages"
            :src="getAIImage(user.id)"
            :alt="'User ' + user.name + ' AI 프로필 사진'"
            @click="fetchUserJob(user.id)"
          />
          <CharacterStats
            v-else-if="showStats"
            :playMemberID="user.id"
            @click="fetchUserJob(user.id)"
          />
          <video v-else :id="'video-' + user.id" autoplay playsinline></video>
        </div>
        <div class="profile-info">
          <div class="user-info">
            <h3>{{ user.name }}</h3>
            <span v-if="isGM">정보</span>
            <img
              v-if="isGM"
              src="@/assets/images/ingame/Info.png"
              alt="정보"
              class="info-icon"
              @click="showUserInfo(user.id)"
            />
          </div>
          <button class="voice-chat-button" @click="toggleVoiceChat(user.id)">
            <img :src="getVoiceIcon(user.id)" alt="음성 채팅" />
          </button>
        </div>
      </div>
    </div>

    <CharacterInfo v-if="showCharacterInfo" @close="closeCharacterInfo" />
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue';
import { useSessionStore } from '@/store/session';
import CharacterStats from './CharacterStats.vue';
import CharacterInfo from '@/views/games/components/ingame/CharacterInfoModal.vue';
import { selectedPlayMemberID, selectedUserJob } from '@/store/state.js'; // 전역 상태 가져오기

const sessionStore = useSessionStore();

const showStats = ref(false);
const showAIImages = ref(true);
const showCharacterInfo = ref(false);
const isGM = ref(true);

// 더미 데이터: 8명의 유저 정보
const users = ref([
  { id: 1, name: 'Player1', aiImage: '@/assets/images/ingame/user1.png' },
  { id: 2, name: 'Player2', aiImage: '@/assets/images/ingame/user2.png' },
  { id: 3, name: 'Player3', aiImage: '@/assets/images/ingame/user3.png' },
  { id: 4, name: 'Player4', aiImage: '@/assets/images/ingame/user4.png' },
  { id: 5, name: 'Player5', aiImage: '@/assets/images/ingame/user5.png' },
  { id: 6, name: 'Player6', aiImage: '@/assets/images/ingame/user6.png' },
  { id: 7, name: 'Player7', aiImage: '@/assets/images/ingame/user7.png' },
  { id: 8, name: 'Player8', aiImage: '@/assets/images/ingame/user8.png' },
]);

// AI 이미지 경로 가져오기
const getAIImage = (userId) => {
  return require(`@/assets/images/ingame/user${userId}.png`);
};

// 스탯 창을 토글하는 함수
const toggleStats = () => {
  showAIImages.value = false;
  showStats.value = true;
};

// AI 이미지 토글 함수
const toggleAIImages = () => {
  showAIImages.value = true;
  showStats.value = false;
};

// 플레이어 ID와 직업 정보를 설정하는 함수
const fetchUserJob = async (playMemberID) => {
  // 전역 상태에 선택된 플레이어 ID 저장
  console.log('선택된 플레이어 ID 설정:', playMemberID);
  selectedPlayMemberID.value = playMemberID;

  // 실제 API 요청을 보내는 로직 (주석 처리)
  /*
  try {
    const response = await axios.get(`/rooms/${roomID}/sheets/${playMemberID}`);
    if (response.data.statusCode === 200) {
      // 전역 상태에 플레이어의 직업 정보를 저장하는 로직
      selectedUserJob.value = response.data.data.jobName;
      console.log("플레이어 직업 설정:", response.data.data.jobName);
    }
  } catch (error) {
    console.error('사용자 직업 정보를 가져오는 중 오류 발생:', error);
  }
  */

  // 임시 데이터 설정 (테스트용)
  const userJobs = [
    '전사',
    '마법사',
    '도적',
    '성직자',
    '레인저',
    '성기사',
    '드루이드',
    '음유시인',
  ];
  selectedUserJob.value = userJobs[playMemberID - 1] || '알 수 없음';
  console.log('선택된 사용자 직업 설정 (더미 데이터):', selectedUserJob.value);
};

// 사용자 정보를 보여주는 함수
const showUserInfo = (userId) => {
  showCharacterInfo.value = true;
};

// 사용자 정보 창을 닫는 함수
const closeCharacterInfo = () => {
  showCharacterInfo.value = false;
};

// 음성 아이콘 경로를 가져오는 함수
const getVoiceIcon = (userId) => {
  return sessionStore.isVoiceOn(userId)
    ? require('@/assets/images/ingame/voice.png')
    : require('@/assets/images/ingame/voicex.png');
};

// 음성 채팅을 토글하는 함수
const toggleVoiceChat = (userId) => {
  sessionStore.toggleVoiceChat(userId);
};

// 컴포넌트가 마운트될 때 호출되는 라이프사이클 훅
onMounted(() => {
  // 각 사용자에 대해 세션을 초기화합니다.
  users.value.forEach(user => {
    sessionStore.initializeSession(user.id);
  });
});

// 컴포넌트가 제거되기 전에 호출되는 라이프사이클 훅
onBeforeUnmount(() => {
  sessionStore.disconnect();
});
</script>

<style scoped>
.video-profile {
  background-color: #555;
  color: white;
  padding: 0;
  display: flex;
  height: 100%;
}

.sidebar {
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  padding: 10px 0;
  width: 40px;
}

.icon-button {
  background: none;
  border: none;
  cursor: pointer;
  width: 100%;
  height: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.icon-button img {
  width: 30px;
  height: 30px;
}

.profile-cards {
  display: flex;
  justify-content: space-between;
  width: calc(100% - 40px);
  margin-left: 0;
}

.profile-card {
  width: calc(12.5% - 10px);
  display: flex;
  flex-direction: column;
  position: relative;
  overflow: hidden;
  margin: 0;
  padding: 0;
}

.profile-image {
  width: 100%;
  height: 100%;
  position: relative;
}

.profile-image img,
.profile-image video,
.character-stats {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  z-index: 1;
}

.character-stats .stats-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 0;
  object-fit: cover;
}

.profile-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 5px;
  background-color: rgba(0, 0, 0, 0.5);
}

.user-info {
  display: flex;
  align-items: center;
  gap: 5px;
}

.info-icon {
  width: 15px;
  height: 15px;
  cursor: pointer;
}

.profile-info h3 {
  margin: 0;
  font-size: 12px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.voice-chat-button {
  background: none;
  border: none;
  cursor: pointer;
  padding: 0;
}

.voice-chat-button img {
  width: 15px;
  height: 15px;
}
</style>
