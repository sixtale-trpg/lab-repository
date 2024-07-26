package org.infinity.sixtalebackend.domain.room.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.infinity.sixtalebackend.domain.member.domain.Member;
import org.infinity.sixtalebackend.domain.member.repository.MemberRepository;
import org.infinity.sixtalebackend.domain.room.domain.PlayMember;
import org.infinity.sixtalebackend.domain.room.domain.Room;
import org.infinity.sixtalebackend.domain.room.domain.RoomStatus;
import org.infinity.sixtalebackend.domain.room.dto.*;
import org.infinity.sixtalebackend.domain.room.repository.PlayMemberRepository;
import org.infinity.sixtalebackend.domain.room.repository.RoomRepository;
import org.infinity.sixtalebackend.domain.scenario.domain.Scenario;
import org.infinity.sixtalebackend.domain.scenario.repository.ScenarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class RoomServiceImpl implements RoomService{
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final PlayMemberRepository playMemberRepository;
    private final ScenarioRepository scenarioRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 게임 방 유저 입장
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

        //playMember 추가
        PlayMember playMember = PlayMember.builder()
                .room(room)
                .member(member)
                .build();
        playMemberRepository.save(playMember);

        // Room의 current_count 증가
        room.setCurrentCount((byte) (room.getCurrentCount() + 1));
        roomRepository.save(room);

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
     */
    @Override
    @Transactional
    public void deletePlayerFromRoom(Long roomID, Long memberID) {
        Room room = roomRepository.findById(roomID).orElseThrow(() -> new IllegalArgumentException("게임 방을 찾을 수 없습니다."));
        Member member = memberRepository.findById(memberID).orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        PlayMember playMember = playMemberRepository.findByRoomAndMember(room, member).orElseThrow(() -> new IllegalArgumentException("게임 방에 회원이 존재하지 않습니다."));

        playMemberRepository.delete(playMember);

        // Room의 current_count 감소
        room.setCurrentCount((byte) (room.getCurrentCount() - 1));
        roomRepository.save(room);
    }

    /**
     * 게임 방 상태 변경
     * @param roomID
     * @param status
     */
    @Override
    public void updateRoomStatus(Long roomID, RoomStatus status) {
        Room room = roomRepository.findById(roomID).get();
        room.setStatus(status);
    }

    /**
     * 게임 방 생성
     */
    @Override
    @Transactional
    public RoomResponse createRoom(RoomCreateRequest roomRequest, Long gmID) {
        Scenario scenario = scenarioRepository.findByIdWithRule(roomRequest.getScenarioID())
                .orElseThrow(() -> new IllegalArgumentException("시나리오를 찾을 수 없습니다."));
        Member gm = memberRepository.findById(gmID)
                .orElseThrow(() -> new IllegalArgumentException("게임 마스터를 찾을 수 없습니다."));

        // 비밀번호 방이면 비밀번호 암호화
        String encryptedPassword = null;
        if (roomRequest.getIsLocked()) {
            if (roomRequest.getPassword() == null || roomRequest.getPassword().isEmpty()) {
                throw new IllegalArgumentException("잠긴 방에는 비밀번호가 필요합니다.");
            }
            encryptedPassword = passwordEncoder.encode(roomRequest.getPassword());
        }

        Room room = Room.builder()
                .title(roomRequest.getTitle())
                .scenario(scenario)
                .rule(scenario.getRule())
                .gm(gm)
                .description(roomRequest.getDescription())
                .maxCount(roomRequest.getMaxCount())
                .isLocked(roomRequest.getIsLocked())
                .password(encryptedPassword)
                .isShortStory(roomRequest.getIsShortStory())
                .isVoice(roomRequest.getIsVoice())
                .status(RoomStatus.WAITING)
                .currentCount((byte) 1)  // 명시적 기본 값 설정
                .build();
        roomRepository.save(room);

        return RoomResponse.builder()
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
                .ruleId(room.getRule().getId())
                .gmId(room.getGm().getId())
                .build();

    }

    /**
     * 게임 방 정보 조회
     */
    @Override
    @Transactional(readOnly = true)
    public RoomDetailsResponse getRoomDetails(Long roomID) {
        Room room = roomRepository.findById(roomID)
                .orElseThrow(() -> new IllegalArgumentException("게임 방을 찾을 수 없습니다."));

        List<PlayMemberResponse> playMemberResponses = room.getPlayMembers().stream()
                .map(pm -> PlayMemberResponse.builder()
                        .playMemberID(pm.getMember().getId())
                        .playMemberNickname(pm.getMember().getNickname())
                        .playMemberImageURL(pm.getMember().getImageURL())
                        .build())
                .collect(Collectors.toList());

        return RoomDetailsResponse.builder()
                .id(room.getId())
                .title(room.getTitle())
                .description(room.getDescription())
                .scenarioID(room.getScenario().getId())
                .scenarioTitle(room.getScenario().getTitle())
                .scenarioImageURL(room.getScenario().getImageURL())
                .ruleID(room.getRule().getId())
                .ruleTitle(room.getRule().getTitle())
                .playMemberList(playMemberResponses)
                .gmID(room.getGm().getId())
                .gmNickname(room.getGm().getNickname())
                .gmImageURL(room.getGm().getImageURL())
                .currentCount(room.getCurrentCount())
                .maxCount(room.getMaxCount())
                .isLocked(room.getIsLocked())
                .password(room.getPassword())
                .isShortStory(room.getIsShortStory())
                .isVoice(room.getIsVoice())
                .status(room.getStatus().name())
                .nextPlay(room.getNextPlay())
                .createdAt(room.getCreatedAt())
                .updatedAt(room.getUpdatedAt())
                .playTime(room.getPlayTime())
                .build();
    }

    /**
     * 게임 방 정보 수정
     */
    @Override
    @Transactional
    public RoomUpdateResponse updateRoom(Long roomID, Long gmID, RoomUpdateRequest roomUpdateRequest) {
        Room room = roomRepository.findById(roomID)
                .orElseThrow(() -> new IllegalArgumentException("게임 방을 찾을 수 없습니다."));

        if (!room.getGm().getId().equals(gmID)) {
            throw new IllegalArgumentException("인증 권한 에러");
        }

        room.setTitle(roomUpdateRequest.getTitle());
        room.setDescription(roomUpdateRequest.getDescription());
        room.setIsLocked(roomUpdateRequest.getIsLocked());

        if (roomUpdateRequest.getIsLocked()) {
            if (roomUpdateRequest.getPassword() == null || roomUpdateRequest.getPassword().isEmpty()) {
                throw new IllegalArgumentException("잠긴 방에는 비밀번호가 필요합니다.");
            }
            room.setPassword(passwordEncoder.encode(roomUpdateRequest.getPassword()));
        } else {
            room.setPassword(null);
        }

        room.setIsShortStory(roomUpdateRequest.getIsShortStory());
        room.setIsVoice(roomUpdateRequest.getIsVoice());

        roomRepository.save(room);

        return RoomUpdateResponse.builder()
                .id(room.getId())
                .title(room.getTitle())
                .description(room.getDescription())
                .currentCount(room.getCurrentCount()) // 그냥 삭제함
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
    }

    /**
     * 게임 방 목록 조회
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RoomResponse> getRoomList(RoomStatus status, String title, Pageable pageable) {
        Page<Room> rooms = roomRepository.findByStatusAndTitle(status, title, pageable);

        return rooms.map(room -> RoomResponse.builder()
                .id(room.getId())
                .title(room.getTitle())
                .description(room.getDescription())
                .currentCount(room.getCurrentCount())
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
                .build());
    }
}
