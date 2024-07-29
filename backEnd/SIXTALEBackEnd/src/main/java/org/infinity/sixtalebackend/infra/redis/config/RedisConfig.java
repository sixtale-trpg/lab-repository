package org.infinity.sixtalebackend.infra.redis.config;

import lombok.RequiredArgsConstructor;
import org.infinity.sixtalebackend.infra.redis.service.RedisSubscriber;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer( // (1)
                                                                        RedisConnectionFactory connectionFactory,
                                                                        MessageListenerAdapter listenerAdapter,
                                                                        ChannelTopic channelTopic
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, channelTopic);
        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(RedisSubscriber subscriber) { // (2)
        return new MessageListenerAdapter(subscriber, "onMessage");
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate
            (RedisConnectionFactory connectionFactory) { // (3)
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));
        return redisTemplate;
    }

    @Bean
    public ChannelTopic waitingChatTopic() {
        return new ChannelTopic("waiting_chat_topic");
    }

    @Bean
    public ChannelTopic waitingWhisperTopic() {
        return new ChannelTopic("waiting_whisper_topic");
    }
}
