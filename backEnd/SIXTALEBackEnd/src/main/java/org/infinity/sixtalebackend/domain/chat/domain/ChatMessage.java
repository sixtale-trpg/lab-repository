package org.infinity.sixtalebackend.domain.chat.domain;

import lombok.Getter;
import lombok.Setter;
import org.infinity.sixtalebackend.domain.chat.dto.MessageType;

@Getter
@Setter
public class ChatMessage {

    private MessageType type; // 메시지 타입
    private String roomID; // 방번호
    private String sender; // 메시지 보낸사람
    private String receiver; // 귓속말 수신자 ID
    private String message; // 메시지
}