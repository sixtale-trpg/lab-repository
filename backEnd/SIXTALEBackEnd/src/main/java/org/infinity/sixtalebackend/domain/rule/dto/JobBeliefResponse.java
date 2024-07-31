package org.infinity.sixtalebackend.domain.rule.dto;

import lombok.Getter;
import lombok.Setter;
import org.infinity.sixtalebackend.domain.rule.domain.Belief;
import org.infinity.sixtalebackend.domain.rule.domain.Job;
import org.infinity.sixtalebackend.domain.rule.domain.JobBeliefID;

@Getter
@Setter
public class JobBeliefResponse {
    private JobBeliefID id;
    private Job job;
    private Belief belief;
    private String description;
}
