<template>
  <div :style="waitingContainerStyle" class="waiting-container">
    <RoomHeader :roomTitle="roomDetails.title" :nextSchedule="nextSchedule" />
    <div class="content">
      <div class="left-section">
        <div class="user-cards">
          <UserCard v-for="user in users" :key="user.id" :user="user" :isGM="isGM" />
        </div>
        <Chat class="chat-section" />
      </div>
      <div class="right-section">
        <div :style="topSectionStyle" class="top-section">
          <Map />
          <div :style="gmCardStyle" class="gm-section">
            <div class="gm-info">
              <div :style="profileImageContainerStyle" class="profile-image-container">
                <img :src="gm.profileImage" alt="GM 프로필" class="profile-image" />
                <img :src="avatarFrameImagePath" alt="테두리" class="avatar-frame" />
              </div>
              <div :style="gmNameStyle" class="gm-name">{{ gm.name }}</div>
              <img :src="infoIconPath" alt="Info" @click="showGMModal = true" class="info-icon" />
            </div>
            <button :disabled="!isGM" :style="[startGameButtonStyle, !isGM ? disabledButtonStyle : {}]" class="start-game-button" @click="startGame">게임 시작</button>
          </div>
        </div>
        <div class="details">
          <div class="rule-scenario">
            <div class="game-info">
              <div class="title">
                <span>게임 룰</span>
                <img src="@/assets/images/room/detail.png" alt="Info" @click="openRulebookModal" class="info-icon-large" />
              </div>
              <div class="content">{{ gameRule }}</div>
            </div>
            <div class="game-info">
              <div class="title">
                <span>시나리오</span>
                <img src="@/assets/images/room/detail.png" alt="Info" @click="fetchScenarioDetails" class="info-icon-large" />
              </div>
              <div class="content">{{ scenario }}</div>
            </div>
          </div>
          <Calendar @select="selectDate" />
        </div>
      </div>
    </div>
    <Userinfo v-if="showUserModal" :user="selectedUser" @close="showUserModal = false" />
    <Userinfo v-if="showGMModal" :user="gm" @close="showGMModal = false" />
    <ScenarioModal v-if="isScenarioModalOpen" :scenario="scenarioDetails" @close="closeScenarioModal" />
    <RulebookModal :isOpen="isRulebookModalOpen" @close="closeRulebookModal" />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useStore } from 'vuex';
import RoomHeader from './components/waiting/RoomHeader.vue';
import UserCard from './components/waiting/UserCard.vue';
import Chat from './components/waiting/Chat.vue';
import Map from './components/waiting/Map.vue';
import Calendar from './components/Calendar.vue';
import RulebookModal from './components/Modal/RulebookModal.vue';
import ScenarioModal from './components/Modal/ScenarioModal.vue';
import Userinfo from './components/Modal/UserInfo.vue';

const store = useStore();

const roomDetails = ref({
  title: '더미 방제목',
  currentPlayers: 4,
  totalPlayers: 8,
  status: '대기중',
});

const users = ref([
  { id: 1, name: '오소리 탈춤꾼입니다', profileImage: require('@/assets/images/users/user1.png') },
  { id: 2, name: 'HybridObjectClass', profileImage: require('@/assets/images/users/user2.png') },
  { id: 3, name: 'User3', profileImage: require('@/assets/images/users/user3.png') },
  { id: 4, name: 'User4', profileImage: require('@/assets/images/users/user4.png') },
  { id: 5, name: 'User5', profileImage: require('@/assets/images/users/user5.png') },
  { id: 6, name: 'User6', profileImage: require('@/assets/images/users/user6.png') },
  { id: 7, name: 'User7', profileImage: require('@/assets/images/users/user7.png') },
  { id: 8, name: 'User8', profileImage: require('@/assets/images/users/user8.png') },
]);

const gm = ref({
  name: 'Game Master',
  profileImage: require('@/assets/images/users/gm.png'),
});

