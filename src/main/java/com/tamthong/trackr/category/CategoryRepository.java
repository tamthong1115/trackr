package com.tamthong.trackr.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findByUserIdAndUpdatedAtAfter(UUID userId, OffsetDateTime updatedAt);
    List<Category> findByUserId(UUID userId);
    void deleteByUserId(UUID userId);
}
