package dev.memocode.farmfarm_server.domain.entity;

import dev.memocode.farmfarm_server.domain.base_entity.UUIDAbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "local_house_section_sensor_measurements")
@EqualsAndHashCode(callSuper = true)
public class LocalHouseSectionSensorMeasurement extends UUIDAbstractEntity {
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "local_house_section_sensor_id")
    private LocalHouseSectionSensor localHouseSectionSensor;

    @Enumerated(STRING)
    @Column(name = "measurement_type")
    private MeasurementType measurementType;

    @Column(name = "value")
    private Float value;

    @Column(name = "measured_at")
    private Instant measuredAt;
}
