package org.infinity.sixtalebackend.domain.chat.service;

import org.infinity.sixtalebackend.domain.chat.dto.ChatMessageRequest;

public interface WaitingLogService {
    void sendWaitingChatMessage(ChatMessageRequest chatMessageRequest);
}
