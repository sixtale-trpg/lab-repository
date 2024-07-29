package org.infinity.sixtalebackend.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.infinity.sixtalebackend.domain.chat.dto.ChatRoom;
import org.infinity.sixtalebackend.domain.chat.repository.ChatRepository;
import org.infinity.sixtalebackend.global.common.response.DefaultResponse;
import org.infinity.sixtalebackend.global.common.response.ResponseMessage;
import org.infinity.sixtalebackend.global.common.response.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chatroom")
public class ChatRoomController {
    private final ChatRepository chatRepository;

    // 채팅 리스트 화면
    @GetMapping("")
    public ResponseEntity<?> goChatRoom(){
        List<ChatRoom> chatRooms = chatRepository.findAllRoom();
        return new ResponseEntity<>(DefaultResponse.res(StatusCode.OK,"채팅 리스트 조회 성공",chatRooms), HttpStatus.OK);
    }

    // 채팅방 생성
    @PostMapping("/room")
    public ResponseEntity<?> createRoom(@RequestParam String name){
        ChatRoom chatRoom = chatRepository.createChatRoom(name);
        return new ResponseEntity<>(DefaultResponse.res(StatusCode.OK,"채팅방 생성",chatRoom.getRoomID()), HttpStatus.OK);
    }

    // 채팅에 참여한 유저 리스트 반환
    @GetMapping("/userlist")
    public ArrayList<String> userList(String roomID){
        return chatRepository.getUserList(roomID);
    }
}
