package org.infinity.sixtalebackend.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.infinity.sixtalebackend.domain.chat.dto.ChatMessageRequest;
import org.infinity.sixtalebackend.domain.chat.dto.GameMessageDto;
import org.infinity.sixtalebackend.domain.chat.service.PlayLogService;
import org.infinity.sixtalebackend.domain.chat.service.SheetLogService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Slf4j
@RequiredArgsConstructor
@Controller
@CrossOrigin(value = "*")
public class PlayChatController {
    private final PlayLogService playLogService;

    @MessageMapping("/play/chat/message")
    public void handlePlayChatMessage(ChatMessageRequest chatMessageRequest) {
        // 플레이방 채팅 처리
        playLogService.sendPlayChatMessage(chatMessageRequest);
    }

    @MessageMapping("/game/message")
    @SendTo("/sub/game/messages")
    public GameMessageDto handleGameMessage(GameMessageDto message){
        return message;
    }
}
