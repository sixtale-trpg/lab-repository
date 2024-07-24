package org.infinity.sixtalebackend.domain.room.service;

import lombok.AllArgsConstructor;
import org.infinity.sixtalebackend.domain.member.domain.Member;
import org.infinity.sixtalebackend.domain.member.repository.MemberRepository;
import org.infinity.sixtalebackend.domain.room.domain.PlayMember;
import org.infinity.sixtalebackend.domain.room.domain.Room;
import org.infinity.sixtalebackend.domain.room.domain.RoomStatus;
import org.infinity.sixtalebackend.domain.room.dto.RoomResponse;
import org.infinity.sixtalebackend.domain.room.repository.PlayMemberRepository;
import org.infinity.sixtalebackend.domain.room.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class RoomServiceImpl implements RoomService{
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final PlayMemberRepository playMemberRepository;

    @Transactional
    public RoomResponse addPlayerToRoom(Long roomID, Long memberID) {
        Room room = roomRepository.findById(roomID).orElseThrow(() -> new IllegalArgumentException("게임 방을 찾을 수 없습니다."));
        Member member = memberRepository.findById(memberID).orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        if (playMemberRepository.existsByRoomAndMember(room, member)) {
            throw new IllegalArgumentException("회원이 이미 게임 방에 있습니다.");
        }

        PlayMember playMember = PlayMember.builder()
                .room(room)
                .member(member)
                .build();
        playMemberRepository.save(playMember);

        RoomResponse roomResponse = RoomResponse.builder()
                .id(room.getId())
                .title(room.getTitle())
                .description(room.getDescription())
                .maxCount(room.getMaxCount())
                .isLocked(room.getIsLocked())
                .password(room.getPassword())
                .isShortStory(room.getIsShortStory())
                .isVoice(room.getIsVoice())
                .status(room.getStatus())
                .nextPlay(room.getNextPlay())
                .createdAt(room.getCreatedAt())
                .updatedAt(room.getUpdatedAt())
                .playTime(room.getPlayTime())
                .build();

        return roomResponse;
    }

    @Override
    public void updateRoomStatus(Long roomID, RoomStatus status) {
        Room room = roomRepository.findById(roomID).get();
        room.setStatus(status);
    }
}
