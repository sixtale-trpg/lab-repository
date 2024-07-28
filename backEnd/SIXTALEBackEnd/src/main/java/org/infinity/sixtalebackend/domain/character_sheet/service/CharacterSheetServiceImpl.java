package org.infinity.sixtalebackend.domain.character_sheet.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.misc.LogManager;
import org.infinity.sixtalebackend.domain.character_sheet.domain.CharacterAction;
import org.infinity.sixtalebackend.domain.character_sheet.domain.CharacterEquipment;
import org.infinity.sixtalebackend.domain.character_sheet.domain.CharacterSheet;
import org.infinity.sixtalebackend.domain.character_sheet.domain.CharacterStat;
import org.infinity.sixtalebackend.domain.character_sheet.dto.CharacterSheetRequest;
import org.infinity.sixtalebackend.domain.character_sheet.repository.CharacterActionRepository;
import org.infinity.sixtalebackend.domain.character_sheet.repository.CharacterEquipmentRepository;
import org.infinity.sixtalebackend.domain.character_sheet.repository.CharacterSheetRepository;
import org.infinity.sixtalebackend.domain.character_sheet.repository.CharacterStatRepository;
import org.infinity.sixtalebackend.domain.room.domain.PlayMember;
import org.infinity.sixtalebackend.domain.room.repository.PlayMemberRepository;
import org.infinity.sixtalebackend.domain.rule.domain.Belief;
import org.infinity.sixtalebackend.domain.rule.domain.Job;
import org.infinity.sixtalebackend.domain.rule.domain.Race;
import org.infinity.sixtalebackend.domain.rule.repository.*;
import org.infinity.sixtalebackend.domain.scenario.domain.ScenarioEquipment;
import org.infinity.sixtalebackend.domain.scenario.repository.ScenarioEquipmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@AllArgsConstructor
public class CharacterServiceImpl implements CharacterSheetService{
    private final PlayMemberRepository playMemberRepository;
    private final CharacterSheetRepository characterSheetRepository;
    private final CharacterStatRepository characterStatRepository;
    private final CharacterActionRepository characterActionRepository;
    private final CharacterEquipmentRepository characterEquipmentRepository;
    private final JobRepository jobRepository;
    private final JobActionRepository jobActionRepository;
    private final ActionOptionRepository actionOptionRepository;
    private final BeliefRepository beliefRepository;
    private final RaceRepository raceRepository;
    private final StatRepository statRepository;
    private final ScenarioEquipmentRepository scenarioEquipmentRepository;

    /**
     * 캐릭터 시트 작성
     */
    @Override
    @Transactional
    public void createCharacterSheet(Long roomID, CharacterSheetRequest characterSheetRequest, Long memberID) {
        PlayMember playMember = playMemberRepository.findByMemberIdAndRoomId(memberID, roomID)
                .orElseThrow(() -> new IllegalArgumentException("Invalid PlayMember or Room ID"));

        Job job = jobRepository.findById(characterSheetRequest.getJobId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Job ID"));

        Belief belief = beliefRepository.findById(characterSheetRequest.getBeliefId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Belief ID"));

        Race race = raceRepository.findById(characterSheetRequest.getRaceId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Race ID"));

        // 캐릭터 시트 저장
        CharacterSheet characterSheet = CharacterSheet.builder()
                .playMember(playMember)
                .job(job)
                .belief(belief)
                .race(race)
                .name(characterSheetRequest.getName())
                .appearance(characterSheetRequest.getAppearance())
                .background(characterSheetRequest.getBackground())
                .currentWeight(characterSheetRequest.getCurrentWeight())
                .currentHp(characterSheetRequest.getCurrentHp())
                .currentMoney(characterSheetRequest.getCurrentMoney())
                .limitWeight(characterSheetRequest.getLimitWeight())
                .limitHp(characterSheetRequest.getLimitHp())
                .glove(characterSheetRequest.getGlove())
                .inspirationScore(characterSheetRequest.getInspirationScore())
                .level(characterSheetRequest.getLevel())
                .exp(characterSheetRequest.getExp())
                .imageURL(characterSheetRequest.getImageURL())
                .build();
        characterSheetRepository.save(characterSheet);

        //캐릭터 스탯 저장
        characterSheetRequest.getStat().forEach(statRequest -> {
            CharacterStat characterStat = CharacterStat.builder()
                    .playMember(playMember)
                    .stat(statRepository.findById(statRequest.getStatID())
                            .orElseThrow(() -> new IllegalArgumentException("Invalid Stat ID")))
                    .statValue(statRequest.getStatValue())
                    .statWeight(statRequest.getStatWeight())
                    .build();
            characterStatRepository.save(characterStat);
        });
        log.info("스탯 저장");

        // 캐릭터 액션 저장
        characterSheetRequest.getCharacterAction().forEach(actionRequest -> {
            CharacterAction characterAction = CharacterAction.builder()
                    .playMember(playMember)
                    .jobAction(jobActionRepository.findById(actionRequest.getActionID())
                            .orElseThrow(() -> new IllegalArgumentException("Invalid Action ID")))
                    .actionOption(actionOptionRepository.findById(actionRequest.getActionOptionId())
                            .orElse(null))
                    .build();
            characterActionRepository.save(characterAction);
        });
        log.info("액션 저장");

        // 캐릭터 장비 저장
        characterSheetRequest.getCharacterEquipment().forEach(equipmentRequest -> {
            CharacterEquipment characterEquipment = CharacterEquipment.builder()
                    .playMember(playMember)
                    .equipment(scenarioEquipmentRepository.findById(equipmentRequest.getEquipmentId())
                            .orElseThrow(() -> new IllegalArgumentException("Invalid Equipment ID")))
                    .currentCount(equipmentRequest.getCurrentCount())
                    .weight(equipmentRequest.getWeight())
                    .build();
            characterEquipmentRepository.save(characterEquipment);
        });
        log.info("장비 저장");
    }
}
