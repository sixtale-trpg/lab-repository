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
            String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());

            ChatMessageRequest roomMessage = objectMapper.readValue(publishMessage, ChatMessageRequest.class);

            if (roomMessage.getType().equals(MessageType.TALK)) {
                ChatMessageResponse chatMessageResponse = new ChatMessageResponse(roomMessage);
                messagingTemplate.convertAndSend("/sub/chat/room/" + roomMessage.getRoomID(), chatMessageResponse);
            }
            // 추가 로직

        } catch (JsonProcessingException e) {
            log.error("Failed to parse message: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error occurred: {}", e.getMessage());
        }
    }
}
