<template>
  <div class="video-profile">
    <div class="profile-card" v-for="n in 8" :key="n">
      <div class="profile-picture">
        <!-- 프로필 사진 (여기에 비디오 또는 이미지 추가) -->
        <img :src="'https://via.placeholder.com/150?text=User+' + n" alt="User profile picture">
        <video :id="'video-' + n" autoplay playsinline></video>
      </div>
      <div class="profile-details">
        <!-- 사용자 닉네임 추가 -->
        <h3>User {{ n }}</h3>
        <!-- <p>Status: Online</p> -->
      </div>
      <button class="voice-chat-button" @click="toggleVoiceChat(n)">{{ isVoiceOn(n) ? 'off' : 'on' }}</button>
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
  },
};
</script>

<style scoped>
.video-profile {
  background-color: #555;
  color: white;
  padding: 20px;
  border-radius: 10px;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
}

.profile-card {
  background-color: #666;
  border-radius: 5px;
  padding: 10px;
  height: 180px;
  width: 100px; /* 넓이를 170px로 수정 */
  text-align: center;
  flex: 1;
  margin: 0 5px;
  position: relative; /* 버튼을 절대적으로 배치하기 위해 상대적으로 설정 */
}

.profile-picture img {
  border-radius: 50%;
  width: 130px; /* 이미지 크기 수정 */
  height: 130px; /* 이미지 크기 수정 */
}

.profile-picture video {
  border-radius: 50%;
  width: 100px; /* 비디오 크기 수정 */
  height: 10px; /* 비디오 크기 수정 */
}

.profile-details {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.profile-details h3 {
  margin: -13px 0;
  font-size: 14px;
}

.profile-details p {
  margin: 0;
  font-size: 12px;
}

.voice-chat-button {
  background-color: #28a745;
  color: white;
  border: none;
  border-radius: 5px;
  padding: 5px 10px;
  cursor: pointer;
  font-size: 12px;
  position: absolute; /* 버튼을 절대 위치로 설정 */
  bottom: 10px; /* 프로필 카드의 아래쪽 여백 */
  right: 10px; /* 프로필 카드의 오른쪽 여백 */
}

.voice-chat-button:hover {
  background-color: #218838;
}
</style>
