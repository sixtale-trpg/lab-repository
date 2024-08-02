package org.infinity.sixtalebackend.domain.rule.service;

import org.infinity.sixtalebackend.domain.rule.dto.JobListResponse;
import org.infinity.sixtalebackend.domain.rule.dto.JobOptionListResponse;

public interface RuleService {
    JobListResponse readJobList(Long ruleID);

    JobOptionListResponse readJobOptionList(Long ruleID, Long jobID);
}
