package org.infinity.sixtalebackend.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import org.infinity.sixtalebackend.domain.chat.domain.ChatRoom;
import org.infinity.sixtalebackend.domain.chat.repository.ChatRoomRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatRoomController {
    private final ChatRoomRepository chatRoomRepository;

//    @GetMapping("/room")
//    public String rooms(Model model) {
//        System.out.println(11);
//        return "/chat/room";
//    }

    @GetMapping("/rooms")
    public List<ChatRoom> room() {
        return chatRoomRepository.findAllRoom();
    }

    @PostMapping("/room")
    public ChatRoom createRoom(@RequestParam String name) {
        return chatRoomRepository.createChatRoom(name);
    }

//    @GetMapping("/room/enter/{roomID}")
//    public String roomDetail(Model model, @PathVariable String roomID) {
//        System.out.println(12);
//
//        model.addAttribute("roomID", roomID);
//        return "/chat/roomdetail";
//    }

    @GetMapping("/room/{roomID}")
    public ChatRoom roomInfo(@PathVariable String roomID) {
        return chatRoomRepository.findRoomById(roomID);
    }
}
