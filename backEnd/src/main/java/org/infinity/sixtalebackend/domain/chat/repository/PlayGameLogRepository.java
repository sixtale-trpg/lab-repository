package org.infinity.sixtalebackend.domain.chat.repository;

import org.infinity.sixtalebackend.domain.chat.domain.PlayGameLog;
import org.infinity.sixtalebackend.domain.chat.domain.PlayWhisperLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayGameLogRepository extends MongoRepository<PlayGameLog,String> {
}
