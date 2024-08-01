package org.infinity.sixtalebackend.infra.websocket.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.config.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp") // ws://localhost:8080/ws-stomp
                .setAllowedOrigins("*");
    }
    /**
     * stomp websocket의 경우, handshake도중에 custom header 사용할 수 없음
     * ChannelInterceptor를 통해 정보 인증
     * => configureClientInboundChannel 에서 CONNECT를 맽는 순간에 nativeHeader user정보 뽑아서 Principal을 세션에 지정
     */

    /**
     *
     * 현재 접속한 세션이 어떤 유저인지 binding 시킴
     * -> 들어오는 세션에 대해 user를 매핑
     * CONNECT를 맽는 순간에 nativeHeader user정보 뽑아서 Principal을 세션에 지정
     * 이렇게 작업하거나 아니면 stompEndPoint 등록 시에 setHandshakeHandler를 등록 또는
     * 별도의 filter를 만들어서 동작해도 됨
     * @param registration
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                System.out.println(1111);
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                System.out.println(accessor);
                if(StompCommand.CONNECT.equals(accessor.getCommand())){
                    String user = accessor.getFirstNativeHeader("user");
                    System.out.println(user);
                    if(user!=null){
                        List<GrantedAuthority> authorities = new ArrayList<>();
                        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                        Authentication auth = new UsernamePasswordAuthenticationToken(user,user,authorities);
                        SecurityContextHolder.getContext().setAuthentication(auth);
                        accessor.setUser(auth);
                    }
                }
                return message;
            }
        });
    }
}
