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
      <div class="profile-card" v-for="n in 8" :key="n">
        <div class="profile-image">
          <!-- AI Images 상태와 Character Stats 상태를 구분하여 렌더링 -->
          <img v-if="showAIImages" :src="getAIImage(n)" :alt="'User ' + n + ' AI profile picture'">
          <CharacterStats v-else-if="showStats" :playMemberID="n" />
          <video v-else :id="'video-' + n" autoplay playsinline></video>
        </div>
        <div class="profile-info">
          <div class="user-info">
            <h3>User {{ n }}</h3>
            <span v-if="isGM">정보</span>
            <img v-if="isGM" src="@/assets/images/ingame/Info.png" alt="Info" class="info-icon" @click="showUserInfo(n)" />
          </div>
          <button class="voice-chat-button" @click="toggleVoiceChat(n)">
            <img :src="getVoiceIcon(n)" alt="Voice chat">
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
import CharacterInfo from '@/views/games/components/Modal/CharacterInfoModal.vue';

export default {
  components: {
    CharacterStats,
    CharacterInfo
  },
  data() {
    return {
      OV: null,
      session: null,
      publisher: null,
      subscribers: [],
      voiceStates: Array(8).fill(false),
      showStats: false,
      showAIImages: true, // 초기값으로 AI 이미지가 보이도록 설정
      showCharacterInfo: false,
      isGM: true
    };
  },
  methods: {
    async startVoiceChat(userId) {
      this.OV = new OpenVidu();
      this.session = this.OV.initSession();

      this.session.on('streamCreated', (event) => {
        const subscriber = this.session.subscribe(event.stream, `video-${userId}`);
        this.subscribers.push(subscriber);
      });

      try {
        const token = await this.getToken();
        await this.session.connect(token, { clientData: `User ${userId}` });

        const publisher = await this.OV.initPublisherAsync(`video-${userId}`, {
          audioSource: undefined,
          videoSource: false,
          publishAudio: true,
          publishVideo: false,
          resolution: '640x480',
          frameRate: 30,
          insertMode: 'APPEND',
          mirror: false,
        });

        this.session.publish(publisher);
        this.publisher = publisher;
      } catch (error) {
        console.error('There was an error connecting to the session:', error);
      }
    },
    async getToken() {
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
    },
    toggleVoiceChat(userId) {
      this.voiceStates[userId - 1] = !this.voiceStates[userId - 1];
      if (this.voiceStates[userId - 1]) {
        this.startVoiceChat(userId);
      } else {
        this.stopVoiceChat(userId);
      }
    },
    isVoiceOn(userId) {
      return this.voiceStates[userId - 1];
    },
    stopVoiceChat(userId) {
      if (this.publisher) {
        this.session.unpublish(this.publisher);
        this.publisher = null;
      }
      this.subscribers.forEach(subscriber => {
        if (subscriber.stream.connection.connectionId === `video-${userId}`) {
          this.session.unsubscribe(subscriber);
        }
      });
      this.subscribers = this.subscribers.filter(
        (subscriber) => subscriber.stream.connection.connectionId !== `video-${userId}`
      );
      if (this.session) {
        this.session.disconnect();
      }
      this.session = null;
      this.OV = null;
    },
    getAIImage(n) {
      // AI 이미지 경로 반환
      return require(`@/assets/images/ingame/user${n}.png`);
    },
    getVoiceIcon(userId) {
      return this.isVoiceOn(userId) ? require('@/assets/images/ingame/voice.png') : require('@/assets/images/ingame/voicex.png');
    },
    toggleStats() {
      this.showStats = true;
      this.showAIImages = false;
    },
    toggleAIImages() {
      this.showAIImages = true;
      this.showStats = false;
    },
    showUserInfo(userId) {
      this.showCharacterInfo = true;
    },
    closeCharacterInfo() {
      this.showCharacterInfo = false;
    }
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
