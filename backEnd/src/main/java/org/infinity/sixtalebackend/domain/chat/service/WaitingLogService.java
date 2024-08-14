package org.infinity.sixtalebackend.domain.chat.service;

import org.infinity.sixtalebackend.domain.chat.dto.ChatMessageListResponse;
import org.infinity.sixtalebackend.domain.chat.dto.ChatMessageRequest;
import org.springframework.data.domain.Pageable;

public interface WaitingLogService {
    ChatMessageRequest sendWaitingChatMessage(ChatMessageRequest chatMessageRequest) ;
    ChatMessageListResponse getWaitingChatLogList(Long roomID, Pageable pageable);

    ChatMessageListResponse getWaitingChatWisperLogList(Long roomID,Long memberID,Pageable pageable);

    ChatMessageListResponse getWaitingChatAllLogList(Long roomID,Long memberID,Pageable pageable);
}
