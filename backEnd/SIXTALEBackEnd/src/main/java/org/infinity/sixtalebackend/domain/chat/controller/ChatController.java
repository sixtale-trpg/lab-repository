package org.infinity.sixtalebackend.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.infinity.sixtalebackend.domain.chat.domain.ChatMessage;
import org.infinity.sixtalebackend.domain.chat.dto.ChatMessageRequest;
import org.infinity.sixtalebackend.domain.chat.dto.MessageType;
import org.infinity.sixtalebackend.domain.chat.repository.ChatRoomRepository;
import org.infinity.sixtalebackend.domain.chat.service.WaitingLogService;
import org.infinity.sixtalebackend.infra.redis.service.RedisPublisher;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@Controller
@CrossOrigin(value = "*")
public class ChatController {
    private final WaitingLogService waitingLogService;
    private final RedisPublisher redisPublisher;
    private final ChatRoomRepository chatRoomRepository;

//    @MessageMapping("/chat/message")
//    public void message(
//            ChatMessageRequest chatMessageRequest
//    ) {
//        Member member = memberRepository.findById(chatMessageRequest.getMemberID())
//                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다. id=" + chatMessageRequest.getMemberID()));
//        System.out.println(member.getNickname());
//
//        chatMessageService.sendMessage(chatMessageRequest, member);
//    }

    @MessageMapping("/waiting/chat/message")
    public void handleWaitingChatMessage(ChatMessageRequest chatMessageRequest) {
        // 대기방 채팅 처리
        waitingLogService.sendWaitingChatMessage(chatMessageRequest);
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


//    @MessageMapping("/chat/play/message")
//    public void messagePlay(PlayChatMessageRequest chatMessageRequest) {
//        Member member = memberRepository.findById(chatMessageRequest.getMemberID())
//                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다. id=" + chatMessageRequest.getMemberID()));
//
//        chatMessageService.sendPlayMessage(chatMessageRequest, member);
//    }
}
