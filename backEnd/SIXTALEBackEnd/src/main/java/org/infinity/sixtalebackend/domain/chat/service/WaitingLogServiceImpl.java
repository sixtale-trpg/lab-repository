package org.infinity.sixtalebackend.domain.chat.service;

import lombok.RequiredArgsConstructor;
import org.infinity.sixtalebackend.domain.chat.domain.WaitingChatLog;
import org.infinity.sixtalebackend.domain.chat.domain.WaitingWhisperLog;
import org.infinity.sixtalebackend.domain.chat.dto.ChatMessageRequest;
import org.infinity.sixtalebackend.domain.chat.dto.MessageType;
import org.infinity.sixtalebackend.domain.chat.repository.WaitingChatLogRepository;
import org.infinity.sixtalebackend.domain.chat.repository.WaitingWhisperLogRepository;
import org.infinity.sixtalebackend.domain.member.domain.Member;
import org.infinity.sixtalebackend.domain.member.repository.MemberRepository;
import org.infinity.sixtalebackend.domain.room.domain.Room;
import org.infinity.sixtalebackend.domain.room.repository.RoomRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class WaitingLogServiceImpl implements WaitingLogService{
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final WaitingChatLogRepository waitingChatLogRepository;
    private final WaitingWhisperLogRepository waitingWhisperLogRepository;
    private final ChannelTopic channelTopic;
    private final RedisTemplate<String,String> redisTemplate;

    /**
     *
     * @param chatMessageRequest
     */
    @Transactional
    @Override
    public void sendWaitingChatMessage(ChatMessageRequest chatMessageRequest) {

        Member member = findMember(chatMessageRequest.getMemberID());
        Room room = findRoom(chatMessageRequest.getRoomID());

        // 채팅 메시지 생성 및 저장
        // 대기방 채팅인 경우(전체 전송)
        if (chatMessageRequest.getType() == MessageType.TALK) {
            WaitingChatLog waitingChatLog = WaitingChatLog.builder()
                    .roomID(room.getId())
                    .memberID(member.getId())
                    .content(chatMessageRequest.getContent())
                    .createdAt(LocalDateTime.now())
                    .build();

            // 대기방 전체 채팅 로그 저장
            waitingChatLogRepository.save(waitingChatLog);

        // 대기방 귓속말 채팅인 경우(1:1 채팅)
        } else if (chatMessageRequest.getType() == MessageType.WHISPER) {
            // 귓속말 상대
            Member recipient = findMember(chatMessageRequest.getRecipientID());

            WaitingWhisperLog waitingWhisperLog = WaitingWhisperLog.builder()
                    .roomID(room.getId())
                    .memberID(member.getId())
                    .recipientID(recipient.getId())
                    .content(chatMessageRequest.getContent())
                    .createdAt(LocalDateTime.now())
                    .build();
            // 대기방 귓속말 로그 저장
            waitingWhisperLogRepository.save(waitingWhisperLog);
        }

        // 토픽 받아오기
        String topic = channelTopic.getTopic();

        // ChatMessageRequest에 유저정보, 현재시간 저장
        chatMessageRequest.setNickName(member.getNickname());
        chatMessageRequest.setMemberID(member.getId());

        redisTemplate.convertAndSend(topic, chatMessageRequest);

    }

    private Member findMember(Long memberID){
        return memberRepository.findById(memberID)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다. id=" + memberID));
    }

    private Room findRoom(Long roomID){
        return roomRepository.findById(roomID)
                .orElseThrow(() -> new IllegalArgumentException("해당 방이 존재하지 않습니다. id=" + roomID));
    }


}
