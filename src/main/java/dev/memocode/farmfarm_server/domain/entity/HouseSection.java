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
@Table(name = "house_sections")
@EqualsAndHashCode(callSuper = true)
public class HouseSection extends IdentifiableSoftDeletableEntity {

    @Column(name = "section_number")
    private Integer sectionNumber;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "house_id")
    private House house;

    @OneToOne(mappedBy = "houseSection", fetch = LAZY)
    private LocalHouseSection localHouseSection;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    public void changeSectionNumber(Integer sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    public SyncStatus getSyncStatus() {
        if (localHouseSectionNotCreated()) {
            return SyncStatus.NOT_CREATED;
        }

        if (isLocalHouseSectionUnhealthy()) {
            return SyncStatus.UNHEALTHY;
        }

        if (!areFieldsSynchronized()) {
            return SyncStatus.UNHEALTHY;
        }

        return SyncStatus.HEALTHY;
    }

    private boolean localHouseSectionNotCreated() {
        return this.localHouseSection == null;
    }

    private boolean isLocalHouseSectionUnhealthy() {
        return this.localHouseSection.getLastUpdatedAt().isBefore(Instant.now().minus(20, MINUTES));
    }

    private boolean areFieldsSynchronized() {
        return this.sectionNumber.equals(this.localHouseSection.getSectionNumber()) &&
                this.version.equals(this.localHouseSection.getHouseSectionVersion()) &&
                this.getCreatedAt().equals(this.localHouseSection.getCreatedAt()) &&
                this.getUpdatedAt().equals(this.localHouseSection.getUpdatedAt()) &&
                this.getDeleted().equals(this.localHouseSection.getDeleted()) &&
                (!this.getDeleted() || this.getDeletedAt().equals(this.localHouseSection.getDeletedAt()));
    }
}