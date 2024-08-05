package org.infinity.sixtalebackend.domain.character_sheet.controller;

import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.infinity.sixtalebackend.domain.character_sheet.dto.CharacterEquipmentRequest;
import org.infinity.sixtalebackend.domain.character_sheet.dto.CharacterSheetEquipmentResponse;
import org.infinity.sixtalebackend.domain.character_sheet.dto.UpdateCharacterSheetResponse;
import org.infinity.sixtalebackend.domain.character_sheet.service.CharacterSheetEquipmentService;
import org.infinity.sixtalebackend.domain.character_sheet.service.CharacterSheetService;
import org.infinity.sixtalebackend.global.common.response.DefaultResponse;
import org.infinity.sixtalebackend.global.common.response.ResponseMessage;
import org.infinity.sixtalebackend.global.common.response.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("rooms/{roomID}/sheets/{playMemberID}/equipment")
@AllArgsConstructor
@Slf4j
public class CharacterSheetEquipmentController {
    private final CharacterSheetEquipmentService characterSheetEquipmentService;


    /**
     * 캐릭터 장비 목록 조회
     */
    @GetMapping
    public ResponseEntity getCharacterSheetEquipment(@PathVariable Long roomID, @PathVariable Long playMemberID) {
        try {
            CharacterSheetEquipmentResponse response = characterSheetEquipmentService.getCharacterSheetEquipment(roomID, playMemberID);
            return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessage.READ_CHARACTER_SHEET_EQUIPMENT, response), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return new ResponseEntity(DefaultResponse.res(StatusCode.BAD_REQUEST, ResponseMessage.READ_CHARACTER_SHEET_EQUIPMENT_FAIL), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 캐릭터 장비 추가
     */
    @PostMapping
    public ResponseEntity addCharacterSheetEquipment(@PathVariable Long roomID, @PathVariable Long playMemberID,
                                                     @RequestBody List<CharacterEquipmentRequest> equipmentRequests) {
        try {
            characterSheetEquipmentService.addCharacterEquipment(roomID, playMemberID, equipmentRequests);

            String createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            UpdateCharacterSheetResponse response = UpdateCharacterSheetResponse.builder()
                    .roomID(roomID)
                    .type("1")
                    .content("플레이어가 장비 추가를 완료했습니다")
                    .createdAt(createdAt)
                    .playMemberID(playMemberID)
                    .build();
            return new ResponseEntity(DefaultResponse.res(StatusCode.CREATED, ResponseMessage.ADD_CHARACTER_SHEET_EQUIPMENT, response), HttpStatus.CREATED);
        }catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return new ResponseEntity(DefaultResponse.res(StatusCode.BAD_REQUEST, ResponseMessage.ADD_CHARACTER_SHEET_EQUIPMENT_FAIL), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 특정 장비 삭제, 장비 테이블의 고유 ID이어야 함
     */
    @DeleteMapping("/{equipmentID}")
    public ResponseEntity deleteCharacterEquipment(@PathVariable Long roomID, @PathVariable Long playMemberID, @PathVariable Long equipmentID) {
        try {
            characterSheetEquipmentService.deleteCharacterEquipment(roomID, playMemberID, equipmentID);
            return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessage.DELETE_CHARACTER_SHEET_EQUIPMENT), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return new ResponseEntity(DefaultResponse.res(StatusCode.BAD_REQUEST, ResponseMessage.DELETE_CHARACTER_SHEET_EQUIPMENT_FAIL), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
