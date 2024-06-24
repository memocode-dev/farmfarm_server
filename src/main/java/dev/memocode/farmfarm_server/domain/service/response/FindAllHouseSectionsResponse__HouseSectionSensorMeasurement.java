package dev.memocode.farmfarm_server.domain.service.response;

import dev.memocode.farmfarm_server.domain.entity.MeasurementType;
import dev.memocode.farmfarm_server.domain.entity.SyncStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindAllHouseSectionsResponse__HouseSectionSensorMeasurement {
    @Schema(description = "측정 유형", requiredMode = Schema.RequiredMode.REQUIRED)
    private MeasurementType measurementType;

    @Schema(description = "측정 값", requiredMode = Schema.RequiredMode.REQUIRED)
    private Float value;

    @Schema(description = "측정 날짜", requiredMode = Schema.RequiredMode.REQUIRED)
    private Instant measuredAt;

    @Schema(description = "동기화 상태", requiredMode = Schema.RequiredMode.REQUIRED)
    private SyncStatus syncStatus;
}
