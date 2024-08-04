package org.infinity.sixtalebackend.domain.scenario.repository;

import org.infinity.sixtalebackend.domain.scenario.domain.ScenarioEquipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface ScenarioEquipmentRepository extends JpaRepository<ScenarioEquipment, Long> {
    List<ScenarioEquipment> findByJobId(Long jobId);
}
