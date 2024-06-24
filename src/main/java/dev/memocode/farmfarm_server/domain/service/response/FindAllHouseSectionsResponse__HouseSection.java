package dev.memocode.farmfarm_server.domain.service.response;

import dev.memocode.farmfarm_server.domain.entity.SyncStatus;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindAllHouseSectionsResponse__HouseSection {
    @Schema(description = "하우스동 ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID id;

    @Schema(description = "하우스동 번호", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer sectionNumber;

    @Schema(description = "생성날짜", requiredMode = Schema.RequiredMode.REQUIRED)
    private Instant createdAt;

    @Schema(description = "수정날짜", requiredMode = Schema.RequiredMode.REQUIRED)
    private Instant updatedAt;

    @Schema(description = "동기화 상태", requiredMode = Schema.RequiredMode.REQUIRED)
    private SyncStatus syncStatus;

    @ArraySchema(schema = @Schema(implementation = FindAllHouseSectionsResponse__HouseSectionSensor.class))
    private List<FindAllHouseSectionsResponse__HouseSectionSensor> sensors;
}
