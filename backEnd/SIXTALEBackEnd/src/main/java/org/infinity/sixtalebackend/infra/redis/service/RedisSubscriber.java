package org.infinity.sixtalebackend.infra.redis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
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

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            // Redis에서 메시지를 JSON 문자열로 역직렬화
            String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());

            // JSON 문자열을 ChatMessageRequest 객체로 변환
            ChatMessageRequest roomMessage = objectMapper.readValue(publishMessage, ChatMessageRequest.class);

            ChatMessageResponse chatMessageResponse = new ChatMessageResponse(roomMessage);

            System.out.println(new String(pattern));
            // 메시지 처리
            if (roomMessage.getType().equals(MessageType.TALK)) {
                messagingTemplate.convertAndSend("/sub/chat/room/" + roomMessage.getRoomID(), chatMessageResponse);
            } else if (roomMessage.getType().equals(MessageType.WHISPER)) {
                // messagingTemplate.convertAndSend("/sub/chat/whisper/" + roomMessage.getRoomID() + "/"+roomMessage.getMemberID(), chatMessageResponse);
                messagingTemplate.convertAndSend("/sub/chat/whisper/" + roomMessage.getRoomID() + "/"+roomMessage.getRecipientID(), chatMessageResponse);
            }
        } catch (JsonProcessingException e) {
            log.error("Failed to parse message: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error occurred: {}", e.getMessage());
        }
    }
}
