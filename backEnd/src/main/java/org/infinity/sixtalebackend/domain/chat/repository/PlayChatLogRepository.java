package org.infinity.sixtalebackend.domain.chat.repository;

import org.infinity.sixtalebackend.domain.chat.domain.PlayChatLog;
import org.infinity.sixtalebackend.domain.chat.domain.WaitingChatLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayChatLogRepository extends MongoRepository<PlayChatLog,String> {
    Page<PlayChatLog> findByRoomID(Long roomID, Pageable pageable);
}
