package dev.memocode.farmfarm_server.domain.repository;

import dev.memocode.farmfarm_server.domain.entity.LocalHouseSectionSensorMeasurement;
import dev.memocode.farmfarm_server.domain.entity.MeasurementType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface LocalHouseSectionSensorMeasurementRepository
        extends JpaRepository<LocalHouseSectionSensorMeasurement, UUID> {
    Optional<LocalHouseSectionSensorMeasurement> findByLocalHouseSectionSensorIdAndMeasurementTypeAndMeasuredAt(
            UUID houseSectionSensorId, MeasurementType measurementType, Instant measuredAt);

    Optional<LocalHouseSectionSensorMeasurement> findTopByLocalHouseSectionSensorIdAndMeasurementTypeOrderByMeasuredAtDesc(
            UUID houseSectionSensorId, MeasurementType measurementType);
}
