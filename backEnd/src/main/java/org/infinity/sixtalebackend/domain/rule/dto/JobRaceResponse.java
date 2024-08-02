package org.infinity.sixtalebackend.domain.rule.dto;

import lombok.Getter;
import lombok.Setter;
import org.infinity.sixtalebackend.domain.rule.domain.Job;
import org.infinity.sixtalebackend.domain.rule.domain.JobRaceID;
import org.infinity.sixtalebackend.domain.rule.domain.Race;

@Getter
@Setter
public class JobRaceResponse {
    private JobRaceID id;
    private Job job;
    private Race race;
    private String description;
}
