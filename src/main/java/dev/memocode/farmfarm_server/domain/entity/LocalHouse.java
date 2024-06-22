package dev.memocode.farmfarm_server.domain.entity;

import dev.memocode.farmfarm_server.domain.base_entity.UUIDAbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "local_houses")
@EqualsAndHashCode(callSuper = true)
public class LocalHouse extends UUIDAbstractEntity {
    @Column(name = "name")
    private String name;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "house_id")
    private House house;

    @Column(name = "house_version", nullable = false)
    private Long houseVersion;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Column(name = "last_updated_at")
    private Instant lastUpdatedAt;

    public void changeName(String name) {
        this.name = name;
    }

    public void changeHouseVersion(Long houseVersion) {
        this.houseVersion = houseVersion;
    }

    public void changeCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void changeUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void changeDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void changeDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public void changeLastUpdatedAt(Instant lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }
}
