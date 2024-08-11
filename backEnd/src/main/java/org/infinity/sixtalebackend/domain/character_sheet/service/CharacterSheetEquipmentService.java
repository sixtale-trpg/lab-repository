package org.infinity.sixtalebackend.domain.character_sheet.service;

import org.infinity.sixtalebackend.domain.character_sheet.dto.CharacterEquipmentRequest;
import org.infinity.sixtalebackend.domain.character_sheet.dto.CharacterSheetEquipmentResponse;
import org.infinity.sixtalebackend.domain.character_sheet.dto.CharacterUpdateEquipmentRequest;

import java.util.List;

public interface CharacterSheetEquipmentService {
    CharacterSheetEquipmentResponse getCharacterSheetEquipment(Long roomID, Long playMemberID);
    void addCharacterEquipment(Long roomID, Long playMemberID, CharacterEquipmentRequest equipmentRequest);
    void deleteCharacterEquipment(Long roomID, Long playMemberID, Long equipmentID);

    void updateCharacterEquipment(Long roomID, Long playMemberID, CharacterUpdateEquipmentRequest equipmentUpdateRequest);
}
