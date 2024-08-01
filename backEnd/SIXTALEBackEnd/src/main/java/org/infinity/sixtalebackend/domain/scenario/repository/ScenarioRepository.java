package org.infinity.sixtalebackend.domain.scenario.repository;


import org.infinity.sixtalebackend.domain.scenario.domain.Scenario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScenarioRepository extends JpaRepository<Scenario, Long> {

    @Query("SELECT s FROM Scenario s JOIN FETCH s.rule WHERE s.id = :id")
    Optional<Scenario> findByIdWithRule(Long id);
}
