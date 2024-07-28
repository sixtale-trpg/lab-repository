<template>
  <div class="video-profile">
    <div class="sidebar">
      <button class="icon-button" @click="showAIImages = true">
        <img src="@/assets/images/ingame/Person.png" alt="AI Images">
      </button>
      <button class="icon-button" @click="showStats = true">
        <img src="@/assets/images/ingame/Status.png" alt="Character Stats">
      </button>
    </div>
    <div class="profile-cards">
      <div class="profile-card" v-for="n in 8" :key="n">
        <div class="profile-image">
          <!-- 실제 이미지 URL을 불러오는 코드 -->
          <!-- <img :src="getCharacterImageUrl(n)" :alt="'User ' + n + ' profile picture'"> -->
          <!-- 임시 이미지 사용 -->
          <img :src="getUserImage(n)" :alt="'User ' + n + ' profile picture'">
          <video :id="'video-' + n" autoplay playsinline></video>
        </div>
        <div class="profile-info">
          <h3>User {{ n }}</h3>
          <button class="voice-chat-button" @click="toggleVoiceChat(n)">
            <img :src="getVoiceIcon(n)" alt="Voice chat">
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { OpenVidu } from 'openvidu-browser';

export default {
  data() {
    return {
      OV: null,
      session: null,
      publisher: null,
      subscribers: [],
      voiceStates: Array(8).fill(false), // 각 사용자에 대한 음성 상태를 저장하는 배열
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
          audioSource: undefined, // The source of audio. If undefined default microphone
          videoSource: false, // No video source
          publishAudio: true, // Whether you want to start publishing with your audio unmuted or not
          publishVideo: false, // Whether you want to start publishing with your video enabled or not
          resolution: '640x480', // The resolution of your video
          frameRate: 30, // The frame rate of your video
          insertMode: 'APPEND', // How the video is inserted in the target element
          mirror: false, // Whether to mirror your local video or not
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
    getUserImage(n) {
      return require(`@/assets/images/ingame/user${n}.png`);
    },
    getVoiceIcon(userId) {
      return this.isVoiceOn(userId) ? require('@/assets/images/ingame/voice.png') : require('@/assets/images/ingame/voicex.png');
    }
  }
};
</script>

<style scoped>
.video-profile {
  background-color: #555;
  color: white;
  padding: 10px;
  border-radius: 10px;
  display: flex;
  height: 100%;
}

.sidebar {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 10px 0;
  width: 40px;
  height: calc(100% - 20px);
  margin-left: 10px;
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
  margin-bottom: 10px; /* 각 아이콘 사이의 간격 */
}

.icon-button img {
  width: 30px;
  height: 30px;
}

.profile-cards {
  display: flex;
  justify-content: space-between;
  width: calc(100% - 100px); /* 아이콘 영역과 간격을 고려한 너비 */
  margin-left: 10px;
}

.profile-card {
  width: calc(12.5% - 10px);
  display: flex;
  flex-direction: column;
}

.profile-image {
  width: 100%;
  padding-top: 90%;
  position: relative;
  overflow: hidden;
}

.profile-image img,
.profile-image video {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.profile-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 5px;
  background-color: rgba(0, 0, 0, 0.5);
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
