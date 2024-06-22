package dev.memocode.farmfarm_server.api;

import dev.memocode.farmfarm_server.api.form.CreateOrganizationForm;
import dev.memocode.farmfarm_server.api.form.UpdateOrganizationForm;
import dev.memocode.farmfarm_server.domain.service.OrganizationService;
import dev.memocode.farmfarm_server.domain.service.request.CreateOrganizationRequest;
import dev.memocode.farmfarm_server.domain.service.request.UpdateOrganizationRequest;
import dev.memocode.farmfarm_server.domain.service.response.FindAllOrganizationsResponse;
import dev.memocode.farmfarm_server.domain.service.response.FindOrganizationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/organizations")
@Tag(name = "organizations", description = "조직")
public class OrganizationController {
    private final OrganizationService organizationService;

    @PostMapping
    @Operation(summary = "조직 생성", description = "조직을 생성할 수 있습니다.")
    public ResponseEntity<String> createOrganization(@RequestBody CreateOrganizationForm form) {
        CreateOrganizationRequest request = CreateOrganizationRequest.builder()
                .name(form.getName())
                .build();

        UUID organizationId = organizationService.createOrganization(request);

        return ResponseEntity.created(URI.create(organizationId.toString())).body(organizationId.toString());
    }

    @PatchMapping("/{organizationId}")
    @Operation(summary = "조직 수정", description = "조직를 수정할 수 있습니다.")
    public ResponseEntity<Void> updateOrganization(@PathVariable UUID organizationId, @RequestBody UpdateOrganizationForm form) {
        UpdateOrganizationRequest request = UpdateOrganizationRequest.builder()
                .organizationId(organizationId)
                .name(form.getName())
                .build();

        organizationService.updateOrganization(request);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{organizationId}")
    @Operation(summary = "조직 삭제", description = "조직을 삭제할 수 있습니다.")
    public ResponseEntity<Void> deleteOrganization(@PathVariable UUID organizationId) {
        organizationService.deleteOrganization(organizationId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "조직 전체 조회", description = "조직을 전체 조회할 수 있습니다.")
    public ResponseEntity<FindAllOrganizationsResponse> findAllOrganizations() {
        FindAllOrganizationsResponse response = organizationService.findAllOrganizations();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{organizationId}")
    @Operation(summary = "조직 단건 조회", description = "조직을 단건 조회할 수 있습니다.")
    public ResponseEntity<FindOrganizationResponse> findOrganization(@PathVariable UUID organizationId) {
        FindOrganizationResponse response = organizationService.findOrganization(organizationId);

        return ResponseEntity.ok(response);
    }
}
