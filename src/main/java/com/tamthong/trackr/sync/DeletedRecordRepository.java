package com.tamthong.trackr.sync;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface DeletedRecordRepository extends JpaRepository<DeletedRecord, UUID> {
    List<DeletedRecord> findByUserIdAndDeletedAtAfter(UUID userId, OffsetDateTime deletedAt);
    void deleteByUserId(UUID userId);
}
