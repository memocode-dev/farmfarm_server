package dev.memocode.farmfarm_server.domain.service.response;

import dev.memocode.farmfarm_server.domain.entity.SensorModelInfo;
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
public class FindAllHouseSectionsResponse__HouseSectionSensor {
    private UUID id;
    private String nameForAdmin;
    private String nameForUser;
    private SensorModelInfo sensorModelInfo;
    private Instant createdAt;
    private Instant updatedAt;
}
