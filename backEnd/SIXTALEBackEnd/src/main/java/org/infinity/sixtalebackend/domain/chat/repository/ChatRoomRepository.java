package org.infinity.sixtalebackend.domain.chat.repository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.infinity.sixtalebackend.domain.chat.domain.ChatRoom;
import org.infinity.sixtalebackend.domain.room.domain.Room;
import org.infinity.sixtalebackend.infra.redis.service.RedisSubscriber;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepository {

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

    @PostConstruct
    private void init() {
        opsHashChatRoom = redisTemplate.opsForHash();
        topics = new HashMap<>();
    }

    /**
     * CHAT_ROOM 이 MAP의 key임
     * @return
     */
    public List<ChatRoom> findAllRoom() {
        return opsHashChatRoom.values(CHAT_ROOMS);
    }

    public ChatRoom findRoomById(String id) {
        return opsHashChatRoom.get(CHAT_ROOMS, id);
    }

    /**
     * 채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash에 저장한다.
     */
    public ChatRoom createChatRoom(String name) {
        ChatRoom chatRoom = new ChatRoom("1",name);
        opsHashChatRoom.put(CHAT_ROOMS, chatRoom.getRoomID(), chatRoom);
        return chatRoom;
    }

    /**
     * 채팅방 입장 : redis에 topic을 만들고 pub/sub 통신을 하기 위해 리스너를 설정한다.
     */
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

    public ChannelTopic getTopic(String roomID) {
        return topics.get(roomID);
    }
}
