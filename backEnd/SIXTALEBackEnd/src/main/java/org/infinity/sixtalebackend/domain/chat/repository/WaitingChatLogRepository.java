package org.infinity.sixtalebackend.domain.chat.repository;

import org.infinity.sixtalebackend.domain.chat.domain.WaitingChatLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaitingChatLogRepository extends MongoRepository<WaitingChatLog,String> {
    List<WaitingChatLog> findByRoomID(Long roomID);
}
