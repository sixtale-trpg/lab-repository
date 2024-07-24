package org.infinity.sixtalebackend.domain.room.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.infinity.sixtalebackend.domain.member.domain.Member;
import org.infinity.sixtalebackend.domain.member.repository.MemberRepository;
import org.infinity.sixtalebackend.domain.room.domain.PlayMember;
import org.infinity.sixtalebackend.domain.room.domain.Room;
import org.infinity.sixtalebackend.domain.room.dto.RoomResponse;
import org.infinity.sixtalebackend.domain.room.repository.PlayMemberRepository;
import org.infinity.sixtalebackend.domain.room.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@AllArgsConstructor
public class RoomServiceImpl implements RoomService{
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final PlayMemberRepository playMemberRepository;

    /**
     * 게임 방 유저 입장
     * @param roomID
     * @param memberID
     * @return RoomResponse
     */
    @Override
    @Transactional
    public RoomResponse addPlayerToRoom(Long roomID, Long memberID) {
        Room room = roomRepository.findById(roomID).orElseThrow(() -> new IllegalArgumentException("게임 방을 찾을 수 없습니다."));
        Member member = memberRepository.findById(memberID).orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        // 이미 들어온 회원일 때
        if (playMemberRepository.existsByRoomAndMember(room, member)) {
            log.info("playMember already exists with memberID: {} and roomID: {}", memberID, roomID);
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

    /**
     * 게임 방 유저 퇴장
     * @param roomID
     * @param memberID
     */
    @Override
    @Transactional
    public void deletePlayerFromRoom(Long roomID, Long memberID) {
        Room room = roomRepository.findById(roomID).orElseThrow(() -> new IllegalArgumentException("게임 방을 찾을 수 없습니다."));
        Member member = memberRepository.findById(memberID).orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        PlayMember playMember = playMemberRepository.findByRoomAndMember(room, member).orElseThrow(() -> new IllegalArgumentException("게임 방에 회원이 존재하지 않습니다."));

        playMemberRepository.delete(playMember);
    }
}
