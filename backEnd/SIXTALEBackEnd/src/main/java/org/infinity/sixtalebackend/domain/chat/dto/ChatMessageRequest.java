package org.infinity.sixtalebackend.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageRequest {
    private Long roomID;       // 채팅방 ID
    private Long memberID;       // 사용자 ID
    private String nickName; // 사용자 닉네임
    private Long recipientID; // 귓속말 수신자 ID
    private String recipientNickName; // 귓속말 수신자 닉네임
    private String content;      // 메시지 내용
    private MessageType type;    // 메시지 유형
}