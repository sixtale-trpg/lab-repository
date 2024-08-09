package org.infinity.sixtalebackend.domain.rule.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.infinity.sixtalebackend.domain.rule.domain.Job;

import java.util.List;

@Getter
@Builder
public class JobListResponse {
    private List<JobResponse> jobList;
}
