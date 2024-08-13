import { Stomp } from "@stomp/stompjs";
import SockJS from "sockjs-client";

class WebSocketService {
  constructor() {
    this.stompClient = null;
    this.connected = false;
    this.messageCallback = null;
    this.roomID = null;
    this.memberID = null;
    this.subscriptionUrls = [];
  }

  // WebSocket 및 STOMP 연결 설정
  connect(roomID, memberID) {
    if (!roomID) {
      console.error("Room ID is required to connect to WebSocket");
      return;
    }

    this.roomID = roomID;
    this.memberID = memberID;
    // WebSocket 및 STOMP 연결 설정

    const socket = new SockJS("wss://i11d108.p.ssafy.io/api/v1/ws"); // SockJS로 WebSocket 연결 생성
    this.stompClient = Stomp.over(socket); // STOMP 클라이언트 생성

    this.stompClient.connect(
      {},
      () => {
        this.connected = true;
        console.log("Connected to WebSocket");

        // 대기 방 구독
        this.subscribeToUrl(`/sub/waiting/chat/room/${this.roomID}`);

        // 귓속말 구독
        this.subscribeToUrl(
          `/sub/waiting/chat/whisper/${this.roomID}/${memberID}`
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

  // 메시지 수신 시 호출할 콜백 설정
  onMessageReceived(callback) {
    this.messageCallback = callback;
  }

  // 메시지를 서버로 전송
  sendMessage(message) {
    if (this.connected) {
      console.log("Sending message:", message);
      try {
        this.stompClient.send(
          "/pub/waiting/chat/message",
          {},
          JSON.stringify(message)
        ); // 메시지 전송
        console.log("Message sent successfully");
      } catch (error) {
        console.error("Error sending message:", error);
      }
    } else {
      console.error("WebSocket is not connected");
    }
  }

  // 특정 URL 구독
  subscribeToUrl(url) {
    this.stompClient.subscribe(url, (message) => {
      console.log(`Message received from ${url}:`, message.body);
      const parsedMessage = JSON.parse(message.body);
      if (this.callback) {
        this.messageCallback(parsedMessage);
      }
    });
  }
}

export default new WebSocketService();
