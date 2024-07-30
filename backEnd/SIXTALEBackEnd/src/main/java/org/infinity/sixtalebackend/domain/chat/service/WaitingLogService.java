package org.infinity.sixtalebackend.domain.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.infinity.sixtalebackend.domain.chat.dto.ChatMessageRequest;

public interface WaitingLogService {
    void sendWaitingChatMessage(ChatMessageRequest chatMessageRequest) ;
}
