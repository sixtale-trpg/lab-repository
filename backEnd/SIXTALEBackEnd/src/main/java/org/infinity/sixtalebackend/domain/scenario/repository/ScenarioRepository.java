package org.infinity.sixtalebackend.domain.scenario.repository;


import org.infinity.sixtalebackend.domain.scenario.domain.Scenario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScenarioRepository extends JpaRepository<Scenario, Long> {
}
