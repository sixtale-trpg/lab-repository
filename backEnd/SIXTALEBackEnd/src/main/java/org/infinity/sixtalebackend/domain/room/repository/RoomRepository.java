package org.infinity.sixtalebackend.domain.room.repository;

import org.infinity.sixtalebackend.domain.room.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
