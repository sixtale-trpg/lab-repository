package org.infinity.sixtalebackend.domain.chat.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class ChatRoom implements Serializable {
    private static final long serialVersionUID = 6494678977089006639L;

    private String roomID;
    private String name;

    public static ChatRoom create(String name) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomID = UUID.randomUUID().toString();
        chatRoom.name = name;
        return chatRoom;
    }
}
