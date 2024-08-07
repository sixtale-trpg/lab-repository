package org.infinity.sixtalebackend.domain.rule.service;

import lombok.AllArgsConstructor;
import org.infinity.sixtalebackend.domain.equipment.domain.Equipment;
import org.infinity.sixtalebackend.domain.equipment.repository.EquipmentRepository;
import org.infinity.sixtalebackend.domain.rule.domain.*;
import org.infinity.sixtalebackend.domain.rule.dto.*;
import org.infinity.sixtalebackend.domain.rule.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
        List<Job> jobs = jobRepository.findByRule(rule);
        // 직업 액션
        List<JobAction> jobActions = jobActionRepository.findByRule(rule);
        // 액션 옵션
        List<List<ActionOption>> actionOptions = new ArrayList<>();
        for (JobAction jobAction : jobActions) {
            actionOptions.add(actionOptionRepository.findByJobAction(jobAction));
        }
        // 종족
        List<JobRace> jobRaces = jobRaceRepository.findByJob(job);
        // 가치관
        List<JobBelief> jobBeliefs = jobBeliefRepository.findByJob(job);
        // 장비
        List<Equipment> equipments = equipmentRepository.findByJob(job);

        JobOptionListResponse response = JobOptionListResponse.builder()
                .jobActionList(jobActions)
                .actionOptionList(actionOptions)
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
