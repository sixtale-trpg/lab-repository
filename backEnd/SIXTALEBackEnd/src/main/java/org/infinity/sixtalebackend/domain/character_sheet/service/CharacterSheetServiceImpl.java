package org.infinity.sixtalebackend.domain.character_sheet.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.infinity.sixtalebackend.domain.character_sheet.domain.CharacterAction;
import org.infinity.sixtalebackend.domain.character_sheet.domain.CharacterEquipment;
import org.infinity.sixtalebackend.domain.character_sheet.domain.CharacterSheet;
import org.infinity.sixtalebackend.domain.character_sheet.domain.CharacterStat;
import org.infinity.sixtalebackend.domain.character_sheet.dto.*;
import org.infinity.sixtalebackend.domain.character_sheet.repository.CharacterActionRepository;
import org.infinity.sixtalebackend.domain.character_sheet.repository.CharacterEquipmentRepository;
import org.infinity.sixtalebackend.domain.character_sheet.repository.CharacterSheetRepository;
import org.infinity.sixtalebackend.domain.character_sheet.repository.CharacterStatRepository;
import org.infinity.sixtalebackend.domain.room.domain.PlayMember;
import org.infinity.sixtalebackend.domain.room.repository.PlayMemberRepository;
import org.infinity.sixtalebackend.domain.rule.domain.*;
import org.infinity.sixtalebackend.domain.rule.repository.*;
import org.infinity.sixtalebackend.domain.scenario.repository.ScenarioEquipmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class CharacterSheetServiceImpl implements CharacterSheetService{
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
    private final JobRaceRepository jobRaceRepository;
    private final JobBeliefRepository jobBeliefRepository;

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
                    .actionOption(actionRequest.getActionOptionId() != null ?
                            actionOptionRepository.findById(actionRequest.getActionOptionId()).orElse(null) :
                            null)
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

    /**
     * 캐릭터 시트 수정
     */
    @Override
    @Transactional
    public void updateCharacterSheet(Long roomID, Long playMemberID, CharacterSheetRequest characterSheetRequest) {
        PlayMember playMember = playMemberRepository.findById(playMemberID)
                .orElseThrow(() -> new IllegalArgumentException("Invalid PlayMember"));

        CharacterSheet characterSheet = characterSheetRepository.findById(playMemberID)
                .orElseThrow(() -> new IllegalArgumentException("Character Sheet not found"));

        Job job = jobRepository.findById(characterSheetRequest.getJobId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Job ID"));

        Belief belief = beliefRepository.findById(characterSheetRequest.getBeliefId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Belief ID"));

        Race race = raceRepository.findById(characterSheetRequest.getRaceId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Race ID"));

        // 캐릭터 시트 업데이트
        characterSheet.setJob(job);
        characterSheet.setBelief(belief);
        characterSheet.setRace(race);
        characterSheet.setName(characterSheetRequest.getName());
        characterSheet.setAppearance(characterSheetRequest.getAppearance());
        characterSheet.setBackground(characterSheetRequest.getBackground());
        characterSheet.setCurrentWeight(characterSheetRequest.getCurrentWeight());
        characterSheet.setCurrentHp(characterSheetRequest.getCurrentHp());
        characterSheet.setCurrentMoney(characterSheetRequest.getCurrentMoney());
        characterSheet.setLimitWeight(characterSheetRequest.getLimitWeight());
        characterSheet.setLimitHp(characterSheetRequest.getLimitHp());
        characterSheet.setGlove(characterSheetRequest.getGlove());
        characterSheet.setInspirationScore(characterSheetRequest.getInspirationScore());
        characterSheet.setLevel(characterSheetRequest.getLevel());
        characterSheet.setExp(characterSheetRequest.getExp());
        characterSheet.setImageURL(characterSheetRequest.getImageURL());
        characterSheetRepository.save(characterSheet);

        // 캐릭터 스탯 업데이트
        characterStatRepository.deleteByPlayMember(playMember);
        for (CharacterStatRequest statRequest : characterSheetRequest.getStat()) {
            CharacterStat characterStat = CharacterStat.builder()
                    .playMember(playMember)
                    .stat(statRepository.findById(statRequest.getStatID())
                            .orElseThrow(() -> new IllegalArgumentException("Invalid Stat ID")))
                    .statValue(statRequest.getStatValue())
                    .statWeight(statRequest.getStatWeight())
                    .build();
            characterStatRepository.save(characterStat);
        }
        log.info("스탯 업데이트 완료");

        // 캐릭터 액션 업데이트
        characterActionRepository.deleteByPlayMember(playMember);
        for (CharacterActionRequest actionRequest : characterSheetRequest.getCharacterAction()) {
            CharacterAction characterAction = CharacterAction.builder()
                    .playMember(playMember)
                    .jobAction(jobActionRepository.findById(actionRequest.getActionID())
                            .orElseThrow(() -> new IllegalArgumentException("Invalid Action ID")))
                    .actionOption(actionRequest.getActionOptionId() != null ?
                            actionOptionRepository.findById(actionRequest.getActionOptionId()).orElse(null) :
                            null)
                    .build();
            characterActionRepository.save(characterAction);
        }
        log.info("액션 업데이트 완료");

        // 캐릭터 장비 업데이트
        List<CharacterEquipment> existingEquipments = characterEquipmentRepository.findByPlayMember(playMember);
        List<Long> newEquipmentIds = characterSheetRequest.getCharacterEquipment().stream()
                .map(CharacterEquipmentRequest::getEquipmentId)
                .collect(Collectors.toList());
        // 삭제할 장비
        List<CharacterEquipment> equipmentsToDelete = existingEquipments.stream()
                .filter(e -> !newEquipmentIds.contains(e.getEquipment().getId()))
                .collect(Collectors.toList());
        characterEquipmentRepository.deleteAll(equipmentsToDelete);

        // 업데이트 또는 추가할 장비
        for (CharacterEquipmentRequest equipmentRequest : characterSheetRequest.getCharacterEquipment()) {
            CharacterEquipment existingEquipment = existingEquipments.stream()
                    .filter(e -> e.getEquipment().getId().equals(equipmentRequest.getEquipmentId()))
                    .findFirst()
                    .orElse(null);

            if (existingEquipment != null) {
                existingEquipment.setCurrentCount(equipmentRequest.getCurrentCount());
                existingEquipment.setWeight(equipmentRequest.getWeight());
                characterEquipmentRepository.save(existingEquipment);
            } else {
                CharacterEquipment newEquipment = CharacterEquipment.builder()
                        .playMember(playMember)
                        .equipment(scenarioEquipmentRepository.findById(equipmentRequest.getEquipmentId())
                                .orElseThrow(() -> new IllegalArgumentException("Invalid Equipment ID")))
                        .currentCount(equipmentRequest.getCurrentCount())
                        .weight(equipmentRequest.getWeight())
                        .build();
                characterEquipmentRepository.save(newEquipment);
            }
        }
        log.info("장비 업데이트 완료");
    }

    /**
     * 캐릭터 시트 조회
     */
    @Override
    @Transactional(readOnly = true)
    public CharacterSheetResponse getCharacterSheet(Long roomID, Long playMemberID) {
        PlayMember playMember = playMemberRepository.findById(playMemberID)
                .orElseThrow(() -> new IllegalArgumentException("Invalid PlayMember or Room ID"));
        CharacterSheet characterSheet = characterSheetRepository.findById(playMemberID)
                .orElseThrow(() -> new IllegalArgumentException("Character Sheet not found"));

        Job job = characterSheet.getJob();
        Race race = characterSheet.getRace();
        Belief belief = characterSheet.getBelief();

        // Get the descriptions from JobRace and JobBelief
        String raceDescription = jobRaceRepository.findByJobAndRace(job, race)
                .map(JobRace::getDescription)
                .orElse("");
        String beliefDescription = jobBeliefRepository.findByJobAndBelief(job, belief)
                .map(JobBelief::getDescription)
                .orElse("");


        List<CharacterStat> stats = characterStatRepository.findByPlayMember(playMember);
        List<CharacterAction> actions = characterActionRepository.findByPlayMember(playMember);
        List<CharacterEquipment> equipments = characterEquipmentRepository.findByPlayMember(playMember);

        return CharacterSheetResponse.builder()
                .jobId(job.getId())
                .jobName(job.getName())
                .raceId(race.getId())
                .raceName(race.getName())
                .raceDescription(raceDescription) // 다시 jobRace필드에서?
                .beliefId(belief.getId())
                .beliefName(belief.getName())
                .beliefDescription(beliefDescription) //
                .name(characterSheet.getName())
                .appearance(characterSheet.getAppearance())
                .background(characterSheet.getBackground())
                .stat(stats.stream().map(stat -> CharacterStatResponse.builder()
                        .statID(stat.getStat().getId())
                        .statValue(stat.getStatValue())
                        .statWeight(stat.getStatWeight())
                        .build()).collect(Collectors.toList()))
                .characterAction(actions.stream().map(action -> CharacterActionResponse.builder()
                        .id(action.getJobAction().getId())
                        .name(action.getJobAction().getName())
                        .isCore(action.getJobAction().getIsCore())
                        .description(action.getJobAction().getDescription())
                        .isDice(action.getJobAction().getIsDice())
                        .diceType(action.getJobAction().getDiceType())
                        .diceCount(action.getJobAction().getDiceCount())
                        .level(action.getJobAction().getLevel())
                        .actionOption(action.getActionOption() != null ?
                                Collections.singletonList(ActionOptionResponse.builder()
                                        .id(action.getActionOption().getId())
                                        .content(action.getActionOption().getContent())
                                        .build())
                                : Collections.emptyList())
                        .build()).collect(Collectors.toList()))
                .characterEquipment(equipments.stream().map(equipment -> CharacterEquipmentResponse.builder()
                        .id(equipment.getEquipment().getId())
                        .name(equipment.getEquipment().getName())
                        .description(equipment.getEquipment().getDescription())
                        .typeID(equipment.getEquipment().getEquipmentType().getId()) //
                        .typeName(equipment.getEquipment().getEquipmentType().getName()) //
                        .weight(equipment.getWeight())
                        .currentCount(equipment.getCurrentCount())
                        .imageURL(equipment.getEquipment().getImageURL())
                        .build()).collect(Collectors.toList()))
                .currentWeight(characterSheet.getCurrentWeight())
                .currentHp(characterSheet.getCurrentHp())
                .currentMoney(characterSheet.getCurrentMoney())
                .limitWeight(characterSheet.getLimitWeight())
                .limitHp(characterSheet.getLimitHp())
                .glove(characterSheet.getGlove())
                .inspirationScore(characterSheet.getInspirationScore())
                .level(characterSheet.getLevel())
                .exp(characterSheet.getExp())
                .imageURL(characterSheet.getImageURL())
                .build();
    }

    /**
     * 캐릭터 시트 작성 취소
     */
    @Override
    @Transactional
    public void deleteCharacterSheet(Long roomID, Long playMemberID) {
        PlayMember playMember = playMemberRepository.findByIdAndRoomId(playMemberID, roomID)
                .orElseThrow(() -> new IllegalArgumentException("Invalid PlayMember or Room ID"));

        // CharacterStat 삭제
        characterStatRepository.deleteByPlayMember(playMember);

        // CharacterAction 삭제
        characterActionRepository.deleteByPlayMember(playMember);

        // CharacterEquipment 삭제
        characterEquipmentRepository.deleteByPlayMember(playMember);

        // CharacterSheet 삭제
        characterSheetRepository.deleteById(playMemberID);

        log.info("캐릭터 시트 및 관련 데이터 삭제 완료");
    }
}
