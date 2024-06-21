package dev.memocode.farmfarm_server.repository;

import dev.memocode.farmfarm_server.domain.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
}
