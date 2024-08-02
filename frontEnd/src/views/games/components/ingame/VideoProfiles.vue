<template>
  <div class="video-profile">
    <div class="sidebar">
      <button class="icon-button" @click="toggleAIImages">
        <img src="@/assets/images/ingame/Person.png" alt="AI Images">
      </button>
      <button class="icon-button" @click="toggleStats">
        <img src="@/assets/images/ingame/Status.png" alt="Character Stats">
      </button>
    </div>
    <div class="profile-cards">
      <div class="profile-card" v-for="user in users" :key="user.id">
        <div class="profile-image">
          <img v-if="showAIImages" :src="getAIImage(user.id)" :alt="'User ' + user.name + ' AI profile picture'" @click="fetchUserJob(user.id)">
          <CharacterStats v-else-if="showStats" :playMemberID="user.id" @click="fetchUserJob(user.id)" />
          <video v-else :id="'video-' + user.id" autoplay playsinline></video>
        </div>
        <div class="profile-info">
          <div class="user-info">
            <h3>{{ user.name }}</h3>
            <span v-if="isGM">정보</span>
            <img v-if="isGM" src="@/assets/images/ingame/Info.png" alt="Info" class="info-icon" @click="showUserInfo(user.id)" />
          </div>
          <button class="voice-chat-button" @click="toggleVoiceChat(user.id)">
            <img :src="getVoiceIcon(user.id)" alt="Voice chat">
          </button>
        </div>
      </div>
    </div>

    <CharacterInfo v-if="showCharacterInfo" @close="closeCharacterInfo" />
  </div>
</template>

<script>
import { ref } from 'vue';
import { OpenVidu } from 'openvidu-browser';
import CharacterStats from './CharacterStats.vue';
import CharacterInfo from '@/views/games/components/ingame/CharacterInfoModal.vue';
import { selectedPlayMemberID, selectedUserJob } from '@/store/state.js'; // 전역 상태 가져오기

export default {
  components: {
    CharacterStats,
    CharacterInfo
  },
  setup() {
    const OV = ref(null);
    const session = ref(null);
    const publisher = ref(null);
    const subscribers = ref([]);
    const voiceStates = ref(Array(8).fill(false));
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
      { id: 8, name: 'Player8', aiImage: '@/assets/images/ingame/user8.png' }
    ]);

    // AI 이미지 경로 가져오기
    const getAIImage = (userId) => {
      return require(`@/assets/images/ingame/user${userId}.png`);
    };

    const toggleStats = (userId) => {
      showAIImages.value = false;
      showStats.value = true;
      fetchUserJob(userId); // 스탯창을 눌렀을 때 해당 유저의 직업 정보를 가져오도록 함
    };

    // 플레이어 ID와 직업 정보를 설정하는 메서드
    const fetchUserJob = async (playMemberID) => {
      // 전역 상태에 선택된 플레이어 ID 저장
      console.log("Setting selectedPlayMemberID:", playMemberID);
      selectedPlayMemberID.value = playMemberID;

      // 실제 API 요청을 보내는 로직 (주석 처리)
      /*
      try {
        const response = await axios.get(`/rooms/${roomID}/sheets/${playMemberID}`);
        if (response.data.statusCode === 200) {
          // 전역 상태에 플레이어의 직업 정보를 저장하는 로직
          selectedUserJob.value = response.data.data.jobName;
          console.log("Setting selectedUserJob:", response.data.data.jobName);
        }
      } catch (error) {
        console.error('Error fetching user job:', error);
      }
      */

      // 임시 데이터 설정 (테스트용)
      const userJobs = ['Warrior', 'Mage', 'Rogue', 'Cleric', 'Ranger', 'Paladin', 'Druid', 'Bard'];
      selectedUserJob.value = userJobs[playMemberID - 1] || 'Unknown';
      console.log("Setting selectedUserJob (dummy data):", selectedUserJob.value);
    };


    // 기타 메서드들...
    const toggleAIImages = () => {
      showAIImages.value = true;
      showStats.value = false;
    };



    const showUserInfo = (userId) => {
      showCharacterInfo.value = true;
    };

    const closeCharacterInfo = () => {
      showCharacterInfo.value = false;
    };

    const getVoiceIcon = (userId) => {
      return voiceStates.value[userId - 1] ? require('@/assets/images/ingame/voice.png') : require('@/assets/images/ingame/voicex.png');
    };

    const toggleVoiceChat = (userId) => {
      voiceStates.value[userId - 1] = !voiceStates.value[userId - 1];
      if (voiceStates.value[userId - 1]) {
        startVoiceChat(userId);
      } else {
        stopVoiceChat(userId);
      }
    };

    const startVoiceChat = async (userId) => {
      OV.value = new OpenVidu();
      session.value = OV.value.initSession();

      session.value.on('streamCreated', (event) => {
        const subscriber = session.value.subscribe(event.stream, `video-${userId}`);
        subscribers.value.push(subscriber);
      });

      try {
        const token = await getToken();
        await session.value.connect(token, { clientData: `User ${userId}` });

        const publisher = await OV.value.initPublisherAsync(`video-${userId}`, {
          audioSource: undefined,
          videoSource: false,
          publishAudio: true,
          publishVideo: false,
          resolution: '640x480',
          frameRate: 30,
          insertMode: 'APPEND',
          mirror: false,
        });

        session.value.publish(publisher);
        publisher.value = publisher;
      } catch (error) {
        console.error('There was an error connecting to the session:', error);
      }
    };

    const getToken = async () => {
      try {
        const response = await fetch('http://localhost:4443/api/sessions', {
          method: 'POST',
          headers: {
            Authorization: `Basic ${btoa('OPENVIDUAPP:MY_SECRET')}`,
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({ customSessionId: 'SessionA' }),
        });

        if (response.status === 409) {
          console.log('Session already exists, proceeding to get the token');
        } else if (!response.ok) {
          throw new Error(`Failed to create session: ${response.statusText}`);
        }

        const tokenResponse = await fetch('http://localhost:4443/api/tokens', {
          method: 'POST',
          headers: {
            Authorization: `Basic ${btoa('OPENVIDUAPP:MY_SECRET')}`,
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({ session: 'SessionA' }),
        });

        if (!tokenResponse.ok) {
          throw new Error(`Failed to create token: ${tokenResponse.statusText}`);
        }

        const token = await tokenResponse.json();
        return token.token;
      } catch (error) {
        console.error('Error getting token:', error);
        throw error;
      }
    };

    const stopVoiceChat = (userId) => {
      if (publisher.value) {
        session.value.unpublish(publisher.value);
        publisher.value = null;
      }
      subscribers.value.forEach(subscriber => {
        if (subscriber.stream.connection.connectionId === `video-${userId}`) {
          session.value.unsubscribe(subscriber);
        }
      });
      subscribers.value = subscribers.value.filter(
        (subscriber) => subscriber.stream.connection.connectionId !== `video-${userId}`
      );
      if (session.value) {
        session.value.disconnect();
      }
      session.value = null;
      OV.value = null;
    };

    return {
      users,  // 유저 리스트
      showStats,
      showAIImages,
      showCharacterInfo,
      isGM,
      toggleAIImages,
      toggleStats,
      showUserInfo,
      closeCharacterInfo,
      getVoiceIcon,
      toggleVoiceChat,
      fetchUserJob, // 전역 상태관리
      getAIImage // AI 이미지 경로 함수 추가
    };
  }
};
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
