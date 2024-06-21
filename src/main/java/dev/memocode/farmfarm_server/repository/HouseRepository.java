package dev.memocode.farmfarm_server.repository;

import dev.memocode.farmfarm_server.domain.House;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HouseRepository extends JpaRepository<House, UUID> {
    boolean existsByName(String name);
    List<House> findAllByDeleted(boolean deleted);
    Optional<House> findByIdAndDeleted(UUID id, boolean deleted);
}
