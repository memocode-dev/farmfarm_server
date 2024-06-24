package dev.memocode.farmfarm_server.domain.entity;

import dev.memocode.farmfarm_server.domain.base_entity.IdentifiableSoftDeletableEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;
import static java.time.temporal.ChronoUnit.MINUTES;
import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PRIVATE;

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

    @OneToMany(mappedBy = "house")
    @Getter(PRIVATE)
    private List<HouseSection> houseSections = new ArrayList<>();

    public void changeName(String name) {
        this.name = name;
    }

    public SyncStatus getSyncStatus() {
        if (localHouseNotCreated()) {
            return SyncStatus.NOT_CREATED;
        }

        if (isLocalHouseUnhealthy()) {
            return SyncStatus.UNHEALTHY;
        }

        if (!areFieldsSynchronized()) {
            return SyncStatus.UNHEALTHY;
        }

        return SyncStatus.HEALTHY;
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

    public boolean isReferenced() {
        return !getHouseSections(false).isEmpty();
    }

    private List<HouseSection> getHouseSections(boolean withDeleted) {
        return withDeleted ?
                this.houseSections :
                this.houseSections.stream()
                        .filter(houseSection -> !houseSection.getDeleted())
                        .toList();
    }
}
