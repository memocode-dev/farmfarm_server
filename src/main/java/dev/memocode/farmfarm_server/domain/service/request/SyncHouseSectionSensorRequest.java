package dev.memocode.farmfarm_server.domain.service.request;

import dev.memocode.farmfarm_server.domain.entity.SensorModel;
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
public class SyncHouseSectionSensorRequest {
    private UUID houseId;
    private UUID houseSectionId;
    private UUID houseSectionSensorId;
    private Long houseSectionSensorVersion;
    private String nameForAdmin;
    private String nameForUser;
    private SensorModel sensorModel;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
    private Boolean deleted;
}
