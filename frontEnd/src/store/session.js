import { defineStore } from "pinia";
import { ref } from "vue";
import { OpenVidu } from "openvidu-browser";

export const useSessionStore = defineStore("session", () => {
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
      session.value.on("streamCreated", (event) => {
        const subUserId = parseInt(event.stream.connection.data.split("User ")[1], 10);
        console.log(`Subscribing to userId: ${subUserId}`);

        const subscriber = session.value.subscribe(event.stream, `video-${subUserId}`);
        subscriber.on("streamPlaying", () => {
          console.log(`Stream playing for userId: ${subUserId}`);
        });

        subscribers.value[subUserId - 1].push(subscriber);
      });

      // 스트림 제거 이벤트 처리
      session.value.on("streamDestroyed", (event) => {
        const subUserId = parseInt(event.stream.connection.data.split("User ")[1], 10);
        console.log(`Stream destroyed for userId: ${subUserId}`);
        subscribers.value[subUserId - 1] = [];
      });

      session.value.on("connectionCreated", (event) => {
        console.log(`Connection created: ${event.connection.connectionId}`);
      });

      session.value.on("connectionDestroyed", (event) => {
        console.log(`Connection destroyed: ${event.connection.connectionId}`);
      });
    }
  };

  // 음성 채팅을 시작하는 함수
  const startVoiceChat = async (userId) => {
    if (!session.value) {
      initializeSession();
    }

    try {
      const token = await getToken();
      await session.value.connect(token, { clientData: `User ${userId}` });
      console.log(`Connected to session for userId: ${userId}`);

      const publisherInstance = await OV.value.initPublisherAsync(`video-${userId}`, {
        audioSource: true,
        videoSource: false,
        publishAudio: true,
        publishVideo: false,
        resolution: "640x480",
        frameRate: 30,
        insertMode: "APPEND",
        mirror: false,
      });

      session.value.publish(publisherInstance);
      publishers.value[userId - 1] = publisherInstance;
      console.log(`Publisher started for userId: ${userId}`);
      voiceStates.value[userId - 1] = true;

      // 오디오 트랙 이벤트 등록
      publisherInstance.on("streamAudioVolumeChange", (event) => {
        console.log(`Audio volume changed for userId ${userId}: `, event.newValue);
      });

      publisherInstance.on("streamPlaying", () => {
        console.log(`Stream is playing for userId: ${userId}`);
      });
    } catch (error) {
      console.error("Error connecting to session:", error);
    }
  };

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
    if (voiceStates.value[userId - 1]) {
      stopVoiceChat(userId);
    } else {
      startVoiceChat(userId);
    }
  };

  // 토큰을 가져오는 함수
  const getToken = async () => {
    try {
      const response = await fetch("https://i11d108.p.ssafy.io/api/v1/sessions", {
        method: "POST",
        headers: {
          //   Authorization: `Basic ${btoa('OPENVIDUAPP:MY_SECRET')}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ customSessionId: "SessionA" }),
        mode: "no-cors",
      });
      console.log(response.data);

      if (response.status === 409) {
        console.log("Session already exists, fetching token");
      } else if (!response.ok) {
        throw new Error(`Failed to create session: ${response.statusText}`);
      }

      const tokenResponse = await fetch("https://i11d108.p.ssafy.io/api/v1/tokens", {
        method: "POST",
        headers: {
          Authorization: `Basic ${btoa("OPENVIDUAPP:MY_SECRET")}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ session: "SessionA" }),
        mode: "no-cors",
      });

      if (!tokenResponse.ok) {
        throw new Error(`Failed to create token: ${tokenResponse.statusText}`);
      }

      const token = await tokenResponse.json();
      return token.token;
    } catch (error) {
      console.error("Error fetching token:", error);
      throw error;
    }
  };

  // 세션 연결을 해제하는 함수
  const disconnect = () => {
    if (session.value) {
      session.value.disconnect();
      console.log("Session disconnected");
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
