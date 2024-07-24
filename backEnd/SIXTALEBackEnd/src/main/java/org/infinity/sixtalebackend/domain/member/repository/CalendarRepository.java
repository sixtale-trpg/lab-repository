package org.infinity.sixtalebackend.domain.member.repository;

import org.infinity.sixtalebackend.domain.member.domain.Calender;
import org.infinity.sixtalebackend.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CalendarRepository extends JpaRepository<Calender, Long>{
    List<Calender> findByMember(Member member);

    Optional<Calender> findByIdAndMember(Long id, Member member);
}
