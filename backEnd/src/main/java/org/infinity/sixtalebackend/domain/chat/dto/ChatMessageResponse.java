package org.infinity.sixtalebackend.domain.chat.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponse {
    private Long roomID;    // 채팅방 ID
    private String sender;   // 발신자
    private String recipient; // 귓속말 수신자
    private String content;  // 메시지 내용
    private MessageType type; // 메시지 유형
    private RoomType roomType;
    private String createdAt;

    public ChatMessageResponse(ChatMessageRequest request) {
        this.roomID = request.getRoomID();
        this.sender = request.getNickName();
        this.recipient = request.getRecipientNickName();
        this.content = request.getContent();
        this.type = request.getType();
        this.roomType = request.getRoomType();
        this.createdAt = request.getCreatedAt();
    }

}