const isGM = ref(true); // 현재 접속한 사용자가 GM인지 여부 설정
const nextSchedule = ref('2024. 07. 21');
const gameRule = ref('던전 월드');
const scenario = ref('왕자와 죽음의 개');
const scenarioDetails = ref({});

const isRulebookModalOpen = ref(false);
const isScenarioModalOpen = ref(false);
const showUserModal = ref(false);
const showGMModal = ref(false);
const selectedUser = ref(null);

const router = useRouter();
const route = useRoute();

const selectDate = (date) => {
  nextSchedule.value = date;
};

const openRulebookModal = () => {
  isRulebookModalOpen.value = true;
};

const closeRulebookModal = () => {
  isRulebookModalOpen.value = false;
};

const openScenarioModal = () => {
  isScenarioModalOpen.value = true;
};

const closeScenarioModal = () => {
  isScenarioModalOpen.value = false;
};

const startGame = () => {
  // 게임 캐릭터 시트 페이지로 이동
  router.push({ name: 'CharacterSheet', params: { roomId: route.params.roomId } });
};

const fetchScenarioDetails = async () => {
  // 시나리오 정보를 백엔드에서 가져오는 부분
  scenarioDetails.value = {
    title: '왕자와 죽음의 개',
    writer_id: 'writer123',
    summary: '이 시나리오는...',
    description: '상세 설명 내용...'
  };
  openScenarioModal();
};

// 배경 이미지 경로 설정
const backgroundImagePath = require('@/assets/images/room/main_background.png');
const profileBoxImagePath = require('@/assets/images/room/profile_box.png');
const nameBoxImagePath = require('@/assets/images/room/name_box.png');
const avatarFrameImagePath = require('@/assets/images/room/avatar_frame.png');
const startButtonImagePath = require('@/assets/images/room/start_button.png');
const infoIconPath = require('@/assets/images/room/info.png');

// 배경 이미지 스타일 설정
const waitingContainerStyle = computed(() => ({
  backgroundImage: `url(${backgroundImagePath})`,
  backgroundSize: 'cover',
  backgroundPosition: 'center',
  backgroundRepeat: 'no-repeat',
  height: '100vh',
  width: '100vw',
}));

// GM Card 스타일 설정
const gmCardStyle = computed(() => ({
  backgroundImage: `url(${profileBoxImagePath})`,
  backgroundSize: 'cover',
  backgroundPosition: 'center',
  display: 'flex',
  flexDirection: 'column',
  alignItems: 'center',
  justifyContent: 'space-between',
  borderRadius: '10px',
  height: '80%',
  padding: '5%',
  width: '90%',
  overflow: 'hidden',
}));

const gmNameStyle = computed(() => ({
  backgroundImage: `url(${nameBoxImagePath})`,
  backgroundSize: 'cover',
  backgroundPosition: 'center',
  padding: '10% 10%',
  borderRadius: '5px',
  color: '#ffffff',
  border: '1px solid #5a4d41',
  marginTop: '10%',
  width: '110%',
  textAlign: 'center',
  display: 'inline-block',
  height: '22%',
  lineHeight: '1.5',
  overflow: 'hidden',
  textOverflow: 'ellipsis',
  whiteSpace: 'nowrap',
}));

const profileImageContainerStyle = {
  position: 'relative',
  width: '200px', /* 고정된 크기 */
  height: '200px', /* 고정된 크기 */
  overflow: 'hidden', /* 이미지가 넘치지 않도록 설정 */
  borderRadius: '50%', /* 컨테이너를 원형으로 설정 */
  backgroundColor: '#291707', /* 배경색을 카드 배경색과 일치시키기 */
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
};

const topSectionStyle = {
  display: 'flex',
  height: '60%',
};

