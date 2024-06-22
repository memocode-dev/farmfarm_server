package dev.memocode.farmfarm_server.domain.entity;

import dev.memocode.farmfarm_server.domain.base_entity.IdentifiableSoftDeletableEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

import static jakarta.persistence.FetchType.LAZY;
import static java.time.temporal.ChronoUnit.MINUTES;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "houses")
@EqualsAndHashCode(callSuper = true)
public class House extends IdentifiableSoftDeletableEntity {
    @Column(name = "name", unique = true)
    private String name;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @OneToOne(mappedBy = "house", fetch = LAZY)
    private LocalHouse localHouse;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    public void changeName(String name) {
        this.name = name;
    }

    public HouseStatus getStatus() {
        if (localHouseNotCreated()) {
            return HouseStatus.NOT_CREATED;
        }

        if (isLocalHouseUnhealthy()) {
            return HouseStatus.UNHEALTHY;
        }

        if (!areFieldsSynchronized()) {
            return HouseStatus.UNHEALTHY;
        }

        return HouseStatus.HEALTHY;
    }

    private boolean localHouseNotCreated() {
        return this.localHouse == null;
    }

    private boolean isLocalHouseUnhealthy() {
        return this.localHouse.getLastUpdatedAt().isBefore(Instant.now().minus(20, MINUTES));
    }

    private boolean areFieldsSynchronized() {
        return this.name.equals(this.localHouse.getName()) &&
                this.version.equals(this.localHouse.getHouseVersion()) &&
                this.getCreatedAt().equals(this.localHouse.getCreatedAt()) &&
                this.getUpdatedAt().equals(this.localHouse.getUpdatedAt()) &&
                this.getDeleted().equals(this.localHouse.getDeleted()) &&
                (!this.getDeleted() || this.getDeletedAt().equals(this.localHouse.getDeletedAt()));
    }
}
