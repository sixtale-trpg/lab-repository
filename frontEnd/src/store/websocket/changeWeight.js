//안씁니다
import { Stomp } from "@stomp/stompjs";
import SockJS from "sockjs-client";

class ChangeWeightWebSocketService {
  constructor() {
    this.stompClient = null;
    this.connected = false;
    this.messageCallback = null;
    this.roomID = null;
  }

  connect(roomID) {
    if (!roomID) {
      console.error("Room ID is required to connect to WebSocket");
      return;
    }
    this.roomID = roomID; // Store the roomID

    const socket = new SockJS("https://i11d108.p.ssafy.io/api/v1/ws"); // 서버 URL
    this.stompClient = Stomp.over(socket); // STOMP 클라이언트 생성

    this.stompClient.connect(
      {},
      () => {
        this.connected = true;
        console.log("Connected to change weight WebSocket");

        // 메시지 구독 설정
        this.stompClient.subscribe(
          `/sub/game/messages/${this.roomID}`,
          (message) => {
            console.log("Game Log received:", message.body);
            const parsedMessage = JSON.parse(message.body);
            if (this.messageCallback) {
              this.messageCallback(parsedMessage);
            }
          }
        );
      },
      (error) => {
        console.error("change weight WebSocket connection error:", error); // 연결 오류 처리
      }
    );
  }

  // 메시지 수신 시 호출할 콜백 설정
  onMessageReceived(callback) {
    this.messageCallback = callback;
  }

  // 메시지를 서버로 전송
  sendMessage(message) {
    if (this.connected) {
      console.log("Sending in-game message:", message);
      this.stompClient.send("/pub/game/message", {}, JSON.stringify(message)); // 메시지 전송
    } else {
      console.error("change weight WebSocket is not connected");
    }
  }
}

export default new ChangeWeightWebSocketService();
