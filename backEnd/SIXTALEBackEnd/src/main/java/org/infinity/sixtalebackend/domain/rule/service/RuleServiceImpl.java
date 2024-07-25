package org.infinity.sixtalebackend.domain.rule.service;

import lombok.AllArgsConstructor;
import org.infinity.sixtalebackend.domain.equipment.domain.Equipment;
import org.infinity.sixtalebackend.domain.equipment.repository.EquipmentRepository;
import org.infinity.sixtalebackend.domain.rule.domain.*;
import org.infinity.sixtalebackend.domain.rule.dto.JobListResponse;
import org.infinity.sixtalebackend.domain.rule.dto.JobOptionListResponse;
import org.infinity.sixtalebackend.domain.rule.repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class RuleServiceImpl implements RuleService {

    private final RuleRepository ruleRepository;
    private final JobRepository jobRepository;
    private final RaceRepository raceRepository;
    private final JobRaceRepository jobRaceRepository;
    private final BeliefRepository beliefRepository;
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
        List<Job> jobs = jobRepository.findByRule(rule);
        // 직업 액션
        List<JobAction> jobActions = jobActionRepository.findByRule(rule);
        // 액션 옵션
        List<List<ActionOption>> actionOptions = new ArrayList<>();
        for (JobAction jobAction : jobActions) {
            actionOptions.add(actionOptionRepository.findByJobAction(jobAction));
        }
        // 종족
        List<Race> races = raceRepository.findAll();
        List<List<JobRace>> jobRaces = new ArrayList<>();
        for (int i=0; i<races.size(); i++) {
            JobRaceID id = new JobRaceID(jobID, races.get(i).getId());
            jobRaces.get(i).add(jobRaceRepository.findById(id).get());
        }
        // 가치관
        List<Belief> beliefs = beliefRepository.findAll();
        List<List<JobBelief>> jobBeliefs = new ArrayList<>();
        for (int i=0; i<beliefs.size(); i++) {
            JobBeliefID id = new JobBeliefID(jobID, beliefs.get(i).getId());
            jobBeliefs.get(i).add(jobBeliefRepository.findById(id).get());
        }
        // 장비
        List<List<Equipment>> equipments = new ArrayList<>();
        for (Job job : jobs) {
            equipments.add(equipmentRepository.findByJob(job));
        }

        JobOptionListResponse response = new JobOptionListResponse();
        response.setJobActionList(jobActions);
        response.setActionOptionList(actionOptions);
        response.setJobRaceList(jobRaces);
        response.setJobBeliefList(jobBeliefs);
        response.setJobEquipmentList(equipments);
        return response;
    }
}
