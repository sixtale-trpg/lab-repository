package org.infinity.sixtalebackend.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDto {
    private MessageType type; //메세지 타입
    private Long roomID; // 방번호
    private Long memberID; // 사용자 ID
    private String message; //메세지
    private String time; //전송시간
    // private Long userCount; // 채팅방 인원 수
}
