package org.infinity.sixtalebackend.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.infinity.sixtalebackend.domain.chat.dto.ChatMessageRequest;
import org.infinity.sixtalebackend.domain.chat.dto.GameMessageDto;
import org.infinity.sixtalebackend.domain.chat.service.SheetLogService;
import org.infinity.sixtalebackend.domain.chat.service.WaitingLogService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Slf4j
@RequiredArgsConstructor
@Controller
@CrossOrigin(value = "*")
public class SheetChatController {
    private final SheetLogService sheetLogService;

    @MessageMapping("/sheet/chat/message")
    public void handleSheetChatMessage(ChatMessageRequest chatMessageRequest) {
        // 캐릭터시트방 채팅 처리
        sheetLogService.sendSheetChatMessage(chatMessageRequest);
    }
}
