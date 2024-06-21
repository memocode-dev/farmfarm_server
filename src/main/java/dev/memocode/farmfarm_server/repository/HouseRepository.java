package dev.memocode.farmfarm_server.repository;

import dev.memocode.farmfarm_server.domain.House;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HouseRepository extends JpaRepository<House, UUID> {
}
