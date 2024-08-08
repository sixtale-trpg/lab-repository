package org.infinity.sixtalebackend.domain.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.infinity.sixtalebackend.domain.chat.domain.WaitingChatLog;
import org.infinity.sixtalebackend.domain.chat.dto.ChatMessageRequest;

import java.util.List;

public interface WaitingLogService {
    void sendWaitingChatMessage(ChatMessageRequest chatMessageRequest) ;
    public List<WaitingChatLog> getLogsByRoomID(Long roomID);
    // void selectWaitingLogList();
}
