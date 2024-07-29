package org.infinity.sixtalebackend.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatMessage {
    public enum MessageType{
        ENTER,TALK
    }
    private MessageType type; //메세지 타입
    private String roomID; // 방번호
    private String sender; //메시지 보낸 사람
    private String message; //메세지
}
