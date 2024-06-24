package dev.memocode.farmfarm_server.domain.service.response;

import dev.memocode.farmfarm_server.domain.entity.MeasurementType;
import dev.memocode.farmfarm_server.domain.entity.SensorModelInfo;
import dev.memocode.farmfarm_server.domain.entity.SyncStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindAllHouseSectionsResponse__HouseSectionSensor {
    @Schema(description = "하우스동 센서 ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID id;

    @Schema(description = "관리자용 센서 이름", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nameForAdmin;

    @Schema(description = "사용자용 센서 이름", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nameForUser;

    @Schema(description = "센서모델 정보", requiredMode = Schema.RequiredMode.REQUIRED)
    private SensorModelInfo sensorModelInfo;

    @Schema(description = "포트 이름", requiredMode = Schema.RequiredMode.REQUIRED)
    private String portName;

    @Schema(description = "생성날짜", requiredMode = Schema.RequiredMode.REQUIRED)
    private Instant createdAt;

    @Schema(description = "수정날짜", requiredMode = Schema.RequiredMode.REQUIRED)
    private Instant updatedAt;

    @Schema(description = "동기화 상태", requiredMode = Schema.RequiredMode.REQUIRED)
    private SyncStatus syncStatus;

    @Schema(description = "측정 데이터", requiredMode = Schema.RequiredMode.REQUIRED)
    private Map<MeasurementType, FindAllHouseSectionsResponse__HouseSectionSensorMeasurement> measurements;
}
