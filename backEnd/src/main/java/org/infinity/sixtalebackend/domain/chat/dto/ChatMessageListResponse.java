package org.infinity.sixtalebackend.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ChatMessageListResponse {
    private List<ChatMessageResponse> chatMessageResponseList;
    private Integer totalPages;
    private Long totalElements;
}
