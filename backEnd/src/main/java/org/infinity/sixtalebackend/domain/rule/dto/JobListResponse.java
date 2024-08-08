package org.infinity.sixtalebackend.domain.rule.dto;

import lombok.Getter;
import lombok.Setter;
import org.infinity.sixtalebackend.domain.rule.domain.Job;

import java.util.List;

@Getter
@Setter
public class JobListResponse {
    private List<Job> jobList;
}
