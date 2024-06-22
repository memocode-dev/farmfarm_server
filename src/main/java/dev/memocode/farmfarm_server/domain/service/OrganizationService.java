package dev.memocode.farmfarm_server.domain.service;

import dev.memocode.farmfarm_server.domain.entity.Organization;
import dev.memocode.farmfarm_server.domain.exception.BusinessRuleViolationException;
import dev.memocode.farmfarm_server.domain.exception.NotFoundException;
import dev.memocode.farmfarm_server.domain.repository.OrganizationRepository;
import dev.memocode.farmfarm_server.domain.service.converter.OrganizationConverter;
import dev.memocode.farmfarm_server.domain.service.request.CreateOrganizationRequest;
import dev.memocode.farmfarm_server.domain.service.request.UpdateOrganizationRequest;
import dev.memocode.farmfarm_server.domain.service.response.FindAllOrganizationsResponse;
import dev.memocode.farmfarm_server.domain.service.response.FindOrganizationResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

import static dev.memocode.farmfarm_server.domain.exception.OrganizationErrorCode.ALREADY_EXISTS_ORGANIZATION_NAME;
import static dev.memocode.farmfarm_server.domain.exception.OrganizationErrorCode.NOT_FOUND_ORGANIZATION;

@Service
@Validated
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository organizationRepository;

    private final OrganizationConverter organizationConverter;

    @Transactional
    public UUID createOrganization(@Valid CreateOrganizationRequest request) {
        // 중복 이름 검사
        if (organizationRepository.existsByName(request.getName())) {
            throw new BusinessRuleViolationException(ALREADY_EXISTS_ORGANIZATION_NAME);
        }

        Organization organization = Organization.builder()
                .name(request.getName())
                .build();

        organizationRepository.save(organization);

        return organization.getId();
    }

    @Transactional
    public void updateOrganization(@Valid UpdateOrganizationRequest request) {
        // 조직 조회
        Organization organization = organizationRepository.findByIdAndDeleted(request.getOrganizationId(), false)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ORGANIZATION));

        // 현재 조직 이름이 아닌 경우
        if (!organization.getName().equals(request.getName())) {
            // 중복 이름 검사
            if (organizationRepository.existsByName(request.getName())) {
                throw new BusinessRuleViolationException(ALREADY_EXISTS_ORGANIZATION_NAME);
            }
        }

        // 조직 이름 변경
        organization.changeName(request.getName());
    }

    @Transactional
    public void deleteOrganization(
            @NotNull(message = "ORGANIZATION_ID_NOT_NULL:organization cannot be null") UUID organizationId) {
        // 조직 조회
        Organization organization = organizationRepository.findByIdAndDeleted(organizationId, false)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ORGANIZATION));

        // 조직 소프트 삭제
        organization.softDelete();
    }

    public FindAllOrganizationsResponse findAllOrganizations() {
        List<Organization> organizations = organizationRepository.findAllByDeleted(false);

        return FindAllOrganizationsResponse.builder()
                .organizations(organizationConverter.toFindAllOrganizationsResponse__Organization(organizations))
                .build();
    }

    public FindOrganizationResponse findOrganization(
            @NotNull(message = "ORGANIZATION_ID_NOT_NULL:organization cannot be null") UUID organizationId) {
        Organization organization = organizationRepository.findByIdAndDeleted(organizationId, false)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ORGANIZATION));

        return FindOrganizationResponse.builder()
                .id(organization.getId())
                .name(organization.getName())
                .createdAt(organization.getCreatedAt())
                .updatedAt(organization.getUpdatedAt())
                .build();
    }
}
