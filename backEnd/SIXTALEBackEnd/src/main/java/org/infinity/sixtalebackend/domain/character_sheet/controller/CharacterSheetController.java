package org.infinity.sixtalebackend.domain.character_sheet.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.infinity.sixtalebackend.domain.character_sheet.dto.CharacterSheetRequest;
import org.infinity.sixtalebackend.domain.character_sheet.service.CharacterSheetService;
import org.infinity.sixtalebackend.global.common.response.DefaultResponse;
import org.infinity.sixtalebackend.global.common.response.ResponseMessage;
import org.infinity.sixtalebackend.global.common.response.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
