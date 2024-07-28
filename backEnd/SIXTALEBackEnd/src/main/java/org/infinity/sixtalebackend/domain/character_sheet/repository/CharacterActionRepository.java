package org.infinity.sixtalebackend.domain.character_sheet.service;

import org.infinity.sixtalebackend.domain.character_sheet.domain.CharacterAction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterActionRepository extends JpaRepository<CharacterAction, Long> {
}
