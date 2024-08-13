class WebSocketService {
  constructor() {
    this.socket = null;
    this.connected = false;
    this.messageCallback = null;
    this.roomID = null;
    this.memberID = null;
  }

  // WebSocket 연결 설정
  connect(roomID, memberID) {
    if (!roomID) {
      console.error("Room ID is required to connect to WebSocket");
      return;
    }

    this.roomID = roomID;
    this.memberID = memberID;
    
    // WebSocket 연결 설정
    const wsUrl = `wss://i11d108.p.ssafy.io/api/v1/ws/${roomID}/${memberID}`; // 실제 서버 URL로 교체
    this.socket = new WebSocket(wsUrl);

    // WebSocket 이벤트 핸들러 설정
    this.socket.onopen = () => {
      this.connected = true;
      console.log("Connected to WebSocket");
    };

    this.socket.onmessage = (event) => {
      const parsedMessage = JSON.parse(event.data);
      console.log("Message received: ", parsedMessage);
      if (this.messageCallback) {
        this.messageCallback(parsedMessage);
      }
    };

    this.socket.onclose = () => {
      this.connected = false;
      console.log("WebSocket connection closed");
    };

    this.socket.onerror = (error) => {
      console.error("WebSocket connection error: ", error);
    };
  }

  // 메시지 수신 시 호출할 콜백 설정
  onMessageReceived(callback) {
    this.messageCallback = callback;
  }

  // 메시지를 서버로 전송
  sendMessage(message) {
    if (this.connected && this.socket) {
      console.log("Sending message:", message);
      try {
        this.socket.send(JSON.stringify(message)); // 메시지 전송
        console.log("Message sent successfully");
      } catch (error) {
        console.error("Error sending message:", error);
      }
    } else {
      console.error("WebSocket is not connected");
    }
  }

  // WebSocket 연결 해제
  disconnect() {
    if (this.socket) {
      this.socket.close();
      this.connected = false;
    }
  }
}

export default new WebSocketService();
