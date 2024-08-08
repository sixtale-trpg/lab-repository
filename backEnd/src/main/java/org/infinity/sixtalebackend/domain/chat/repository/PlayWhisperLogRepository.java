package org.infinity.sixtalebackend.domain.chat.repository;

import org.infinity.sixtalebackend.domain.chat.domain.PlayWhisperLog;
import org.infinity.sixtalebackend.domain.chat.domain.WaitingWhisperLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayWhisperLogRepository extends MongoRepository<PlayWhisperLog,String> {
}
