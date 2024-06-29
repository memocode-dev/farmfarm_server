package dev.memocode.farmfarm_server.domain.service.request;

import jakarta.validation.constraints.NotNull;
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
public class FindAllHouseSectionSensorMeasurementsRequest {
    @NotNull
    private UUID houseId;

    @NotNull
    private UUID houseSectionId;

    @NotNull
    private UUID houseSectionSensorId;

    @NotNull
    private Instant startMeasuredAt;

    @NotNull
    private Instant endMeasuredAt;
}
