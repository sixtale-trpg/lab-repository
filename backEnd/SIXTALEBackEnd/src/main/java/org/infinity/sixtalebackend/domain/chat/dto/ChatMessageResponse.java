package org.infinity.sixtalebackend.domain.chat.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponse {
    private Long roomID;    // 채팅방 ID
    private String sender;   // 발신자 ID
    // private String recipientID; // 귓속말 수신자 ID
    private String content;  // 메시지 내용
    private MessageType type; // 메시지 유형

    public ChatMessageResponse(ChatMessageRequest request) {
        this.roomID = request.getRoomID();
        this.sender = request.getNickName();
        this.content = request.getContent();
        this.type = request.getType();
    }

}