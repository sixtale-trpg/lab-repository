package org.infinity.sixtalebackend.domain.character_sheet.repository;

import org.infinity.sixtalebackend.domain.character_sheet.domain.CharacterSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface CharacterSheetRepository extends JpaRepository<CharacterSheet, Long> {
/*
    @Query("SELECT cs, jb.description as beliefDescription, jr.description as raceDescription, " +
            "st, eq, act, ja, ao, e, et " +
            "FROM CharacterSheet cs " +
            "JOIN FETCH cs.job j " +
            "JOIN FETCH cs.race r " +
            "JOIN FETCH cs.belief b " +
            "LEFT JOIN JobBelief jb ON jb.job = j AND jb.belief = b " +
            "LEFT JOIN JobRace jr ON jr.job = j AND jr.race = r " +
            "LEFT JOIN cs.playMember pm " +
            "LEFT JOIN CharacterStat st ON st.playMember = pm " +
            "LEFT JOIN CharacterEquipment eq ON eq.playMember = pm " +
            "LEFT JOIN eq.equipment e " +
            "LEFT JOIN e.equipmentType et " +
            "LEFT JOIN CharacterAction act ON act.playMember = pm " +
            "LEFT JOIN act.jobAction ja " +
            "LEFT JOIN act.actionOption ao " +
            "WHERE cs.id = :playMemberID")
    List<Object[]> findByPlayMemberIdWithFetch(@Param("playMemberID") Long playMemberID);*/

 /*   @Query("SELECT DISTINCT cs " +
            "FROM CharacterSheet cs " +
            "JOIN FETCH cs.job j " +
            "JOIN FETCH cs.race r " +
            "JOIN FETCH cs.belief b " +
            "LEFT JOIN FETCH j.jobBeliefs jb " +
            "LEFT JOIN FETCH j.jobRaces jr " +
            "LEFT JOIN FETCH cs.characterStats " +
            "LEFT JOIN FETCH cs.characterEquipments ce " +
            "LEFT JOIN FETCH ce.equipment e " +
            "LEFT JOIN FETCH e.equipmentType " +
            "LEFT JOIN FETCH cs.characterActions ca " +
            "LEFT JOIN FETCH ca.jobAction " +
            "LEFT JOIN FETCH ca.actionOption " +
            "WHERE cs.id = :playMemberID")
    Optional<CharacterSheet> findByPlayMemberIdWithFetch(@Param("playMemberID") Long playMemberID);*/

}
