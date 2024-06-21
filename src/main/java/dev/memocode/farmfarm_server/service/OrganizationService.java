package dev.memocode.farmfarm_server.service;

import dev.memocode.farmfarm_server.api.OrganizationController;
import dev.memocode.farmfarm_server.domain.Organization;
import dev.memocode.farmfarm_server.repository.OrganizationRepository;
import dev.memocode.farmfarm_server.service.converter.OrganizationConverter;
import dev.memocode.farmfarm_server.service.request.CreateOrganizationRequest;
import dev.memocode.farmfarm_server.service.response.FindAllOrganizationsResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Service
@Validated
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository organizationRepository;

    private final OrganizationConverter organizationConverter;

    public UUID createOrganization(@Valid CreateOrganizationRequest request) {

        Organization organization = Organization.builder()
                .name(request.getName())
                .build();

        organizationRepository.save(organization);

        return organization.getId();
    }

    public FindAllOrganizationsResponse findAllOrganizations() {
        List<Organization> organizations = organizationRepository.findAll();

        return FindAllOrganizationsResponse.builder()
                .organizations(organizationConverter.toFindAllOrganizationsResponse__Organization(organizations))
                .build();
    }
}
