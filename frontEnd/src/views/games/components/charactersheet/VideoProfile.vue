<template>
  <div class="video-profile">
    <div class="profile-cards">
      <div class="profile-card" v-for="n in 8" :key="n">
        <div class="profile-image">
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

<script setup>
import { ref } from 'vue';
import { OpenVidu } from 'openvidu-browser';

const OV = ref(null);
const session = ref(null);
const publishers = ref(Array(8).fill(null));
const subscribers = ref(Array(8).fill([]));
const voiceStates = ref(Array(8).fill(false));

const startVoiceChat = async (userId) => {
  OV.value = new OpenVidu();
  session.value = OV.value.initSession();

  session.value.on('streamCreated', (event) => {
    const subscriber = session.value.subscribe(event.stream, `video-${userId}`);
    subscribers.value[userId - 1].push(subscriber);
  });

  try {
    const token = await getToken();
    await session.value.connect(token, { clientData: `User ${userId}` });

    const publisherInstance = await OV.value.initPublisherAsync(`video-${userId}`, {
      audioSource: undefined,
      videoSource: false,
      publishAudio: true,
      publishVideo: false,
      resolution: '640x480',
      frameRate: 30,
      insertMode: 'APPEND',
      mirror: false,
    });

    session.value.publish(publisherInstance);
    publishers.value[userId - 1] = publisherInstance;
  } catch (error) {
    console.error('세션에 연결하는 중 오류가 발생했습니다:', error);
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
      console.log('세션이 이미 존재합니다, 토큰을 얻으러 갑니다');
    } else if (!response.ok) {
      throw new Error(`세션 생성 실패: ${response.statusText}`);
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
      throw new Error(`토큰 생성 실패: ${tokenResponse.statusText}`);
    }

    const token = await tokenResponse.json();
    return token.token;
  } catch (error) {
    console.error('토큰을 가져오는 중 오류가 발생했습니다:', error);
    throw error;
  }
};

const toggleVoiceChat = (userId) => {
  voiceStates.value[userId - 1] = !voiceStates.value[userId - 1];
  if (voiceStates.value[userId - 1]) {
    startVoiceChat(userId);
  } else {
    stopVoiceChat(userId);
  }
};

const isVoiceOn = (userId) => {
  return voiceStates.value[userId - 1];
};

const stopVoiceChat = (userId) => {
  const publisher = publishers.value[userId - 1];

  if (publisher) {
    session.value.unpublish(publisher);
    publishers.value[userId - 1] = null;
  }

  subscribers.value[userId - 1].forEach(subscriber => {
    session.value.unsubscribe(subscriber);
  });

  subscribers.value[userId - 1] = [];
};

const getUserImage = (n) => {
  return require(`@/assets/images/ingame/user${n}.png`);
};

const getVoiceIcon = (userId) => {
  return isVoiceOn(userId) ? require('@/assets/images/ingame/voice.png') : require('@/assets/images/ingame/voicex.png');
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

.profile-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 10px;
  width: 100%;
}

.profile-card {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.profile-image {
  width: 100%;
  padding-top: 100%;
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
  width: 100%;
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
