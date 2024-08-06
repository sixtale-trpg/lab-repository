package org.infinity.sixtalebackend.domain.character_sheet.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.infinity.sixtalebackend.domain.character_sheet.dto.CharacterActionListResponse;
import org.infinity.sixtalebackend.domain.character_sheet.service.CharacterActionService;
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
@RequestMapping("/rooms/{roomID}/sheets/{playMemberID}/actions")
@Slf4j
@AllArgsConstructor
public class CharacterActionController {

    private final CharacterActionService characterActionService;

    /**
     * 캐릭터 액션 목록 조회
     */
    /*@GetMapping
    public ResponseEntity<CharacterActionListResponse> getCharacterActions(@PathVariable Long roomID,
                                                                           @PathVariable Long playMemberID) {
        CharacterActionListResponse response = characterActionService.getCharacterActions(roomID, playMemberID);
        return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessage.READ_CHARACTER_SHEET_ACTION, response), HttpStatus.OK);
    }*/


}
