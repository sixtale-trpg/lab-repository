package org.infinity.sixtalebackend.domain.character_sheet.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.infinity.sixtalebackend.domain.character_sheet.dto.CharacterSheetRequest;
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
    @PostMapping
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
    public ResponseEntity updateCharacterSheet(@PathVariable Long roomID, @PathVariable Long playMemberID, @RequestBody @Valid CharacterSheetRequest characterSheetRequest) {
        try {
            // memberID = 1L 가정
//            Long memberID = 1L;
            characterSheetService.updateCharacterSheet(roomID, playMemberID, characterSheetRequest);

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
}
