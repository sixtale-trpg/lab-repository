package org.infinity.sixtalebackend.domain.chat.repository;

import org.infinity.sixtalebackend.domain.chat.domain.WaitingChatLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaitingChatLogRepository extends MongoRepository<WaitingChatLog,String> {
}
