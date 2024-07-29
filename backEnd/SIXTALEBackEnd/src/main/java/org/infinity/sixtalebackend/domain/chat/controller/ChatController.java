package org.infinity.sixtalebackend.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.infinity.sixtalebackend.domain.chat.dto.ChatDTO;
import org.infinity.sixtalebackend.domain.chat.repository.ChatRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatController {
    private final static String CHAT_EXCHANGE_NAME = "chat.exchange";
    private final static String CHAT_QUEUE_NAME = "chat.queue";

    // private final SimpMessageSendingOperations template;
    private final RabbitTemplate rabbitTemplate;
    private final ChatRepository chatRepository;

    /**
     * /pub/chat.message.{roomID} 로 요청하면 브로커를 통해 처리
     * /exchange/chat.exchange/room.{roomID} 를 구독한 클라이언트에 메시지가 전송된다.
     * @param chat
     * @param chatRoomID
     */
    @MessageMapping("chat.enter.{chatRoomID}")
    public void enterUser(@Payload ChatDTO chat, @DestinationVariable String chatRoomID) {
        chat.setTime(LocalDateTime.now());
        chat.setMessage(chat.getSender()+ " 님 입장!!");
        rabbitTemplate.convertAndSend(CHAT_EXCHANGE_NAME,"room."+chatRoomID,chat);
    }

    @MessageMapping("chat.message.{chatRoomID")
    public void sendMessage(@Payload ChatDTO chat,@DestinationVariable String chatRoomID){
        chat.setTime(LocalDateTime.now());
        chat.setMessage(chat.getMessage());
        rabbitTemplate.convertAndSend(CHAT_EXCHANGE_NAME,"room."+chatRoomID,chat);
    }

    /**
     * 기본적으로 chat.queue가 exchange에 바인딩 되어있기 때문에 모든 메시지 처리
     * @param chatDTO
     */
    @RabbitListener(queues = CHAT_QUEUE_NAME)
    public void receive(ChatDTO chatDTO){
        System.out.println("received: "+chatDTO.getMessage());
    }

//    @MessageMapping("/enterUser")
//    public void enterUser(@Payload ChatDTO chat, SimpMessageHeaderAccessor headerAccessor) {
//        chatRepository.plusUserCnt(chat.getRoomID());
//        String userUUID = chatRepository.addUser(chat.getRoomID(), chat.getSender());
//
//        headerAccessor.getSessionAttributes().put("userUUID", userUUID);
//        headerAccessor.getSessionAttributes().put("roomID", chat.getRoomID());
//
//        chat.setMessage(chat.getSender() + " 님이 입장하셨습니다.");
//        template.convertAndSend("/sub/chat/room/" + chat.getRoomID(), chat);
//    }
//
//    @MessageMapping("/sendMessage")
//    public void sendMessage(@Payload ChatDTO chat){
//        log.info("CHAT {}",chat);
//        chat.setMessage(chat.getMessage());
//        template.convertAndSend("/sub/chat/room/" + chat.getRoomID(), chat);
//    }
}
