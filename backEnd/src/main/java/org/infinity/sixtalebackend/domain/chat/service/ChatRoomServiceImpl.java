package org.infinity.sixtalebackend.domain.chat.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.infinity.sixtalebackend.domain.chat.dto.ChatRoom;
import org.infinity.sixtalebackend.domain.room.domain.Room;
import org.infinity.sixtalebackend.domain.room.repository.RoomRepository;
import org.infinity.sixtalebackend.infra.redis.service.RedisSubscriber;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService{

    // 채팅방(topic)에 발행되는 메시지를 처리할 Listner
    private final RedisMessageListenerContainer redisMessageListener;
    // 구독 처리 서비스
    private final RedisSubscriber redisSubscriber;
    // Redis
    private static final String CHAT_ROOMS = "CHAT_ROOM";
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, ChatRoom> opsHashChatRoom;
    // 채팅방의 대화 메시지를 발행하기 위한 redis topic 정보. 서버별로 채팅방에 매치되는 topic정보를 Map에 넣어 roomId로 찾을수 있도록 한다.
    private Map<String, ChannelTopic> topics;
    private final RoomRepository roomRepository;

    @PostConstruct
    private void init() {
        opsHashChatRoom = redisTemplate.opsForHash();
        topics = new HashMap<>();
    }

    /**
     * CHAT_ROOM 이 MAP의 key임
     * @return
     */
    @Override
    public List<ChatRoom> findAllRoom() {
        return opsHashChatRoom.values(CHAT_ROOMS);
    }

    @Override
    public ChatRoom findRoomById(String id) {
        return opsHashChatRoom.get(CHAT_ROOMS, id);
    }

    /**
     * 채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash에 저장한다.
     */
    @Override
    public ChatRoom createChatRoom(Long roomID) {
        // 룸 정보 조회
        Room room = roomRepository.findById(roomID)
                .orElseThrow(() -> new IllegalArgumentException("게임 방을 찾을 수 없습니다."));
        // redisdp 넣을 값(id,name)으로 dto변경하기
        ChatRoom chatRoom = ChatRoom.builder()
                        .roomID(roomID.toString())
                                .name(room.getTitle()).build();
        // redis hash에 저장
        opsHashChatRoom.put(CHAT_ROOMS, roomID.toString(),chatRoom);
        return chatRoom;
    }

    @Override
    public void enterChatRoom(String roomID) {
        // 토픽 이미 생성되어 있는지 찾아보기
        ChannelTopic topic = topics.get(roomID);
        // 없으면 채널 새로 생성
        if (topic == null)
            topic = new ChannelTopic(roomID);
        // 통신을 위한 리스너 설정
        redisMessageListener.addMessageListener(redisSubscriber, topic);
        // 토픽 맵에 넣기
        topics.put(roomID, topic);
    }

    @Override
    public ChannelTopic getTopic(String roomID) {
        return topics.get(roomID);
    }
}
