package org.infinity.sixtalebackend.domain.rule.repository;

import com.jayway.jsonpath.JsonPath;
import org.infinity.sixtalebackend.domain.rule.domain.Job;
import org.infinity.sixtalebackend.domain.rule.domain.JobRace;
import org.infinity.sixtalebackend.domain.rule.domain.JobRaceID;
import org.infinity.sixtalebackend.domain.rule.domain.Race;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobRaceRepository extends JpaRepository<JobRace, JobRaceID> {
    Optional<JobRace> findByJobAndRace(Job job, Race race);
}
