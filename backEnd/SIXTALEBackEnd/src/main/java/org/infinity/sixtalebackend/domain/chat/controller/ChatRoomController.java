package org.infinity.sixtalebackend.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import org.infinity.sixtalebackend.domain.chat.dto.ChatRoom;
import org.infinity.sixtalebackend.domain.chat.repository.ChatRoomRepository;
import org.infinity.sixtalebackend.domain.room.service.RoomServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatRoomController {
    private final ChatRoomRepository chatRoomRepository;
    private final RoomServiceImpl roomService;

//    @GetMapping("/room")
//    public String rooms(Model model) {
//        System.out.println(11);
//        return "/chat/room";
//    }

    @GetMapping("/rooms")
    public List<ChatRoom> room() {
        return chatRoomRepository.findAllRoom();
    }

    /**
     * room을 만들때 topic 생성
     * @param name
     * @return
     */
    @PostMapping("/room")
    public ChatRoom createRoom(@RequestParam String name) {
        return chatRoomRepository.createChatRoom(name);
    }

    /**
     * 게임 방 생성
     */
//    @PostMapping("/room")
//    public ResponseEntity createRoomTest(@RequestBody RoomCreateRequest roomCreateRequest) {
//        try {
//            Long gmID = 1L;
//            RoomResponse roomResponse = roomService.createRoom(roomCreateRequest, gmID);
//            return new ResponseEntity(DefaultResponse.res(StatusCode.CREATED, ResponseMessage.CREATE_ROOM, roomResponse), HttpStatus.CREATED);
//        } catch (IllegalArgumentException e) {
//            return new ResponseEntity(DefaultResponse.res(StatusCode.BAD_REQUEST, ResponseMessage.CREATE_ROOM_FAIL), HttpStatus.BAD_REQUEST);
//        }  catch (Exception e) {
//            return new ResponseEntity(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

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
