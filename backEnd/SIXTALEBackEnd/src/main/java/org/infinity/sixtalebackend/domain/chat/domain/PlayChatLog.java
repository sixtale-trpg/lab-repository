package org.infinity.sixtalebackend.domain.chat.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@Document(collection = "play_chat_log")
public class PlayChatLog {
    @Id
    private String id; // 자동으로 들어감
    private Long roomID;    // 채팅방 ID
    private Long memberID;    // 사용자 ID
    private String content;   // 메시지 내용
    private LocalDateTime createdAt;

    public PlayChatLog() {
        this.createdAt = LocalDateTime.now();
    }
}
