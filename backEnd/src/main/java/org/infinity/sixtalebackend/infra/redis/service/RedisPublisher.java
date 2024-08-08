package org.infinity.sixtalebackend.infra.redis.service;

import lombok.RequiredArgsConstructor;
import org.infinity.sixtalebackend.domain.chat.dto.ChatMessageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RedisPublisher {
    private final RedisTemplate redisTemplate;

    /**
     * 처음 방 입장했을 때 토픽 구독
     * @param channelTopic
     * @param chatMessageRequest
     */
    public void publish(ChannelTopic channelTopic, ChatMessageRequest chatMessageRequest){
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessageRequest);
    }
}
