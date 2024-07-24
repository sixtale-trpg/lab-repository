package org.infinity.sixtalebackend.domain.room.service;

import org.infinity.sixtalebackend.domain.room.domain.RoomStatus;
import org.infinity.sixtalebackend.domain.room.dto.RoomResponse;

public interface RoomService {
    public RoomResponse addPlayerToRoom(Long roomID, Long memberID);

    void updateRoomStatus(Long roomID, RoomStatus status);
}
