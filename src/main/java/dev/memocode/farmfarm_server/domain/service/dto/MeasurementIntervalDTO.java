package dev.memocode.farmfarm_server.domain.service.dto;

import com.querydsl.core.annotations.QueryProjection;
import dev.memocode.farmfarm_server.domain.entity.MeasurementType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor
public class MeasurementIntervalDTO {
    private Instant measuredAt;
    private MeasurementType measurementType;
    private Float value;

    @QueryProjection
    public MeasurementIntervalDTO(Instant measuredAt, MeasurementType measurementType, Float value) {
        this.measuredAt = measuredAt;
        this.measurementType = measurementType;
        this.value = value;
    }
}
