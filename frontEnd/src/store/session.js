import { defineStore } from 'pinia';
import { OpenVidu } from 'openvidu-browser';

export const useSessionStore = defineStore('session', {
  state: () => ({
    OV: null,
    session: null,
    publishers: Array(8).fill(null), // 각 사용자에 대한 발행자
    subscribers: Array(8).fill([]),  // 각 사용자에 대한 구독자
    voiceStates: Array(8).fill(false), // 각 사용자의 음성 상태
  }),

  actions: {
    async initializeSession(userId) {
      // 새로운 OpenVidu 인스턴스를 생성
      this.OV = new OpenVidu();
      this.session = this.OV.initSession();

      // 스트림 생성 이벤트 리스너 등록
      this.session.on('streamCreated', (event) => {
        const subscriber = this.session.subscribe(event.stream, `video-${event.stream.connection.connectionId}`);
        const index = parseInt(event.stream.connection.data.split(' ')[1]) - 1;
        this.subscribers[index].push(subscriber);
      });

      // 스트림 제거 이벤트 리스너 등록
      this.session.on('streamDestroyed', (event) => {
        const index = parseInt(event.stream.connection.data.split(' ')[1]) - 1;
        this.subscribers[index] = this.subscribers[index].filter(
          (sub) => sub !== event.stream
        );
      });

      // 세션에 연결
      try {
        const token = await this.getToken();
        await this.session.connect(token, { clientData: `User ${userId}` });

        const publisherInstance = await this.OV.initPublisherAsync(`video-${userId}`, {
          audioSource: undefined,
          videoSource: false,
          publishAudio: true,
          publishVideo: false,
          resolution: '640x480',
          frameRate: 30,
          insertMode: 'APPEND',
          mirror: false,
        });

        this.session.publish(publisherInstance);
        this.publishers[userId - 1] = publisherInstance;
        this.voiceStates[userId - 1] = true;
      } catch (error) {
        console.error('세션에 연결하는 중 오류가 발생했습니다:', error);
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
          console.log('세션이 이미 존재합니다, 토큰을 얻으러 갑니다.');
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

        const { token } = await tokenResponse.json();
        return token;
      } catch (error) {
        console.error('토큰을 가져오는 중 오류가 발생했습니다:', error);
        throw error;
      }
    },

    toggleVoiceChat(userId) {
      const isVoiceOn = this.voiceStates[userId - 1];
      if (isVoiceOn) {
        this.stopVoiceChat(userId);
      } else {
        this.initializeSession(userId); // 세션을 새로 초기화
      }
      this.voiceStates[userId - 1] = !isVoiceOn;
    },

    stopVoiceChat(userId) {
      const publisher = this.publishers[userId - 1];

      if (publisher) {
        try {
          this.session.unpublish(publisher);
          this.publishers[userId - 1] = null;
        } catch (error) {
          console.error('발행자 제거 중 오류가 발생했습니다:', error);
        }
      }

      this.subscribers[userId - 1].forEach((subscriber) => {
        this.session.unsubscribe(subscriber);
      });

      this.subscribers[userId - 1] = [];
    },

    disconnect() {
      if (this.session) {
        this.session.disconnect();
        this.session = null;
        this.OV = null;
        this.publishers.fill(null);
        this.subscribers.fill([]);
        this.voiceStates.fill(false);
      }
    },
  },
});
