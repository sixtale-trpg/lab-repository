package org.infinity.sixtalebackend.domain.rule.repository;

import org.infinity.sixtalebackend.domain.rule.domain.JobAction;
import org.infinity.sixtalebackend.domain.rule.domain.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobActionRepository extends JpaRepository<JobAction, Long> {
    List<JobAction> findByRule(Rule rule);
}
