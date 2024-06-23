package dev.memocode.farmfarm_server.domain.entity;

import dev.memocode.farmfarm_server.domain.base_entity.IdentifiableSoftDeletableEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static java.time.temporal.ChronoUnit.MINUTES;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "house_section_sensors")
@EqualsAndHashCode(callSuper = true)
public class HouseSectionSensor extends IdentifiableSoftDeletableEntity {

    @Column(name = "name_for_admin")
    private String nameForAdmin;

    @Column(name = "name_for_user")
    private String nameForUser;

    @Enumerated(STRING)
    @Column(name = "sensor_model")
    private SensorModel sensorModel;

    @Column(name = "port_name")
    private String portName;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "house_section_id")
    private HouseSection houseSection;

    @OneToOne(mappedBy = "houseSectionSensor", fetch = LAZY)
    private LocalHouseSectionSensor localHouseSectionSensor;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    public SyncStatus getSyncStatus() {
        if (localHouseSectionSensorNotCreated()) {
            return SyncStatus.NOT_CREATED;
        }

        if (isLocalHouseSectionSensorUnhealthy()) {
            return SyncStatus.UNHEALTHY;
        }

        if (!areFieldsSynchronized()) {
            return SyncStatus.UNHEALTHY;
        }

        return SyncStatus.HEALTHY;
    }

    private boolean localHouseSectionSensorNotCreated() {
        return this.localHouseSectionSensor == null;
    }

    private boolean isLocalHouseSectionSensorUnhealthy() {
        return this.localHouseSectionSensor.getLastUpdatedAt().isBefore(Instant.now().minus(20, MINUTES));
    }

    private boolean areFieldsSynchronized() {
        return this.nameForAdmin.equals(this.localHouseSectionSensor.getNameForAdmin()) &&
                this.nameForUser.equals(this.localHouseSectionSensor.getNameForUser()) &&
                this.sensorModel.equals(this.localHouseSectionSensor.getSensorModel()) &&
                this.portName.equals(this.localHouseSectionSensor.getPortName()) &&
                this.version.equals(this.localHouseSectionSensor.getHouseSectionSensorVersion()) &&
                this.getCreatedAt().equals(this.localHouseSectionSensor.getCreatedAt()) &&
                this.getUpdatedAt().equals(this.localHouseSectionSensor.getUpdatedAt()) &&
                this.getDeleted().equals(this.localHouseSectionSensor.getDeleted()) &&
                (!this.getDeleted() || this.getDeletedAt().equals(this.localHouseSectionSensor.getDeletedAt()));
    }

    public void changePortName(String portName) {
        this.portName = portName;
    }

    public void changeNameForAdmin(String nameForAdmin) {
        this.nameForAdmin = nameForAdmin;
    }

    public void changeNameForUser(String nameForUser) {
        this.nameForUser = nameForUser;
    }
}
