package dev.memocode.farmfarm_server.domain.service.response;

import dev.memocode.farmfarm_server.domain.entity.MeasurementType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindAllHouseSectionSensorMeasurementsResponse__HouseSectionSensorMeasurement {
    private MeasurementType measurementType;
    private Float value;
    private Instant measuredAt;
}
