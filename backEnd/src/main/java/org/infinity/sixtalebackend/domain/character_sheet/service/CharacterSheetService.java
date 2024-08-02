package org.infinity.sixtalebackend.domain.character_sheet.service;

import org.infinity.sixtalebackend.domain.character_sheet.dto.CharacterSheetRequest;
import org.infinity.sixtalebackend.domain.character_sheet.dto.CharacterSheetResponse;

public interface CharacterSheetService {
    void createCharacterSheet(Long roomID, CharacterSheetRequest characterSheetRequest, Long memberID);
    void updateCharacterSheet(Long roomID, Long playMemberID, CharacterSheetRequest characterSheetRequest);
    CharacterSheetResponse getCharacterSheet(Long roomID, Long playMemberID);
    void deleteCharacterSheet(Long roomID, Long playMemberID);

}
