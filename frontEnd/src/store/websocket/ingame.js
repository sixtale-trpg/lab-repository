import { Stomp } from "@stomp/stompjs";
import SockJS from "sockjs-client";

class InGameWebSocketService {
  constructor() {
    this.stompClient = null;
    this.connected = false;
    this.messageCallback = null;
    this.roomID = null;
  }

<<<<<<< HEAD
  connect(roomID) {
    if (!roomID) {
      console.error("Room ID is required to connect to WebSocket");
      return;
    }

    this.roomID = roomID;

    const socket = new SockJS("http://localhost:8888/api/v1/ws"); // 서버 URL
=======
  connect() {
    const socket = new SockJS('http://i11d108.p.ssafy.io:8888/api/v1/ws'); // 서버 URL
>>>>>>> f36c987828098a6284eb09aff424fa3bbf00e7b0
    this.stompClient = Stomp.over(socket);

    this.stompClient.connect(
      {},
      () => {
        this.connected = true;
        console.log("Connected to In-Game WebSocket");

        // 인게임 메시지 구독
        this.stompClient.subscribe(`/sub/game/messages/${this.roomID}`, (message) => {
          console.log("In-Game Message received:", message.body);
          const parsedMessage = JSON.parse(message.body);
          if (this.messageCallback) {
            this.messageCallback(parsedMessage);
          }
        });
      },
      (error) => {
        console.error("In-Game WebSocket connection error:", error);
      }
    );
  }

  // 인게임 메시지 콜백 설정
  onMessageReceived(callback) {
    this.messageCallback = callback;
  }

  // 메시지 전송
  sendMessage(message) {
    if (this.connected) {
      console.log("Sending in-game message:", message);
      this.stompClient.send("/pub/game/message", {}, JSON.stringify(message));
    } else {
      console.error("In-Game WebSocket is not connected");
    }
  }
}

export default new InGameWebSocketService();
