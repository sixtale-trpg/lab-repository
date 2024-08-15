<template>
  <div class="character-sheet" :style="backgroundStyle">
    <Header class="header" />
    <div class="main-content">
      <div class="left-section">
        <JobBoard 
          class="job-board" 
          :created-jobs="createdCharacterSheetJobs"
          @job-selected="handleJobSelection"
        />
        <VideoProfile class="video-profile" />
      </div>
      <div class="right-section">
        <GMSection 
          class="gm-section" 
          :gm="gm" 
          :isGM="isGM" 
          @start-game="startGame" 
        />
        <Chatting class="chatting" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import Header from '@/views/games/components/charactersheet/Header.vue';
import VideoProfile from '@/views/games/components/charactersheet/VideoProfile.vue';
import GMSection from '@/views/games/components/charactersheet/GMSection.vue';
import Chatting from '@/views/games/components/charactersheet/Chatting.vue';
import JobBoard from './components/charactersheet/JobBoard.vue';
import { getRoomInfo } from '@/common/api/RoomsAPI';
import { getMemberInfo } from '@/common/api/mypageAPI';

const router = useRouter();
const route = useRoute();
const roomId = ref(null);

const gm = ref({
  name: 'GM닉네임입니다',
  profileImage: require('@/assets/images/users/gm.png'),
});

const createdCharacterSheetJobs = ref([]);
const isGM = ref(false);

const handleJobSelection = (jobId) => {
  if (!createdCharacterSheetJobs.value.includes(jobId)) {
    createdCharacterSheetJobs.value.push(jobId);
  }
};

const fetchRoomDetails = async () => {
  try {
    roomId.value = route.params.roomId;
    console.log(roomId.value)
    const roomInfo = await getRoomInfo(roomId.value);
    console.log('Room Info:', roomInfo); // roomInfo가 올바르게 반환되는지 확인

    gm.value.name = roomInfo.gmNickname;
    gm.value.profileImage = roomInfo.gmImageURL || require('@/assets/images/users/default.png');

    const memberInfo = await getMemberInfo();
    console.log('Member Info:', memberInfo); // memberInfo가 올바르게 반환되는지 확인

    const currentUserId = memberInfo.data.data.id;
    console.log('GM ID:', roomInfo.gmID); // GM ID 확인
    console.log('Current User ID:', currentUserId); // Current User ID 확인

    if (roomInfo.gmID === currentUserId) {
      isGM.value = true; // 현재 사용자가 GM이면 true로 설정
      console.log('isGM:', isGM.value); // isGM이 true로 설정되었는지 확인
    }
  } catch (error) {
    console.error('Error fetching room info or member info:', error);
  }
};


onMounted(() => {
  fetchRoomDetails();
});

const startGame = () => {
  if (isGM.value) {
    router.push(`/game/${route.params.roomId}/in-game`);
  }
};

const backgroundImage = require('@/assets/images/character_sheet/MainBackground.png');
const backgroundStyle = ref({
  backgroundImage: `url(${backgroundImage})`,
  backgroundSize: 'cover',
  backgroundPosition: 'center'
});

</script>



<style scoped>
.character-sheet {
  display: flex;
  flex-direction: column;
  height: 100vh;
  width: 100vw;
  overflow: hidden;
}

.header {
  height: 10vh;
  width: 100%;
}

.main-content {
  display: flex;
  height: 90vh;
  width: 100%;
}

.left-section {
  display: flex;
  flex-direction: column;
  width: 80%;
  height: 100%;
  padding: 1vh;
  box-sizing: border-box;
}

.right-section {
  display: flex;
  flex-direction: column;
  width: 20%;
  height: 100%;
  padding: 1vh;
  box-sizing: border-box;
}

.job-board {
  height: 73%;
  margin-bottom: 1vh;
}

.video-profile {
  height: 27%;
}

.gm-section {
  height: 25%;
  margin-bottom: 1vh;
}

.chatting {
  height: 75%;
}
</style>
