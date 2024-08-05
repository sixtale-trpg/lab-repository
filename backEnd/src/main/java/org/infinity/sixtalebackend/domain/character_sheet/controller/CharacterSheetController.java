package org.infinity.sixtalebackend.domain.character_sheet.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.infinity.sixtalebackend.domain.character_sheet.dto.CharacterSheetRequest;
import org.infinity.sixtalebackend.domain.character_sheet.dto.CharacterSheetResponse;
import org.infinity.sixtalebackend.domain.character_sheet.dto.CharacterSheetUpdateRequest;
import org.infinity.sixtalebackend.domain.character_sheet.dto.UpdateCharacterSheetResponse;
import org.infinity.sixtalebackend.domain.character_sheet.service.CharacterSheetService;
import org.infinity.sixtalebackend.global.common.response.DefaultResponse;
import org.infinity.sixtalebackend.global.common.response.ResponseMessage;
import org.infinity.sixtalebackend.global.common.response.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/rooms/{roomID}/sheets")
@Slf4j
@AllArgsConstructor
public class CharacterSheetController {
    private final CharacterSheetService characterSheetService;

    /**
     * 캐릭터 시트 작성
     */
    @PatchMapping
    public ResponseEntity createCharacterSheet(@PathVariable Long roomID, @RequestBody @Valid CharacterSheetRequest characterSheetRequest) {
        try {
            //memberID = 1L 가정
            Long memberID = 1L;
            characterSheetService.createCharacterSheet(roomID, characterSheetRequest, memberID);
            return new ResponseEntity(DefaultResponse.res(StatusCode.CREATED, ResponseMessage.CREATE_CHARACTER_SHEET), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return new ResponseEntity(DefaultResponse.res(StatusCode.BAD_REQUEST, ResponseMessage.CREATE_CHARACTER_SHEET_FAIL), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 캐릭터 시트 수정
     */
    @PutMapping("/{playMemberID}")
    public ResponseEntity updateCharacterSheet(@PathVariable Long roomID, @PathVariable Long playMemberID, @RequestBody @Valid CharacterSheetUpdateRequest characterSheetUpdateRequest) {
        try {
            // memberID = 1L 가정
//            Long memberID = 1L;
            characterSheetService.updateCharacterSheet(roomID, playMemberID, characterSheetUpdateRequest);

            // 현재 시각 설정
            String createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            // 성공 시 응답 데이터 생성
            UpdateCharacterSheetResponse response = UpdateCharacterSheetResponse.builder()
                    .roomID(roomID)
                    .type("캐릭터 시트 수정")
                    .content("플레이어가 캐릭터 시트 수정을 완료했습니다.")
                    .createdAt(createdAt)
                    .playMemberID(playMemberID)
                    .build();
            return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessage.UPDATE_CHARACTER_SHEET, response), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return new ResponseEntity(DefaultResponse.res(StatusCode.BAD_REQUEST, ResponseMessage.UPDATE_CHARACTER_SHEET_FAIL), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 캐릭터 시트 조회
     */
    @GetMapping("/{playMemberID}")
    public ResponseEntity getCharacterSheet(@PathVariable Long roomID, @PathVariable Long playMemberID) {
        try {
            CharacterSheetResponse response = characterSheetService.getCharacterSheet(roomID, playMemberID);
            return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessage.READ_CHARACTER_SHEET, response), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return new ResponseEntity(DefaultResponse.res(StatusCode.BAD_REQUEST, ResponseMessage.READ_CHARACTER_SHEET_FAIL), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 캐릭터 시트 작성 취소(삭제)
     */
    @DeleteMapping("/{playMemberID}")
    public ResponseEntity deleteCharacterSheet(@PathVariable Long roomID, @PathVariable Long playMemberID) {
        try {
            characterSheetService.deleteCharacterSheet(roomID, playMemberID);
            return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessage.DELETE_CHARACTER_SHEET), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return new ResponseEntity(DefaultResponse.res(StatusCode.BAD_REQUEST, ResponseMessage.DELETE_CHARACTER_SHEET_FAIL), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
