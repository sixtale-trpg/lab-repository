package org.infinity.sixtalebackend.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Builder
public class ChatRoom implements Serializable {

    private String roomID;
    private String name;

    public ChatRoom(String roomID, String name) {
        this.roomID = roomID;
        this.name = name;
    }
}
