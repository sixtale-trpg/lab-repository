package org.infinity.sixtalebackend.domain.chat.service;

import org.infinity.sixtalebackend.domain.chat.dto.ChatMessageRequest;

public interface SheetLogService {
    void sendSheetChatMessage(ChatMessageRequest chatMessageRequest);
}
