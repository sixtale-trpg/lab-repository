package org.infinity.sixtalebackend.domain.chat.repository;

import org.infinity.sixtalebackend.domain.chat.domain.WaitingChatLog;
import org.infinity.sixtalebackend.domain.chat.domain.WaitingWhisperLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaitingWhisperLogRepository extends MongoRepository<WaitingWhisperLog,String> {
    Page<WaitingWhisperLog> findByRoomIDAndRecipientID(Long roomID,Long recipientID, Pageable pageable);
}
