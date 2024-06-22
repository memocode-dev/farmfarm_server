package dev.memocode.farmfarm_server.domain.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindOrganizationResponse {
    private UUID id;
    private String name;
    private Instant createdAt;
    private Instant updatedAt;
}
