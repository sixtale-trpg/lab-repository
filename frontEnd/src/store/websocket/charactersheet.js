import { Stomp } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

class SheetWebSocketService {
    constructor() {
        this.stompClient = null;
        this.connected = false;
        this.messageCallback = null;
    }

    connect() {
        const socket = new SockJS('http://i11d108.p.ssafy.io:8888/api/v1/ws');
        this.stompClient = Stomp.over(socket);

        this.stompClient.connect({}, () => {
            this.connected = true;
            console.log('Connected to WebSocket for Sheet');

            // 캐릭터시트 메시지 구독
            this.stompClient.subscribe('/sub/sheet/chat/messages', (message) => {
                console.log('Sheet Message received:', message.body);
                const parsedMessage = JSON.parse(message.body);
                if (this.messageCallback) {
                    this.messageCallback(parsedMessage);
                }
            });
        }, (error) => {
            console.error('WebSocket connection error for Sheet:', error);
        });
    }

    onMessageReceived(callback) {
        this.messageCallback = callback;
    }

    sendMessage(message) {
        if (this.connected) {
            console.log('Sending sheet message:', message);
            this.stompClient.send('/sheet/chat/message', {}, JSON.stringify(message));
        } else {
            console.error('WebSocket for Sheet is not connected');
        }
    }
}

export default new SheetWebSocketService();
