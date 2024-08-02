package org.infinity.sixtalebackend.domain.rule.service;

import lombok.AllArgsConstructor;
import org.infinity.sixtalebackend.domain.equipment.domain.Equipment;
import org.infinity.sixtalebackend.domain.equipment.repository.EquipmentRepository;
import org.infinity.sixtalebackend.domain.rule.domain.*;
import org.infinity.sixtalebackend.domain.rule.dto.JobListResponse;
import org.infinity.sixtalebackend.domain.rule.dto.JobOptionListResponse;
import org.infinity.sixtalebackend.domain.rule.dto.JobRaceResponse;
import org.infinity.sixtalebackend.domain.rule.repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
}
