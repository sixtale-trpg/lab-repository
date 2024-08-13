import { defineStore } from 'pinia';
import { ref } from 'vue';
import { OpenVidu } from 'openvidu-browser';

export const useSessionStore = defineStore('session', () => {
  const OV = ref(new OpenVidu());
  const session = ref(null);
  const publishers = ref(Array(9).fill(null)); // 사용자별 발행자 저장
  const subscribers = ref(Array(9).fill([])); // 사용자별 구독자 저장
  const voiceStates = ref(Array(9).fill(false)); // 사용자별 음성 상태 저장

  // 세션을 초기화하는 함수
  const initializeSession = () => {
    if (!session.value) {
      session.value = OV.value.initSession();

      // 스트림 생성 이벤트 처리
      session.value.on('streamCreated', (event) => {
        const subUserId = parseInt(event.stream.connection.data.split('User ')[1], 10);
        console.log(`Subscribing to userId: ${subUserId}`);

        const subscriber = session.value.subscribe(event.stream, `video-${subUserId}`);
        subscriber.on('streamPlaying', () => {
          console.log(`Stream playing for userId: ${subUserId}`);
        });

        subscribers.value[subUserId - 1].push(subscriber);
      });

      // 스트림 제거 이벤트 처리
      session.value.on('streamDestroyed', (event) => {
        const subUserId = parseInt(event.stream.connection.data.split('User ')[1], 10);
        console.log(`Stream destroyed for userId: ${subUserId}`);
        subscribers.value[subUserId - 1] = [];
      });

      session.value.on('connectionCreated', (event) => {
        console.log(`Connection created: ${event.connection.connectionId}`);
      });

      session.value.on('connectionDestroyed', (event) => {
        console.log(`Connection destroyed: ${event.connection.connectionId}`);
      });
    }
  };

  
// 음성 채팅을 시작하는 함수
const startVoiceChat = async (userId) => {
  console.log(session.value)
  if (!session.value) {
  initializeSession();
}
try {
const token = await getToken(userId);
  console.log(`Token: ${token}`);

  // const OV = new OpenVidu();
  const OV = new OpenVidu('https://onedoit.store'); // OpenVidu 서버 URL
  const session = OV.initSession();

  console.log(session)
  await session.connect(token, { clientData: `User ${userId}` });
  console.log(`Connected to session for userId: ${userId}`);

  const publisher = OV.initPublisher(`video-${userId}`, {
    audioSource: true,
    videoSource: false,
    publishAudio: true,
    publishVideo: false,
    resolution: '640x480',
    frameRate: 30,
    insertMode: 'APPEND',
    mirror: false,
  });

  session.publish(publisher);

  console.log(`Publisher started for userId: ${userId}`);

  // 오디오 트랙 이벤트 등록
  publisher.on('streamAudioVolumeChange', (event) => {
    console.log(`Audio volume changed for userId ${userId}: `, event.newValue);
  });

  publisher.on('streamPlaying', () => {
    console.log(`Stream is playing for userId: ${userId}`);
  });
  } catch (error) {
  console.error('Error connecting to session:', error);
  }
};
  // // 음성 채팅을 시작하는 함수
  // const startVoiceChat = async (customSessionId) => {
  //   // if (!session.value) {
  //   //   initializeSession();
  //   // }

  //   try {
  //     const token = await getToken(customSessionId);
  //     console.log(token)
  //     const OV = new OpenVidu();
  //     const session = OV.initSession();
  //     // await session.value.connect(token, { clientData: `User ${userId}` });
  //     // console.log(`Connected to session for userId: ${userId}`);
  //     await session.connect(token, { clientData: `User ${customSessionId}` });
  //     console.log(`Connected to session for customSessionId: ${customSessionId}`);

  //     const publisher = OV.initPublisher(`video-${customSessionId}`, {
  //     audioSource: true,
  //     videoSource: false,
  //     publishAudio: true,
  //     publishVideo: false,
  //     resolution: '640x480',
  //     frameRate: 30,
  //     insertMode: 'APPEND',
  //     mirror: false,
  //   });

  //     session.value.publish(publisher);
  //     // publishers.value[userId - 1] = publisherInstance;
  //     console.log(`Publisher started for userId: ${userId}`);
  //     // voiceStates.value[userId - 1] = true;

  //     // 오디오 트랙 이벤트 등록
  //     publisher.on('streamAudioVolumeChange', (event) => {
  //       console.log(`Audio volume changed for userId ${userId}: `, event.newValue);
  //     });

  //     publisher.on('streamPlaying', () => {
  //       console.log(`Stream is playing for userId: ${userId}`);
  //     });
  //   } catch (error) {
  //     console.error('Error connecting to session:', error);
  //   }
  // };

  // 음성 채팅을 중지하는 함수
  const stopVoiceChat = (userId) => {
    const publisher = publishers.value[userId - 1];

    if (publisher) {
      session.value.unpublish(publisher);
      publishers.value[userId - 1] = null;
      console.log(`Publisher stopped for userId: ${userId}`);
      voiceStates.value[userId - 1] = false;
    }

    subscribers.value[userId - 1].forEach((subscriber) => {
      session.value.unsubscribe(subscriber);
    });
    subscribers.value[userId - 1] = [];
  };

  // 음성 채팅을 토글하는 함수
  const toggleVoiceChat = (userId) => {
    const formattedUserId = String(userId); // userId를 문자열로 변환
    if (voiceStates.value[userId - 1]) {
      stopVoiceChat(formattedUserId);
    } else {
      startVoiceChat(formattedUserId);
    }
  };
// 세션을 생성하는 함수
const createSession = async (customSessionId) => {
  try {
    const response = await fetch('http://localhost:8888/api/v1/sessions', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ customSessionId }),
    });

    if (response.status === 409) {
      console.log('Session already exists, fetching token');
    } else if (!response.ok) {
      const errorText = await response.text(); // 응답 본문을 텍스트로 읽기
      throw new Error(`Failed to create session: ${response.statusText}. ${errorText}`);
    }

    const sessionId = await response.text(); // 응답을 텍스트로 읽기
    return sessionId;
  } catch (error) {
    console.error('Error creating session:', error);
    throw error;
  }
};

