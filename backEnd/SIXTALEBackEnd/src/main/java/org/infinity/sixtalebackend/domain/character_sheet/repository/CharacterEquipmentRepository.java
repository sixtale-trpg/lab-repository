package org.infinity.sixtalebackend.domain.character_sheet.service;

import org.infinity.sixtalebackend.domain.character_sheet.domain.CharacterEquipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterEquipmentRepository extends JpaRepository<CharacterEquipment, Long> {
}
