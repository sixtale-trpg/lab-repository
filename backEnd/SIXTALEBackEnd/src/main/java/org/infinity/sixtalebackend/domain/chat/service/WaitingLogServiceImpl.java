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
import org.infinity.sixtalebackend.domain.room.repository.RoomRepository;
import org.infinity.sixtalebackend.infra.redis.service.RedisPublisher;
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
    private final ChatRoomService chatRoomService;
    private final RedisPublisher redisPublisher;

    /**
     * 대기방 채팅, 귓속말 채팅 기능
     * @param chatMessageRequest
     */
    @Transactional
    @Override
    public void sendWaitingChatMessage(ChatMessageRequest chatMessageRequest) {

        Member member = findMember(chatMessageRequest.getMemberID());
        // 닉네임 저장
        chatMessageRequest.setNickName(member.getNickname());

        findRoom(chatMessageRequest.getRoomID());

        if (MessageType.ENTER.equals(chatMessageRequest.getType())) {
            // 채팅 입장 시, 룸 아이디 토픽없으면 토픽 생성 -> pub/sub기능 할 수 있도록 리스너 설정
            chatRoomService.enterChatRoom(chatMessageRequest.getRoomID().toString());
            chatMessageRequest.setContent(chatMessageRequest.getNickName()+ "님이 입장하셨습니다.");
        }
        if(MessageType.WHISPER.equals(chatMessageRequest.getType())){
            handleWhisperMessage(chatMessageRequest.getRoomID(), member, chatMessageRequest);
        }else {
            handleChatMessage(chatMessageRequest.getRoomID(), member, chatMessageRequest);
        }
        // Websocket에 발행된 메시지를 redis로 발행한다(publish)
        redisPublisher.publish(chatRoomService.getTopic(String.valueOf(chatMessageRequest.getRoomID())), chatMessageRequest);
    }

    /**
     * 전체 전송 로그 저장
     * @param roomID
     * @param member
     * @param chatMessageRequest
     */
    private void handleChatMessage(Long roomID, Member member, ChatMessageRequest chatMessageRequest) {
        WaitingChatLog waitingChatLog = WaitingChatLog.builder()
                .roomID(roomID)
                .memberID(member.getId())
                .content(chatMessageRequest.getContent())
                .createdAt(LocalDateTime.now())
                .build();

        waitingChatLogRepository.save(waitingChatLog);

        // redisTemplate.convertAndSend(topic, chatMessageRequest);
    }

    /**
     * 귓속말 로그 저장
     * @param roomID
     * @param member
     * @param chatMessageRequest
     */
    private void handleWhisperMessage(Long roomID, Member member, ChatMessageRequest chatMessageRequest) {
        Member recipient = findMember(chatMessageRequest.getRecipientID());
        chatMessageRequest.setRecipientNickName(recipient.getNickname());

        WaitingWhisperLog waitingWhisperLog = WaitingWhisperLog.builder()
                .roomID(roomID)
                .memberID(member.getId())
                .recipientID(recipient.getId())
                .content(chatMessageRequest.getContent())
                .createdAt(LocalDateTime.now())
                .build();

        waitingWhisperLogRepository.save(waitingWhisperLog);

        // String topic = channelTopic.getTopic();
        // redisTemplate.convertAndSend(topic, chatMessageRequest);

    }

    private Member findMember(Long memberID){
        return memberRepository.findById(memberID)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다. id=" + memberID));
    }

    private Boolean findRoom(Long roomID){
        boolean existRoom = roomRepository.existsById(roomID);
        if(!existRoom) throw new IllegalArgumentException("해당 방이 존재하지 않습니다. id=" + roomID);
        return true;
    }

}
