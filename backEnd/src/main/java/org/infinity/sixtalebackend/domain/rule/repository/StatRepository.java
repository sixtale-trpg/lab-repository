package org.infinity.sixtalebackend.domain.rule.repository;

import org.infinity.sixtalebackend.domain.rule.domain.Stat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatRepository extends JpaRepository<Stat, Long> {
}
