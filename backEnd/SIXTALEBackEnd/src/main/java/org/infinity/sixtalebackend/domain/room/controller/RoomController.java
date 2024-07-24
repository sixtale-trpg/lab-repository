package org.infinity.sixtalebackend.domain.room.controller;

import lombok.AllArgsConstructor;
import org.infinity.sixtalebackend.domain.room.domain.Room;
import org.infinity.sixtalebackend.domain.room.dto.RoomResponse;
import org.infinity.sixtalebackend.domain.room.service.RoomService;
import org.infinity.sixtalebackend.domain.room.service.RoomServiceImpl;
import org.infinity.sixtalebackend.global.common.response.DefaultResponse;
import org.infinity.sixtalebackend.global.common.response.ResponseMessage;
import org.infinity.sixtalebackend.global.common.response.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rooms")
@AllArgsConstructor
public class RoomController {
    private final RoomService roomServiceImpl;

    @PostMapping("/{roomID}/players")
    public ResponseEntity addPlayerToRoom(@PathVariable Long roomID) {
        try {
            // id = 2 유저 가정
            Long memberID = 2L;
            RoomResponse roomResponse = roomServiceImpl.addPlayerToRoom(roomID, memberID);
            return new ResponseEntity(DefaultResponse.res(StatusCode.CREATED, ResponseMessage.ENTER_USER, roomResponse), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(DefaultResponse.res(StatusCode.BAD_REQUEST, ResponseMessage.ENTER_USER_FAIL), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{roomID}/status")
    public ResponseEntity addPlayerToRoom(@PathVariable Long roomID, @RequestBody Room room) {
        try {
            roomServiceImpl.updateRoomStatus(roomID, room.getStatus());
            return new ResponseEntity(DefaultResponse.res(StatusCode.CREATED, ResponseMessage.UPDATE_ROOM_STATUS), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(DefaultResponse.res(StatusCode.BAD_REQUEST, ResponseMessage.UPDATE_ROOM_STATUS_FAIL), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
