package dev.memocode.farmfarm_server.repository;

import dev.memocode.farmfarm_server.domain.House;
import dev.memocode.farmfarm_server.domain.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
    boolean existsByName(String name);
    List<Organization> findAllByDeleted(boolean deleted);
    Optional<Organization> findByIdAndDeleted(UUID id, boolean deleted);
}
