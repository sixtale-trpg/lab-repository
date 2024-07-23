package org.infinity.sixtalebackend.domain.room.repository;

import org.infinity.sixtalebackend.domain.member.domain.Member;
import org.infinity.sixtalebackend.domain.room.domain.PlayMember;
import org.infinity.sixtalebackend.domain.room.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayMemberRepository extends JpaRepository<PlayMember, Long> {
    boolean existsByRoomAndMember(Room room, Member member);
}
