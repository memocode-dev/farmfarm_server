package dev.memocode.farmfarm_server.domain.repository;

import dev.memocode.farmfarm_server.domain.entity.HouseSectionSensor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface HouseSectionSensorRepository extends JpaRepository<HouseSectionSensor, UUID> {
    Optional<HouseSectionSensor> findByIdAndDeleted(UUID houseSectionSensorId, boolean deleted);
}
