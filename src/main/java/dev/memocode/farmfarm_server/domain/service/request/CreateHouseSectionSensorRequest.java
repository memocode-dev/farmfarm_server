package dev.memocode.farmfarm_server.domain.service.request;

import dev.memocode.farmfarm_server.domain.entity.SensorModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateHouseSectionSensorRequest {
    private UUID houseId;
    private UUID houseSectionId;
    private String nameForAdmin;
    private String nameForUser;
    private SensorModel sensorModel;
}
