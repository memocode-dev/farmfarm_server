package dev.memocode.farmfarm_server.domain.entity;

import dev.memocode.farmfarm_server.domain.base_entity.IdentifiableSoftDeletableEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;

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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "house_section_id")
    private HouseSection houseSection;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;
}
