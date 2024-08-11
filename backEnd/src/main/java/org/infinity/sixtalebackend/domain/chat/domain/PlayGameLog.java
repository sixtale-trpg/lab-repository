package org.infinity.sixtalebackend.domain.chat.domain;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.infinity.sixtalebackend.domain.chat.dto.GameType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@Document(collection = "play_game_log")
public class PlayGameLog {
    @Id
    private String id; // 자동으로 들어감
    private Long roomID;
    private Long sheetID;
    @Enumerated(EnumType.STRING)
    private GameType gameType;
    private String content;
    private LocalDateTime createdAt;
}
