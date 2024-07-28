package org.infinity.sixtalebackend.domain.character_sheet.service;

import org.infinity.sixtalebackend.domain.character_sheet.dto.CharacterSheetRequest;

public interface CharacterSheetService {
    void createCharacterSheet(Long roomID, CharacterSheetRequest characterSheetRequest, Long memberID);
}
