package dev.memocode.farmfarm_server.domain.repository;

import dev.memocode.farmfarm_server.domain.entity.HouseSection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HouseSectionRepository extends JpaRepository<HouseSection, UUID> {
    Optional<HouseSection> findByIdAndDeleted(UUID id, boolean deleted);
    List<HouseSection> findAllByHouseIdAndDeleted(UUID houseId, boolean deleted);
}
