package dev.memocode.farmfarm_server.domain.service.response;

import dev.memocode.farmfarm_server.domain.entity.SyncStatus;
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
public class FindAllHousesResponse__House {
    @Schema(description = "하우스 ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID id;

    @Schema(description = "하우스 이름", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "생성 날짜", requiredMode = Schema.RequiredMode.REQUIRED)
    private Instant createdAt;

    @Schema(description = "수정 날짜", requiredMode = Schema.RequiredMode.REQUIRED)
    private Instant updatedAt;

    @Schema(description = "동기화 상태", requiredMode = Schema.RequiredMode.REQUIRED)
    private SyncStatus syncStatus;
}
