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
import org.infinity.sixtalebackend.domain.equipment.domain.EquipmentType;
import org.infinity.sixtalebackend.domain.room.domain.PlayMember;
import org.infinity.sixtalebackend.domain.room.repository.PlayMemberRepository;
import org.infinity.sixtalebackend.domain.rule.domain.*;
import org.infinity.sixtalebackend.domain.rule.repository.*;
import org.infinity.sixtalebackend.domain.scenario.domain.ScenarioEquipment;
import org.infinity.sixtalebackend.domain.scenario.repository.ScenarioEquipmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
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
     * 무게 합 프론트에서 보내줄 것임
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
        List<CharacterStat> characterStats = characterSheetRequest.getStat().stream()
                .map(statRequest -> CharacterStat.builder()
                        .playMember(playMember)
                        .characterSheet(characterSheet)
                        .stat(statRepository.findById(statRequest.getStatID())
                                .orElseThrow(() -> new IllegalArgumentException("Invalid Stat ID")))
                        .statValue(statRequest.getStatValue())
                        .statWeight(statRequest.getStatWeight())
                        .build())
                .collect(Collectors.toList());
        characterStatRepository.saveAll(characterStats);

        log.info("스탯 저장");

        // 캐릭터 액션 저장
        List<CharacterAction> characterActions = characterSheetRequest.getCharacterAction().stream()
                .map(actionRequest -> CharacterAction.builder()
                        .characterSheet(characterSheet)
                        .playMember(playMember)
                        .jobAction(jobActionRepository.findById(actionRequest.getActionID())
                                .orElseThrow(() -> new IllegalArgumentException("Invalid Action ID")))
                        .actionOption(actionRequest.getActionOptionId() != null ?
                                actionOptionRepository.findById(actionRequest.getActionOptionId()).orElse(null) :
                                null)
                        .build())
                .collect(Collectors.toList());
        characterActionRepository.saveAll(characterActions);
        log.info("액션 저장");

        // 캐릭터 장비 저장
        List<CharacterEquipment> characterEquipments = characterSheetRequest.getCharacterEquipment().stream()
                .map(equipmentRequest -> CharacterEquipment.builder()
                        .playMember(playMember)
                        .characterSheet(characterSheet) // PlayMember 대신 CharacterSheet 사용
                        .equipment(scenarioEquipmentRepository.findById(equipmentRequest.getEquipmentId())
                                .orElseThrow(() -> new IllegalArgumentException("Invalid Equipment ID")))
                        .currentCount(equipmentRequest.getCurrentCount())
                        .weight(equipmentRequest.getWeight())
                        .build())
                .collect(Collectors.toList());

        characterEquipmentRepository.saveAll(characterEquipments);
        log.info("장비 저장");
    }

    /**
     * 캐릭터 시트 수정
     */
    @Override
    @Transactional
    public void updateCharacterSheet(Long roomID, Long playMemberID, CharacterSheetUpdateRequest characterSheetUpdateRequest) {
        PlayMember playMember = playMemberRepository.findById(playMemberID)
                .orElseThrow(() -> new IllegalArgumentException("Invalid PlayMember"));

        CharacterSheet characterSheet = characterSheetRepository.findByPlayMemberId(playMemberID)
                .orElseThrow(() -> new IllegalArgumentException("Character Sheet not found"));

        Job job = jobRepository.findById(characterSheetUpdateRequest.getJobId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Job ID"));

        Belief belief = beliefRepository.findById(characterSheetUpdateRequest.getBeliefId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Belief ID"));

        Race race = raceRepository.findById(characterSheetUpdateRequest.getRaceId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Race ID"));

        // 캐릭터 시트 업데이트
        characterSheet.setPlayMember(playMember);
        characterSheet.setJob(job);
        characterSheet.setBelief(belief);
        characterSheet.setRace(race);
        characterSheet.setName(characterSheetUpdateRequest.getName());
        characterSheet.setAppearance(characterSheetUpdateRequest.getAppearance());
        characterSheet.setBackground(characterSheetUpdateRequest.getBackground());
        characterSheet.setCurrentWeight(characterSheetUpdateRequest.getCurrentWeight());
        characterSheet.setCurrentHp(characterSheetUpdateRequest.getCurrentHp());
        characterSheet.setCurrentMoney(characterSheetUpdateRequest.getCurrentMoney());
        characterSheet.setLimitWeight(characterSheetUpdateRequest.getLimitWeight());
        characterSheet.setLimitHp(characterSheetUpdateRequest.getLimitHp());
        characterSheet.setGlove(characterSheetUpdateRequest.getGlove());
        characterSheet.setInspirationScore(characterSheetUpdateRequest.getInspirationScore());
        characterSheet.setLevel(characterSheetUpdateRequest.getLevel());
        characterSheet.setExp(characterSheetUpdateRequest.getExp());
        characterSheet.setImageURL(characterSheetUpdateRequest.getImageURL());
        characterSheetRepository.save(characterSheet);

        // 캐릭터 스탯 업데이트
        Map<Long, CharacterStatRequest> statRequestMap = characterSheetUpdateRequest.getStat().stream()
                .collect(Collectors.toMap(CharacterStatRequest::getStatID, Function.identity()));

        characterSheet.getCharacterStats().forEach(stat -> {
            CharacterStatRequest statRequest = statRequestMap.get(stat.getStat().getId());
            if (statRequest != null) {
                stat.setStatValue(statRequest.getStatValue());
                stat.setStatWeight(statRequest.getStatWeight());
            }
        });
    }

    /**
     * 캐릭터 시트 조회
     */
    @Override
    @Transactional(readOnly = true)
    public CharacterSheetResponse getCharacterSheet(Long roomID, Long playMemberID) {
        CharacterSheet characterSheet = characterSheetRepository.findByPlayMemberIdWithFetch(playMemberID)
                .orElseThrow(() -> new IllegalArgumentException("Character Sheet not found"));

        Job job = characterSheet.getJob();
        Race race = characterSheet.getRace();
        Belief belief = characterSheet.getBelief();

        String raceDescription = job.getJobRaces().stream()
                .filter(jr -> jr.getRace().equals(race))
                .map(JobRace::getDescription)
                .findFirst()
                .orElse("");
        String beliefDescription = job.getJobBeliefs().stream()
                .filter(jb -> jb.getBelief().equals(belief))
                .map(JobBelief::getDescription)
                .findFirst()
                .orElse("");

        // stat, actions, equipments 뽑아내기
        Set<CharacterStat> stats = characterSheet.getCharacterStats();
        Set<CharacterAction> actions = characterSheet.getCharacterActions();
        Set<CharacterEquipment> equipments = characterSheet.getCharacterEquipments();

        //Dto로 변환
        List<CharacterStatResponse> statResponses = stats.stream()
                .map(stat -> CharacterStatResponse.builder()
                        .statID(stat.getStat().getId())
                        .statValue(stat.getStatValue())
                        .statWeight(stat.getStatWeight())
                        .build())
                .collect(Collectors.toList());

        List<CharacterActionResponse> actionResponses = actions.stream()
                .map(action -> {
                    JobAction jobAction = action.getJobAction();
                    ActionOption actionOption = action.getActionOption();
                    return CharacterActionResponse.builder()
                            .id(action.getId())
                            .actionID(jobAction.getId())
                            .name(jobAction.getName())
                            .isCore(jobAction.getIsCore())
                            .description(jobAction.getDescription())
                            .isDice(jobAction.getIsDice())
                            .diceType(jobAction.getDiceType())
                            .diceCount(jobAction.getDiceCount())
                            .level(jobAction.getLevel())
                            .actionOption(actionOption != null ?
                                    Collections.singletonList(ActionOptionResponse.builder()
                                            .id(actionOption.getId())
                                            .content(actionOption.getContent())
                                            .build())
                                    : Collections.emptyList())
                            .build();
                })
                .collect(Collectors.toList());

        List<CharacterEquipmentResponse> equipmentResponses = equipments.stream()
                .map(equipment -> {
                    ScenarioEquipment scenarioEquipment = equipment.getEquipment();
                    EquipmentType equipmentType = scenarioEquipment.getEquipmentType();
                    return CharacterEquipmentResponse.builder()
                            .id(equipment.getId())
                            .equipmentID(scenarioEquipment.getId())
                            .name(scenarioEquipment.getName())
                            .description(scenarioEquipment.getDescription())
                            .typeID(equipmentType.getId())
                            .typeName(equipmentType.getName())
                            .weight(equipment.getWeight())
                            .currentCount(equipment.getCurrentCount())
                            .imageURL(scenarioEquipment.getImageURL())
                            .build();
                })
                .collect(Collectors.toList());

        return CharacterSheetResponse.builder()
                .jobId(job.getId())
                .jobName(job.getName())
                .jobDiceType(job.getDiceType())
                .raceId(race.getId())
                .raceName(race.getName())
                .raceDescription(raceDescription) // Fetch from jobRace field
                .beliefId(belief.getId())
                .beliefName(belief.getName())
                .beliefDescription(beliefDescription) // Fetch from jobBelief field
                .name(characterSheet.getName())
                .appearance(characterSheet.getAppearance())
                .background(characterSheet.getBackground())
                .stat(statResponses)
                .characterAction(actionResponses)
                .characterEquipment(equipmentResponses)
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

        CharacterSheet characterSheet = characterSheetRepository.findByPlayMember(playMember)
                .orElseThrow(() -> new IllegalArgumentException("Character Sheet not found"));

        // CharacterStat 삭제하면 캐스케이드 옵션 사용에 의해 전부 삭제
        characterSheetRepository.delete(characterSheet);
        log.info("캐릭터 시트 및 관련 데이터 삭제 완료");
    }
}
