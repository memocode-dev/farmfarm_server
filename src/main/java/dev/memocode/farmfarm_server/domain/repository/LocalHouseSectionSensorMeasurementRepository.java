package dev.memocode.farmfarm_server.domain.repository;

import dev.memocode.farmfarm_server.domain.entity.LocalHouseSectionSensorMeasurement;
import dev.memocode.farmfarm_server.domain.entity.MeasurementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LocalHouseSectionSensorMeasurementRepository
        extends JpaRepository<LocalHouseSectionSensorMeasurement, UUID> {
    Optional<LocalHouseSectionSensorMeasurement> findByLocalHouseSectionSensorIdAndMeasurementTypeAndMeasuredAt(
            UUID houseSectionSensorId, MeasurementType measurementType, Instant measuredAt);

    Optional<LocalHouseSectionSensorMeasurement> findTopByLocalHouseSectionSensorIdAndMeasurementTypeOrderByMeasuredAtDesc(
            UUID houseSectionSensorId, MeasurementType measurementType);

    @Query(value = "SELECT date_trunc('hour', m.measured_at) + interval '1 minute' * (floor(extract(minute from m.measured_at) / 10) * 10) AS interval_start, " +
            "m.measurement_type, " +
            "AVG(m.value) AS avg_value " +
            "FROM local_house_section_sensor_measurements m " +
            "WHERE m.local_house_section_sensor_id = :sensorId " +
            "AND m.measured_at BETWEEN :start AND :end " +
            "GROUP BY m.measurement_type, interval_start " +
            "ORDER BY interval_start, m.measurement_type", nativeQuery = true)
    List<Object[]> findAverageAllSensorDataByLocalHouseSectionSensorAndMeasuredAtBetween(
            @Param("sensorId") UUID sensorId,
            @Param("start") Instant startMeasuredAt,
            @Param("end") Instant endMeasuredAt);
}
