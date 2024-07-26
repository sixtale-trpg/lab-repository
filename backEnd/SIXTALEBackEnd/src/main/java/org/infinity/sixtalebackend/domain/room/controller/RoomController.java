package org.infinity.sixtalebackend.domain.room.controller;

import lombok.AllArgsConstructor;
import org.infinity.sixtalebackend.domain.room.domain.Room;
import org.infinity.sixtalebackend.domain.room.domain.RoomStatus;
import org.infinity.sixtalebackend.domain.room.dto.*;
import org.infinity.sixtalebackend.domain.room.service.RoomService;
import org.infinity.sixtalebackend.domain.room.service.RoomServiceImpl;
import org.infinity.sixtalebackend.global.common.response.DefaultResponse;
import org.infinity.sixtalebackend.global.common.response.ResponseMessage;
import org.infinity.sixtalebackend.global.common.response.StatusCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("/rooms")
@AllArgsConstructor
public class RoomController {
    private final RoomService roomServiceImpl;

    /**
     * 게임 방 유저 입장
     */
    @PostMapping("/{roomID}/players")
    public ResponseEntity addPlayerToRoom(@PathVariable Long roomID) {
        try {
            // id = 1 유저 가정
            Long memberID = 1L;
            RoomResponse roomResponse = roomServiceImpl.addPlayerToRoom(roomID, memberID);
            return new ResponseEntity(DefaultResponse.res(StatusCode.CREATED, ResponseMessage.ENTER_USER, roomResponse), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(DefaultResponse.res(StatusCode.BAD_REQUEST, ResponseMessage.ENTER_USER_FAIL), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * 게임 방 유저 퇴장
     */
    @DeleteMapping("{roomID}/players")
    public ResponseEntity deletePlayerFromRoom(@PathVariable Long roomID) {
        try {
            Long memberID = 1L;
            roomServiceImpl.deletePlayerFromRoom(roomID, memberID);
            return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessage.EXIT_USER), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(DefaultResponse.res(StatusCode.BAD_REQUEST, ResponseMessage.EXIT_USER_FAIL), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 플레이 상태 변경
     */
    @PatchMapping("/{roomID}/status")
    public ResponseEntity updateRoomStatus(@PathVariable Long roomID, @RequestBody Room room) {
        try {
            roomServiceImpl.updateRoomStatus(roomID, room.getStatus());
            return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessage.UPDATE_ROOM_STATUS), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(DefaultResponse.res(StatusCode.BAD_REQUEST, ResponseMessage.UPDATE_ROOM_STATUS_FAIL), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 게임 방 생성
     */
    @PostMapping
    public ResponseEntity createRoom(@RequestBody RoomCreateRequest roomCreateRequest) {
        try {
            Long gmID = 1L;
            RoomResponse roomResponse = roomServiceImpl.createRoom(roomCreateRequest, gmID);
            return new ResponseEntity(DefaultResponse.res(StatusCode.CREATED, ResponseMessage.CREATE_ROOM, roomResponse), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(DefaultResponse.res(StatusCode.BAD_REQUEST, ResponseMessage.CREATE_ROOM_FAIL), HttpStatus.BAD_REQUEST);
        }  catch (Exception e) {
            return new ResponseEntity(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 게임 방 정보 조회
     */
    @GetMapping("/{roomID}")
    public ResponseEntity getRoomDetails(@PathVariable Long roomID) {
        try {
            RoomDetailsResponse roomDetails = roomServiceImpl.getRoomDetails(roomID);
            return ResponseEntity.ok(DefaultResponse.res(StatusCode.OK, ResponseMessage.READ_ROOM_INFO, roomDetails));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(DefaultResponse.res(StatusCode.BAD_REQUEST, ResponseMessage.READ_ROOM_INFO_FAIL));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR));
        }
    }

    /**
     * 게임 방 정보 수정
     */
    @PatchMapping("/{roomID}")
    public ResponseEntity updateRoom(@PathVariable Long roomID, @RequestBody RoomUpdateRequest roomUpdateRequest) {
        try {
            Long gmID = 1L;
            RoomUpdateResponse roomResponse = roomServiceImpl.updateRoom(roomID, gmID, roomUpdateRequest);
            return ResponseEntity.ok(DefaultResponse.res(StatusCode.OK, ResponseMessage.UPDATE_ROOM, roomResponse));
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("인증 권한 에러")) {
                return ResponseEntity.status(401).body(DefaultResponse.res(StatusCode.UNAUTHORIZED, ResponseMessage.UNAUTHORIZED));
            }
            return ResponseEntity.badRequest().body(DefaultResponse.res(StatusCode.BAD_REQUEST, ResponseMessage.UPDATE_ROOM_FAIL));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR));
        }
    }

    /**
     * 게임 방 목록 조회
     */
    @GetMapping
    public ResponseEntity getRoomList(@RequestParam(required = false) RoomStatus status,
                                      @RequestParam(required = false) String title,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "6") int size,
                                      PagedResourcesAssembler<RoomResponse> assembler) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
            Page<RoomResponse> rooms = roomServiceImpl.getRoomList(status, title, pageable);

            PagedModel<EntityModel<RoomResponse>> pagedModel = assembler.toModel(rooms, WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RoomController.class)
                    .getRoomList(status, title, page, size, assembler)).withSelfRel());

            return ResponseEntity.ok(DefaultResponse.res(StatusCode.OK, ResponseMessage.READ_ROOM_LIST, pagedModel));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(DefaultResponse.res(StatusCode.BAD_REQUEST, ResponseMessage.READ_ROOM_LIST_FAIL));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR));
        }
    }
}
