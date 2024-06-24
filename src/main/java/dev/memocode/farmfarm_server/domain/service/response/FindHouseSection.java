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
public class FindHouseSection {
    @Schema(description = "하우스동 ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID id;

    @Schema(description = "하우스동 번호", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer sectionNumber;

    @Schema(description = "동기화 상태", requiredMode = Schema.RequiredMode.REQUIRED)
    private SyncStatus syncStatus;

    @Schema(description = "생성 날짜", requiredMode = Schema.RequiredMode.REQUIRED)
    private Instant createdAt;

    @Schema(description = "수정 날짜", requiredMode = Schema.RequiredMode.REQUIRED)
    private Instant updatedAt;

    @Schema(description = "삭제 날짜")
    private Instant deletedAt;

    @Schema(description = "삭제 여부", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean deleted;
}
