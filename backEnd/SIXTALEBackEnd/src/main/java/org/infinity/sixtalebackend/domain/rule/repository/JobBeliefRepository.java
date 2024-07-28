package org.infinity.sixtalebackend.domain.rule.repository;

import org.infinity.sixtalebackend.domain.rule.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobBeliefRepository extends JpaRepository<JobBelief, JobBeliefID> {
    List<JobBelief> findByJob(Job job);
}