// 토큰을 가져오는 함수
const getToken = async (customSessionId) => {
  try {
    // 세션 생성
    const sessionId = await createSession(customSessionId);

    // 토큰 생성
    const tokenResponse = await fetch(`http://localhost:8888/api/v1/sessions/${sessionId}/connections`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({}),
    }); 

    if (tokenResponse.status != 200) {
      const errorText = await tokenResponse.text(); // 응답 본문을 텍스트로 읽기
      throw new Error(`Failed to create token: ${tokenResponse.statusText}. ${errorText}`);
    }

    // 응답을 텍스트로 읽기
    const webSocketUrl = await tokenResponse.text();
    console.log('WebSocket URL:', webSocketUrl);

     // URL에서 토큰 추출
     const urlParams = new URLSearchParams(new URL(webSocketUrl).search);
     const token = urlParams.get('token');
     return token;

  } catch (error) {
    console.error('Error fetching token:', error);
    throw error;
  }
};



  // 세션 연결을 해제하는 함수
  const disconnect = () => {
    if (session.value) {
      session.value.disconnect();
      console.log('Session disconnected');
      session.value = null; // 세션 초기화

      // 모든 사용자 음성 상태 초기화
      voiceStates.value.fill(false);
      publishers.value.fill(null);
      subscribers.value.forEach((subs) => subs.splice(0));
    }
  };

  // 음성 상태 확인 함수
  const isVoiceOn = (userId) => {
    return voiceStates.value[userId - 1];
  };

  return {
    initializeSession,
    toggleVoiceChat,
    disconnect,
    isVoiceOn,
  };
});
