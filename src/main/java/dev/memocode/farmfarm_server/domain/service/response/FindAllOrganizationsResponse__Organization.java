package dev.memocode.farmfarm_server.domain.service.response;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class FindAllOrganizationsResponse__Organization {
    @Schema(description = "조직 ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID id;

    @Schema(description = "조직 이름", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "생성 날짜", requiredMode = Schema.RequiredMode.REQUIRED)
    private Instant createdAt;

    @Schema(description = "수정 날짜", requiredMode = Schema.RequiredMode.REQUIRED)
    private Instant updatedAt;
}
