package dev.memocode.farmfarm_server.domain.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindAllHouseSectionSensorMeasurementsResponse {
    private List<FindAllHouseSectionSensorMeasurementsResponse__HouseSectionSensorMeasurement> measurements;
    private Instant startMeasuredAt;
    private Instant endMeasuredAt;
}
