package org.infinity.sixtalebackend.domain.character_sheet.service;

import org.infinity.sixtalebackend.domain.character_sheet.dto.CharacterActionListResponse;

public interface CharacterActionService {
    CharacterActionListResponse getCharacterActions(Long roomID, Long playMemberID);
}
