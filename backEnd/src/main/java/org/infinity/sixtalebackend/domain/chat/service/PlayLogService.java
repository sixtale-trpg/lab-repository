package org.infinity.sixtalebackend.domain.chat.service;

import org.infinity.sixtalebackend.domain.chat.dto.ChatMessageRequest;

public interface PlayLogService {
    void sendPlayChatMessage(ChatMessageRequest chatMessageRequest) ;
}
