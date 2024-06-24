package dev.memocode.farmfarm_server.domain.service.response;

import dev.memocode.farmfarm_server.domain.entity.SensorModel;
import dev.memocode.farmfarm_server.domain.entity.SyncStatus;
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
public class FindHouseSectionSensorResponse {
    private UUID id;
    private String nameForAdmin;
    private String nameForUser;
    private SensorModel sensorModel;
    private String portName;
    private SyncStatus syncStatus;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
    private Boolean deleted;
}
