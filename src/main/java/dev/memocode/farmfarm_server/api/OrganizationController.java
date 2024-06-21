package dev.memocode.farmfarm_server.api;

import dev.memocode.farmfarm_server.api.form.CreateOrganizationForm;
import dev.memocode.farmfarm_server.service.OrganizationService;
import dev.memocode.farmfarm_server.service.request.CreateOrganizationRequest;
import dev.memocode.farmfarm_server.service.response.FindAllHousesResponse;
import dev.memocode.farmfarm_server.service.response.FindAllOrganizationsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
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

    @GetMapping
    @Operation(summary = "조직 전체 조회", description = "조직을 전체 조회할 수 있습니다.")
    public ResponseEntity<FindAllOrganizationsResponse> findAllHouses() {
        FindAllOrganizationsResponse response = organizationService.findAllOrganizations();

        return ResponseEntity.ok(response);
    }
}
