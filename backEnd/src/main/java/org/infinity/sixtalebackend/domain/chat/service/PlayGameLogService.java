package org.infinity.sixtalebackend.domain.chat.service;

import org.infinity.sixtalebackend.domain.chat.dto.ChatMessageRequest;
import org.infinity.sixtalebackend.domain.chat.dto.GameMessageDto;

public interface PlayGameLogService {
    void sendPlayGameLogMessage(GameMessageDto gameMessageDto) ;
}