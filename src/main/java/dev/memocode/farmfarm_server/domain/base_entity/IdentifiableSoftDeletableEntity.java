package dev.memocode.farmfarm_server.domain.base_entity;

import dev.memocode.farmfarm_server.domain.exception.BusinessRuleViolationException;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

import static dev.memocode.farmfarm_server.domain.exception.BaseErrorCode.DELETED_AT_NULL;

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

    public void softDelete(Instant deletedAt) {
        if (deletedAt == null) {
            throw new BusinessRuleViolationException(DELETED_AT_NULL);
        }

        this.deleted = true;
        this.deletedAt = deletedAt;
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
