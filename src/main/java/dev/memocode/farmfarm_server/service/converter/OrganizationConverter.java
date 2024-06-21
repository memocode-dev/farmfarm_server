package dev.memocode.farmfarm_server.service.converter;

import dev.memocode.farmfarm_server.domain.Organization;
import dev.memocode.farmfarm_server.service.response.FindAllOrganizationsResponse__Organization;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrganizationConverter {
    public FindAllOrganizationsResponse__Organization toFindAllOrganizationsResponse__Organization(
            Organization organization) {
        return FindAllOrganizationsResponse__Organization.builder()
                .id(organization.getId())
                .name(organization.getName())
                .createdAt(organization.getCreatedAt())
                .updatedAt(organization.getUpdatedAt())
                .build();
    }

    public List<FindAllOrganizationsResponse__Organization> toFindAllOrganizationsResponse__Organization(
            List<Organization> organizations) {
        return organizations.stream()
                .map(this::toFindAllOrganizationsResponse__Organization)
                .toList();
    }
}
