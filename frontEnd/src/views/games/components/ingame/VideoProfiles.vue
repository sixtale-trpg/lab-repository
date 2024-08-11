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
      <div v-if="users.length === 0">No users available</div>
      <div class="profile-card" v-for="user in users" :key="user.playMemberID">
        <div class="profile-image">
          <img
            v-if="showAIImages"
            :src="user.imageURL || require('@/assets/images/user.png')"
            :alt="'User ' + user.playMemberNickname + ' AI 프로필 사진'"
            @click="fetchUserJob(user.playMemberID)"
          />
          <CharacterStats
            v-else-if="showStats"
            :playMemberID="user.playMemberID"
            @click="fetchUserJob(user.playMemberID)"
          />
          <video
            v-show="!showAIImages && !showStats"
            :id="'video-' + user.playMemberID"
            autoplay
            playsinline
            ref="videoElements"
          ></video>
        </div>
        <div class="profile-info">
          <div class="user-info">
            <h3>{{ user.playMemberNickname }}</h3>
            <span v-if="isGM">정보</span>
            <img
              v-if="isGM"
              src="@/assets/images/ingame/Info.png"
              alt="정보"
              class="info-icon"
              @click="showUserInfo(user.playMemberID)"
            />
          </div>
          <VoiceChatButton :userId="user.playMemberID" />
        </div>
      </div>
    </div>
    <CharacterInfo v-if="showCharacterInfo" @close="closeCharacterInfo" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { getCharacterSheet } from '@/common/api/CharacterSheetAPI.js';
import { getRoomInfo } from '@/common/api/RoomsAPI.js';
import CharacterStats from './CharacterStats.vue';
import CharacterInfo from '@/views/games/components/ingame/CharacterInfoModal.vue';
import VoiceChatButton from '../../VoiceChatButton.vue';
import { selectedPlayMemberID, selectedUserJobID } from '@/store/state.js'; // 전역 상태 가져오기

const route = useRoute();
const showStats = ref(false);
const showAIImages = ref(true);
const showCharacterInfo = ref(false);
const isGM = ref(true);
const users = ref([]);
const videoElements = ref([]);

const fetchCharacterSheets = async () => {
  const roomId = route.params.roomId;

  if (!roomId) {
    console.error('Room ID가 설정되지 않았습니다.');
    return;
  }

  try {
    const roomInfo = await getRoomInfo(roomId);
    console.log('Room Info:', roomInfo); // roomInfo 로그 추가

    if (roomInfo && Array.isArray(roomInfo.playMemberList)) {
      users.value = []; // users 배열 초기화
      for (let playMember of roomInfo.playMemberList) {
        console.log(`Fetching character sheet for playMemberID: ${playMember.playMemberID}`);
        try {
          const characterSheet = await getCharacterSheet(roomId, playMember.playMemberID);
          console.log('Character Sheet:', characterSheet); // characterSheet 로그 추가
          if (characterSheet) {
            users.value.push({ ...playMember, ...characterSheet, playMemberID: playMember.playMemberID });
          } else {
            users.value.push({ ...playMember, playMemberID: playMember.playMemberID }); // 기본 정보 추가
          }
        } catch (fetchError) {
          console.error(`Error fetching character sheet for playMemberID ${playMember.playMemberID}:`, fetchError);
          users.value.push({ ...playMember, playMemberID: playMember.playMemberID }); // 기본 정보 추가
        }
      }
      console.log('Fetched users:', users.value);
    } else {
      console.error('Room info or play member list is not an array:', roomInfo);
    }
  } catch (error) {
    console.error('Error fetching character sheets:', error);
  }
};

const fetchUserJob = async (playMemberID) => {
  console.log('선택된 플레이어 ID 설정:', playMemberID);
  selectedPlayMemberID.value = playMemberID;

  try {
    const roomId = route.params.roomId;
    const characterSheet = await getCharacterSheet(roomId, playMemberID);
    selectedUserJobID.value = characterSheet.jobId; // 직업 ID를 저장
    console.log('선택된 사용자 직업 설정:', selectedUserJobID.value);
  } catch (error) {
    console.error('사용자 직업 정보를 가져오는 중 오류 발생:', error);
  }
};

const toggleStats = () => {
  showAIImages.value = false;
  showStats.value = true;
};

const toggleAIImages = () => {
  showAIImages.value = true;
  showStats.value = false;
};

const showUserInfo = (playMemberID) => {
  showCharacterInfo.value = true;
};

const closeCharacterInfo = () => {
  showCharacterInfo.value = false;
};

onMounted(() => {
  fetchCharacterSheets();
});
</script>

<style scoped>
.video-profile {
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
  background-color: rgba(83, 78, 78, 0.5);
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
</style>
