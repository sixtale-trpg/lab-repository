package org.infinity.sixtalebackend.domain.character_sheet.repository;

import org.infinity.sixtalebackend.domain.character_sheet.domain.CharacterSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface CharacterSheetRepository extends JpaRepository<CharacterSheet, Long> {

    @Query("SELECT cs FROM CharacterSheet cs " +
            "JOIN FETCH cs.job j " +
            "JOIN FETCH cs.race r " +
            "JOIN FETCH cs.belief b " +
            "LEFT JOIN FETCH JobBelief jb ON jb.job = j AND jb.belief = b " +
            "LEFT JOIN FETCH JobRace jr ON jr.job = j AND jr.race = r " +
            "WHERE cs.id = :playMemberID")
    CharacterSheet findByPlayMemberIdWithFetch(@Param("playMemberID") Long playMemberID);

}
