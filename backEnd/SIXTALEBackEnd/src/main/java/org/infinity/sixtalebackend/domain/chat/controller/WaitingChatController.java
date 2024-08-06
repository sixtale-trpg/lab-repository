package org.infinity.sixtalebackend.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.infinity.sixtalebackend.domain.chat.dto.ChatMessageRequest;
import org.infinity.sixtalebackend.domain.chat.dto.GameMessageDto;
import org.infinity.sixtalebackend.domain.chat.service.WaitingLogService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@Controller
@CrossOrigin(value = "*")
public class WaitingChatController {
    private final WaitingLogService waitingLogService;

    @MessageMapping("/waiting/chat/message")
    public void handleWaitingChatMessage(ChatMessageRequest chatMessageRequest) {
        // 대기방 채팅 처리
        waitingLogService.sendWaitingChatMessage(chatMessageRequest);
    }

    @MessageMapping("/game/message")
    @SendTo("/sub/game/messages")
    public GameMessageDto handleGameMessage(GameMessageDto message){
        return message;
    }

//    @MessageMapping("/waiting/whisper/message")
//    public void handleWaitingWhisperMessage(ChatMessageRequest chatMessageRequest) {
//        // 대기방 귓속말 처리
//        waitingLogService.sendWaitingChatMessage(chatMessageRequest);
//    }

    /**
     * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
     */
//    @MessageMapping("/chat/message")
//    public void message(ChatMessage message) {
//        if (MessageType.ENTER.equals(message.getType())) {
//            // 채팅 입장 시, 룸 아이디로 토픽 생성
//            chatRoomRepository.enterChatRoom(String.valueOf(message.getRoomID()));
//            message.setMessage(message.getSender()+ "님이 입장하셨습니다.");
//        }
//        // Websocket에 발행된 메시지를 redis로 발행한다(publish)
//        redisPublisher.publish(chatRoomRepository.getTopic(String.valueOf(message.getRoomID())), message);
//    }


}
