package org.infinity.sixtalebackend.domain.room.service;

import org.infinity.sixtalebackend.domain.room.domain.RoomStatus;
import org.infinity.sixtalebackend.domain.room.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoomService {
    public RoomResponse addPlayerToRoom(Long roomID, Long memberID);

    void deletePlayerFromRoom(Long roomID, Long memberID);

    void updateRoomStatus(Long roomID, RoomStatus status);

    RoomResponse createRoom(RoomCreateRequest roomRequest, Long gmID);

    RoomDetailsResponse getRoomDetails(Long roomID);
    RoomUpdateResponse updateRoom(Long roomID, Long gmID, RoomUpdateRequest roomUpdateRequest);
    Page<RoomResponse> getRoomList(RoomStatus status, String title, Pageable pageable);
}
