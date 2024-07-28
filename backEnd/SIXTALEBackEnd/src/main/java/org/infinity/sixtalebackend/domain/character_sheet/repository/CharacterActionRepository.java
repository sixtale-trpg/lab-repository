package org.infinity.sixtalebackend.domain.character_sheet.repository;

import org.infinity.sixtalebackend.domain.character_sheet.domain.CharacterAction;
import org.infinity.sixtalebackend.domain.room.domain.PlayMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface CharacterActionRepository extends JpaRepository<CharacterAction, Long> {
    void deleteByPlayMember(PlayMember playMember);
}
