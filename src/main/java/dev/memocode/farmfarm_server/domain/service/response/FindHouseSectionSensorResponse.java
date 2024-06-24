package dev.memocode.farmfarm_server.domain.service.response;

import dev.memocode.farmfarm_server.domain.entity.HouseSection;
import dev.memocode.farmfarm_server.domain.entity.LocalHouseSectionSensor;
import dev.memocode.farmfarm_server.domain.entity.SensorModel;
import dev.memocode.farmfarm_server.domain.entity.SyncStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;

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
