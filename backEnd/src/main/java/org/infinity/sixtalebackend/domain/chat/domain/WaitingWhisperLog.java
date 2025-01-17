package org.infinity.sixtalebackend.domain.chat.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@Document(collection = "waiting_whisper_log")
public class WaitingWhisperLog {
    @Id
    private String id; // 자동으로 들어감
    private Long roomID;    // 채팅방 ID
    private Long memberID;    // 사용자 ID
    private Long recipientID; // 귓속말 수신자 ID
    private String content;   // 메시지 내용
    private LocalDateTime createdAt;

}
