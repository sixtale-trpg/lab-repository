package org.infinity.sixtalebackend.domain.rule.service;

import lombok.AllArgsConstructor;
import org.infinity.sixtalebackend.domain.equipment.domain.Equipment;
import org.infinity.sixtalebackend.domain.equipment.dto.EquipmentResponse;
import org.infinity.sixtalebackend.domain.equipment.repository.EquipmentRepository;
import org.infinity.sixtalebackend.domain.rule.domain.*;
import org.infinity.sixtalebackend.domain.rule.dto.*;
import org.infinity.sixtalebackend.domain.rule.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RuleServiceImpl implements RuleService {

    private final RuleRepository ruleRepository;
    private final JobRepository jobRepository;
    private final JobRaceRepository jobRaceRepository;
    private final JobBeliefRepository jobBeliefRepository;
    private final JobActionRepository jobActionRepository;
    private final ActionOptionRepository actionOptionRepository;
    private final EquipmentRepository equipmentRepository;
    private final CommonActionRepository commonActionRepository;

    @Override
    public JobListResponse readJobList(Long ruleID) {
        Rule rule = ruleRepository.findById(ruleID).get();
        List<Job> jobs = jobRepository.findByRule(rule);

        JobListResponse response = new JobListResponse();
        response.setJobList(jobs);
        return response;
    }

    @Override
    public JobOptionListResponse readJobOptionList(Long ruleID, Long jobID) {
        Rule rule = ruleRepository.findById(ruleID).get();
        Job job = jobRepository.findById(jobID).get();
        // 직업 액션
        List<JobAction> jobActionList = jobActionRepository.findByRule(rule);
        List<JobActionResponse> jobActions = jobActionList.stream()
                .map(m -> JobActionResponse.builder()
                        .id(m.getId())
                        .name(m.getName())
                        .isCore(m.getIsCore())
                        .description(m.getDescription())
                        .isDice(m.getIsDice())
                        .diceType(m.getDiceType())
                        .diceCount(m.getDiceCount())
                        .level(m.getLevel())
                        .actionOptions(m.getActionOptions().stream() // 액션 옵션
                                .map(e -> ActionOptionResponse.builder()
                                        .id(e.getId())
                                        .content(e.getContent())
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
        // 종족
        List<JobRace> jobRaceList = jobRaceRepository.findByJob(job);
        List<JobRaceResponse> jobRaces = jobRaceList.stream()
                .map(m -> JobRaceResponse.builder()
                        .raceID(m.getRace().getId())
                        .raceName(m.getRace().getName())
                        .description(m.getDescription())
                        .build())
                .collect(Collectors.toList());
        // 가치관
        List<JobBelief> jobBeliefList = jobBeliefRepository.findByJob(job);
        List<JobBeliefResponse> jobBeliefs = jobBeliefList.stream()
                .map(m -> JobBeliefResponse.builder()
                        .beliefID(m.getBelief().getId())
                        .beliefName(m.getBelief().getName())
                        .description(m.getDescription())
                        .build())
                .collect(Collectors.toList());
        // 장비
        List<Equipment> equipmentList = equipmentRepository.findByJob(job);
        List<EquipmentResponse> equipments = equipmentList.stream()
                .map(m -> EquipmentResponse.builder()
                        .id(m.getId())
                        .name(m.getName())
                        .description(m.getDescription())
                        .equipmentType(m.getEquipmentType())
                        .weight(m.getWeight())
                        .count(m.getCount())
                        .ruleId(m.getRule().getId())
                        .jobId(m.getJob().getId())
                        .imageUrl(m.getImageUrl())
                        .build())
                .collect(Collectors.toList());

        JobOptionListResponse response = JobOptionListResponse.builder()
                .jobActionList(jobActions)
                .jobRaceList(jobRaces)
                .jobBeliefList(jobBeliefs)
                .jobEquipmentList(equipments)
                .build();
        return response;
    }

    @Transactional(readOnly = true)
    @Override
    public CharacterActionListResponse getCommonActions(Long ruleID) {
        Rule rule = ruleRepository.findById(ruleID).get();
        List<CommonAction> commonActions = commonActionRepository.findByRule(rule);

        List<CharacterActionResponse> basicActions = commonActions.stream()
                .filter(CommonAction::getIsBasic) // isBasic is true
                .map(this::mapToCharacterActionResponse)
                .collect(Collectors.toList());

        List<CharacterActionResponse> specialActions = commonActions.stream()
                .filter(action -> !action.getIsBasic()) // isBasic is false
                .map(this::mapToCharacterActionResponse)
                .collect(Collectors.toList());

        return CharacterActionListResponse.builder()
                .basicActions(basicActions)
                .specialActions(specialActions)
                .build();
    }

    private CharacterActionResponse mapToCharacterActionResponse(CommonAction commonAction) {
        return CharacterActionResponse.builder()
                .id(commonAction.getId())
                .actionID(commonAction.getId())
                .name(commonAction.getName())
                .description(commonAction.getDescription())
                .isDice(commonAction.getIsDice())
                .diceType(commonAction.getDiceType())
                .diceCount(commonAction.getDiceCount())
                .build();
    }

}
