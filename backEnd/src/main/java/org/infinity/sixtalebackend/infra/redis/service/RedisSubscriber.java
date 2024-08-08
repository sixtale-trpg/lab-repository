package org.infinity.sixtalebackend.infra.redis.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.infinity.sixtalebackend.domain.chat.dto.*;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final RedisTemplate redisTemplate;
    private final SimpMessageSendingOperations messagingTemplate;

    /**
     * Redis에서 메시지가 발행(publish)되면 대기하고 있던 onMessage가 해당 메시지를 받아 처리한다.
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            // redis에서 발행된 데이터를 받아 deserialize
            String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
            // JSON 문자열을 ChatMessageRequest 객체로 변환
            ChatMessageRequest roomMessage = objectMapper.readValue(publishMessage, ChatMessageRequest.class);
            // 받는 메시지 ChatMessageResponse로 데이터 가공
            ChatMessageResponse chatMessageResponse = new ChatMessageResponse(roomMessage);

            // Websocket 구독자에게 채팅 메시지 Send
            // 메시지 처리
            if (roomMessage.getRoomType().equals(RoomType.WAITING)) {
                // 대기방 채팅
                if (roomMessage.getType().equals(MessageType.WHISPER)){
                    messagingTemplate.convertAndSend("/sub/waiting/chat/whisper/" + roomMessage.getRoomID() + "/" + roomMessage.getRecipientID(), chatMessageResponse);
                }
                else messagingTemplate.convertAndSend("/sub/waiting/chat/room/" + roomMessage.getRoomID(), chatMessageResponse);

            } else if (roomMessage.getRoomType().equals(RoomType.SHEET)) {
                if (roomMessage.getType().equals(MessageType.WHISPER)){
                    messagingTemplate.convertAndSend("/sub/sheet/chat/whisper/" + roomMessage.getRoomID() + "/" + roomMessage.getRecipientID(), chatMessageResponse);
                }
                else messagingTemplate.convertAndSend("/sub/sheet/chat/room/" + roomMessage.getRoomID(), chatMessageResponse);
            }else if (roomMessage.getRoomType().equals(RoomType.PLAY)) {
                if (roomMessage.getType().equals(MessageType.WHISPER)){
                    messagingTemplate.convertAndSend("/sub/play/chat/whisper/" + roomMessage.getRoomID() + "/" + roomMessage.getRecipientID(), chatMessageResponse);
                }
                else messagingTemplate.convertAndSend("/sub/play/chat/room/" + roomMessage.getRoomID(), chatMessageResponse);
            }

        } catch (Exception e) {
            log.error("Unexpected error occurred: {}", e.getMessage());
        }
    }
}
