<template>
  <div class="waiting-container">
    <RoomHeader :roomTitle="roomDetails.title" :nextSchedule="nextSchedule" />
    <div class="content">
      <div class="left-section">
        <div class="user-cards">
          <UserCard v-for="user in users" :key="user.id" :user="user" :isGM="isGM" />
        </div>
        <Chat class="chat-section" />
      </div>
      <div class="right-section">
        <div class="top-section">
          <Map />
          <div class="gm-section">
            <div class="gm-info" @click="showGMModal = true">
              <img :src="gm.profileImage" alt="GM Profile" class="profile-image" />
              <div class="gm-name">{{ gm.name }}</div>
            </div>
            <button v-if="isGM" class="start-game-button" @click="startGame">게임 시작</button>
          </div>
        </div>
        <div class="details">
          <div class="game-info">
            <div class="title">
              <span>게임 룰</span>
              <img src="@/assets/images/room/detail.png" alt="Info" @click="openRulebookModal" class="info-icon" />
            </div>
            <div class="content">{{ gameRule }}</div>
            <div class="title">
              <span>시나리오</span>
              <img src="@/assets/images/room/detail.png" alt="Info" @click="fetchScenarioDetails" class="info-icon" />
            </div>
            <div class="content">{{ scenario }}</div>
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
import { ref } from 'vue';
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

const isGM = ref(true);
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
  width: 50%;
  display: flex;
  flex-direction: column;
  background-color: #081017;
  padding: 10px;
  box-sizing: border-box;
}

.user-cards {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  grid-template-rows: repeat(2, 1fr);
  gap: 5px;
  height: 55%;
}

.chat-section {
  flex: 1;
  margin-top: 10px;
  overflow-y: auto;
}

.right-section {
  width: 50%;
  display: flex;
  background-color: #081017;
  flex-direction: column;
  padding-left: 10px;
  box-sizing: border-box;
}

.top-section {
  display: flex;
  height: 60%;
}

.map-section {
  flex: 6;
  background-color: #f5f5f5;
  border: 1px solid #ccc;
  border-radius: 10px;
  padding: 10px;
  height: 100%;
  position: relative;
}

.gm-section {
  flex: 4;
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-left: 10px;
}

.gm-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #291707;
  border: 1px solid #ccc;
  border-radius: 10px;
  height: 70%;
  padding: 10px;
  width: 100%;
  justify-content: center;
  cursor: pointer;
}

.profile-image {
  width: 60%;
  height: auto;
  border-radius: 50%;
}

.gm-name {
  background-color: #564307;
  padding: 5px 10px;
  border-radius: 5px;
  color: #ffffff;
  border: 1px solid #5a4d41;
  margin-top: 30px;
  text-align: center;
  width: 80%;
}

.start-game-button {
  width: 80%;
  padding: 10px;
  background-color: #073467;
  color: white;
  border: 1px solid white;
  border-radius: 5px;
  cursor: pointer;
  margin-top: 20px;
}

.details {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
  padding-bottom: 20px;
  flex-grow: 1;
}

.game-info {
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 10px; /* 크기를 줄이기 위해 패딩을 줄였습니다 */
  background-color: #291707;
  border: 1px solid #fff;
  border-radius: 10px;
  width: 48%;
}

.game-info .title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  color: #ffffff;
  font-size: 1.8em; /* 제목의 글자 크기를 키웠습니다 */
}

.info-icon {
  width: 16px;
  height: 16px;
  cursor: pointer;
  margin-left: 5px;
}

.game-info .content {
  background-color: #564307;
  padding: 20px; /* 내용을 크게 보이게 하기 위해 패딩을 키웠습니다 */
  border-radius: 5px;
  color: #ffffff;
  border: 1px solid #5a4d41;
  text-align: center; /* 내용을 가운데 정렬했습니다 */
  font-size: 1.4em; /* 내용의 글자 크기를 키웠습니다 */
}

.calendar {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
  background-color: #291707;
  border: 1px solid #fff;
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

  .game-info,
  .calendar {
    width: 100%;
    margin-bottom: 10px;
  }
}
</style>
