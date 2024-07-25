package org.infinity.sixtalebackend.domain.map.controller;

import lombok.AllArgsConstructor;
import org.infinity.sixtalebackend.domain.map.dto.*;
import org.infinity.sixtalebackend.domain.map.service.MapServiceImpl;
import org.infinity.sixtalebackend.global.common.response.DefaultResponse;
import org.infinity.sixtalebackend.global.common.response.ResponseMessage;
import org.infinity.sixtalebackend.global.common.response.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/rooms")
public class MapController {

    private final MapServiceImpl mapService;

    @GetMapping("/{roomID}/maps")
    public ResponseEntity getMapList(@PathVariable Long roomID) {
        try {
            MapListResponse response = mapService.getMapList(roomID);
            return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessage.READ_MAP_LIST, response), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(DefaultResponse.res(StatusCode.BAD_REQUEST, ResponseMessage.READ_MAP_LIST_FAIL), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{roomID}/maps/{mapID}")
    public ResponseEntity readMap(@PathVariable Long roomID, @PathVariable Long mapID) {
        try {
            MapResponse response = mapService.readMap(roomID, mapID);
            return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessage.READ_MAP, response), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(DefaultResponse.res(StatusCode.BAD_REQUEST, ResponseMessage.READ_MAP_FAIL), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{roomID}/maps/{mapID}/place")
    public ResponseEntity getPlaceEventList(@PathVariable Long roomID, @PathVariable Long mapID) {
        try {
            PlaceEventListResponse response = mapService.getPlaceEventList(roomID, mapID);
            return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessage.READ_PLACE_EVENT_LIST, response), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(DefaultResponse.res(StatusCode.BAD_REQUEST, ResponseMessage.READ_PLACE_EVENT_LIST_FAIL), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{roomID}/maps/{mapID}/npc")
    public ResponseEntity getNPCEventList(@PathVariable Long roomID, @PathVariable Long mapID) {
        try {
            NPCEventListResponse response = mapService.getNPCEventList(roomID, mapID);
            return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessage.READ_NPC_EVENT_LIST, response), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(DefaultResponse.res(StatusCode.BAD_REQUEST, ResponseMessage.READ_NPC_EVENT_LIST_FAIL), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
