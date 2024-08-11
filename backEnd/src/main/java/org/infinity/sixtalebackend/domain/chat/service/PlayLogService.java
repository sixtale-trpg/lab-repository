package org.infinity.sixtalebackend.domain.chat.service;

import org.infinity.sixtalebackend.domain.chat.dto.ChatMessageListResponse;
import org.infinity.sixtalebackend.domain.chat.dto.ChatMessageRequest;
import org.springframework.data.domain.Pageable;

public interface PlayLogService {
    void sendPlayChatMessage(ChatMessageRequest chatMessageRequest) ;
    ChatMessageListResponse getPlayChatLogList(Long roomID, Pageable pageable);

    ChatMessageListResponse getPlayChatWisperLogList(Long roomID,Long memberID,Pageable pageable);

    ChatMessageListResponse getPlayChatAllLogList(Long roomID,Long memberID,Pageable pageable);

}
