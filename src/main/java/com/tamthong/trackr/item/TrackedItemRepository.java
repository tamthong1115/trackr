package com.tamthong.trackr.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TrackedItemRepository extends JpaRepository<TrackedItem, UUID> {
    List<TrackedItem> findByUserId(UUID userId);
    Optional<TrackedItem> findByIdAndUserId(UUID id, UUID userId);
    List<TrackedItem> findByUserIdAndUpdatedAtAfter(UUID userId, OffsetDateTime updatedAt);
    void deleteByUserId(UUID userId);
}
