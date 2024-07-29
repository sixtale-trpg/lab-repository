package org.infinity.sixtalebackend.domain.chat.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatDTO {
    public enum MessageType{
        ENTER,TALK,LEAVE;
    }

    private MessageType type;
    private String roomID;
    private String sender;
    private String message;
    private LocalDateTime time; //채팅 발송 시간
}
