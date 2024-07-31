package org.infinity.sixtalebackend.infra.websocket.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 메시지 구독 Url (topic을 구독)
        config.enableSimpleBroker("/sub");
        // 메시지 발행 url
        config.setApplicationDestinationPrefixes("/pub");
        // 특정 유저에게 보내는 사용자 path 지정
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp") // ws://localhost:8080/ws-stomp
                .setAllowedOrigins("*");
    }

    /**
     * 현재 접속한 세션이 어떤 유저인지 binding 시킴 
     * -> 들어오는 세션에 대해 user를 매핑
     * 이렇게 작업하거나 아니면 stompEndPoint 등록 시에 setHandshakeHandler를 등록 또는
     * 별도의 filter를 만들어서 동작해도 됨
     * @param registration
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        WebSocketMessageBrokerConfigurer.super.configureClientInboundChannel(registration);
    }
}
