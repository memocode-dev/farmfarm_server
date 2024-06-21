package dev.memocode.farmfarm_server.service.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrganizationRequest {
    @NotNull(message = "ORGANIZATION_ID_NOT_NULL:organization cannot be null")
    private UUID organizationId;

    @NotBlank(message = "ORGANIZATION_NAME_NOT_BLANK:Name cannot be blank")
    private String name;
}
