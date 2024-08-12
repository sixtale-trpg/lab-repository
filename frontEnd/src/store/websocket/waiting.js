import { Stomp } from "@stomp/stompjs";
import SockJS from "sockjs-client";

class WebSocketService {
  constructor() {
    this.stompClient = null;
    this.connected = false;
    this.messageCallback = null;
    this.roomID = null;
  }

  // WebSocket 및 STOMP 연결 설정
  connect(roomID) {
    if (!roomID) {
      console.error("Room ID is required to connect to WebSocket");
      return;
    }

    this.roomID = roomID;

    const socket = new SockJS("http://localhost:8888/api/v1/ws"); // SockJS로 WebSocket 연결 생성
    this.stompClient = Stomp.over(socket); // STOMP 클라이언트 생성

    this.stompClient.connect(
      {},
      () => {
        this.connected = true;
        console.log("Connected to WebSocket");

        // 메시지 구독 설정
        this.stompClient.subscribe(
          `/sub/waiting/chat/room/${this.roomID}`,
          (message) => {
            console.log("Message received:", message.body);
            const parsedMessage = JSON.parse(message.body); // 수신된 메시지 파싱
            if (this.messageCallback) {
              this.messageCallback(parsedMessage); // 콜백 함수 호출
            }
          }
        );
      },
      (error) => {
        console.error("WebSocket connection error:", error); // 연결 오류 처리
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
      console.log("Sending message:", message);
      this.stompClient.send(
        "/pub/waiting/chat/message",
        {},
        JSON.stringify(message)
      ); // 메시지 전송
    } else {
      console.error("WebSocket is not connected");
    }
  }
}

export default new WebSocketService();