const startGameButtonStyle = {
  backgroundImage: `url(${startButtonImagePath})`,
  backgroundSize: 'cover',
  backgroundPosition: 'center',
  width: '80%',
  padding: '10px',
  color: 'white',
  borderRadius: '5px',
  cursor: 'pointer',
  marginTop: '5px',
  border: 'none',
  textAlign: 'center',
};

const disabledButtonStyle = {
  cursor: 'not-allowed',
  opacity: '0.5',
};
</script>

<style scoped>
.waiting-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  box-sizing: border-box;
}

.content {
  display: flex;
  height: 90%;
}

.left-section {
  width: 49%;
  display: flex;
  flex-direction: column;
  padding: 10px;
  box-sizing: border-box;
}

.user-cards {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  grid-template-rows: repeat(2, 1fr);
  gap: 5px;
  height: 55%;
  padding-left: 6px;
}

.chat-section {
  flex: 1;
  margin-top: 10px;
  overflow-y: auto;
}

.right-section {
  width: 50%;
  display: flex;
  flex-direction: column;
  padding-top: 15px;
  box-sizing: border-box;
}

.top-section {
  display: flex;
  height: 60%;
}

.map-section {
  flex: 6;
  border-radius: 10px;
  height: 100%;
  position: relative;
}

.gm-section {
  flex: 4;
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-left: 10px;
  background-color: #291707;
  border-radius: 10px;
  height: 70%;
  width: 100%;
  justify-content: center;
  cursor: pointer;
}

.gm-profile {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  position: relative;
  flex: 1;
}

.profile-image {
  width: 90%;
  height: 90%;
  object-fit: cover; /* 이미지가 컨테이너를 왜곡 없이 덮도록 설정 */
  border-radius: 50%;
}

.avatar-frame {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  border-radius: 50%;
}

.info-icon {
  width: 20px;
  height: 20px;
  cursor: pointer;
  position: absolute;
  top: 14%;
  right: 1%;
  transform: translate(-50%, -50%);
}

.start-game-button {
  width: 80%;
  padding: 10px;
  color: white;
  border-radius: 5px;
  cursor: pointer;
  margin-top: 5px;
  border: none;
  text-align: center;
}

.start-game-button.disabled {
  cursor: not-allowed;
  opacity: 0.5;
}

.details {
  display: flex;
  justify-content: space-between;
  margin-top: -40px;
  padding-bottom: 20px;
  flex-grow: 1;
}

.rule-scenario {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  width: 48%;
}

.game-info {
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 10px;
  background-color: #291707;
  border-radius: 10px;
  margin-bottom: 20px;
}

.game-info .title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  color: #ffffff;
  font-size: 1.8em;
}

.info-icon-large {
  width: 16px;
  height: 16px;
  cursor: pointer;
  margin-left: 5px;
}

.game-info .content {
  background-color: #564307;
  padding: 20px;
  border-radius: 5px;
  color: #ffffff;
  border: 1px solid #5a4d41;
  text-align: center;
  font-size: 1.4em;
}

.calendar {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 23px;
  background-color: #291707;
  border-radius: 10px;
  width: 48%;
}

.map-section .map-controls {
  position: absolute;
  top: 50%;
  left: 0;
  right: 0;
  display: flex;
  justify-content: space-between;
  transform: translateY(-50%);
}

.map-section .map-controls img {
  width: 20px;
  height: 20px;
  cursor: pointer;
}

@media (max-width: 768px) {
  .content {
    flex-direction: column;
  }

  .left-section,
  .right-section {
    width: 100%;
  }

  .user-cards {
    grid-template-columns: repeat(2, 1fr);
    grid-template-rows: repeat(4, 1fr);
    height: auto;
  }

  .chat-section {
    margin-top: 5px;
  }

  .top-section,
  .details {
    height: auto;
  }

  .details {
    flex-direction: column;
  }

  .rule-scenario,
  .calendar {
    width: 100%;
    margin-bottom: 10px;
  }
}
</style>
