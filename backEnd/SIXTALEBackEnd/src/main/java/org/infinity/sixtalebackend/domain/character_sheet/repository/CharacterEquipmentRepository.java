package org.infinity.sixtalebackend.domain.character_sheet.repository;

import org.infinity.sixtalebackend.domain.character_sheet.domain.CharacterEquipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface CharacterEquipmentRepository extends JpaRepository<CharacterEquipment, Long> {
}
