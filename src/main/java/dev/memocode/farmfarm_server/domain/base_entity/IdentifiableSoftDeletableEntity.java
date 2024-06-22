package dev.memocode.farmfarm_server.domain.base_entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
public abstract class IdentifiableSoftDeletableEntity extends IdentifiableTimeManagementEntity {
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    public void softDelete() {
        this.deleted = true;
        this.deletedAt = Instant.now();
    }

    public void recover() {
        this.deleted = false;
        this.deletedAt = null;
    }

    @PrePersist
    protected void onCreateDeleted() {
        if (this.deleted == null) {
            this.deleted = false;
        }
    }
}
