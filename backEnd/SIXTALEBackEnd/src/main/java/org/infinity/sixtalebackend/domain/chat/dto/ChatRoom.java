package org.infinity.sixtalebackend.domain.chat.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.UUID;

@Getter
@Setter
public class ChatRoom {
    private String roomID;
    private String roomName;
    private long userCount;
    private HashMap<String,String> userList = new HashMap<String,String>();

    public ChatRoom create(String roomName) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomID = UUID.randomUUID().toString();
        chatRoom.roomName = roomName;

        return chatRoom;
    }
}
