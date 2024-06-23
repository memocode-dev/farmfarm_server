package dev.memocode.farmfarm_server.domain.service.response;

import dev.memocode.farmfarm_server.domain.entity.MeasurementType;
import dev.memocode.farmfarm_server.domain.entity.SensorModelInfo;
import dev.memocode.farmfarm_server.domain.entity.SyncStatus;
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
    private UUID id;
    private String nameForAdmin;
    private String nameForUser;
    private SensorModelInfo sensorModelInfo;
    private String portName;
    private Instant createdAt;
    private Instant updatedAt;
    private SyncStatus syncStatus;
    private Map<MeasurementType, FindAllHouseSectionsResponse__HouseSectionSensorMeasurement> measurements;
}
