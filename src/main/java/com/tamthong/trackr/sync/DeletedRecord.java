package com.tamthong.trackr.sync;

import com.tamthong.trackr.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "deleted_records")
public class DeletedRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "record_id", nullable = false)
    private UUID recordId;

    @Column(name = "table_name", nullable = false)
    private String tableName;

    @Column(name = "deleted_at", nullable = false)
    private OffsetDateTime deletedAt;
}
