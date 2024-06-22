package dev.memocode.farmfarm_server.domain.service.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrganizationRequest {
    @NotBlank(message = "ORGANIZATION_NAME_NOT_BLANK:Name cannot be blank")
    private String name;
}
