package org.infinity.sixtalebackend.domain.rule.repository;

import org.infinity.sixtalebackend.domain.rule.domain.JobRace;
import org.infinity.sixtalebackend.domain.rule.domain.JobRaceID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRaceRepository extends JpaRepository<JobRace, JobRaceID> {
}
